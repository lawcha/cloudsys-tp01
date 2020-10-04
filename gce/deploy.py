#!/usr/bin/env python

import argparse
import os
import time

import googleapiclient.discovery
from six.moves import input
import json


def list_instances(compute, project, zone):
    result = compute.instances().list(project=project, zone=zone).execute()
    return result['items'] if 'items' in result else None


def create_instance(compute, project, zone, config_file, script_file):
    startup_script = open(
        os.path.join(
            os.path.dirname(__file__), script_file), 'r').read()

    with open(config_file) as json_file:
        config = json.load(json_file)

    config['metadata']['items'][0]['value'] = startup_script

    return compute.instances().insert(project=project, zone=zone, body=config).execute()


def delete_instance(compute, project, zone, name):
    return compute.instances().delete(project=project, zone=zone, instance=name).execute()


def wait_for_operation(compute, project, zone, operation):
    print('Waiting for operation to finish...')
    while True:
        result = compute.zoneOperations().get(project=project, zone=zone,
                                              operation=operation).execute()

        if result['status'] == 'DONE':
            print("done.")
            if 'error' in result:
                raise Exception(result['error'])
            return result

        time.sleep(1)


if __name__ == '__main__':
    project = "silent-caster-290308"
    zone = "europe-west6-a"
    wait_s = 10

    instances = []
    instances.append({'name': 'backend-instance', 'config': 'config-backend.json',
                      'script': 'startup-script-backend.sh'})
    instances.append({'name': 'frontend-instance', 'config': 'config-frontend.json',
                      'script': 'startup-script-frontend.sh'})

    compute = googleapiclient.discovery.build('compute', 'v1')

    for instance in instances:
        print(f"Creating {instance['name']} instance")
        operation = create_instance(
            compute, project, zone, instance['config'], instance['script'])
        wait_for_operation(compute, project, zone, operation['name'])

    instances = list_instances(compute, project, zone)

    print(f'In {wait_s} the instance will be deleted, for lesson purposes')
    time.sleep(wait_s)

    for instance in instances:
        print(f"Deleting {instance['name']} instance")

        operation = delete_instance(compute, project, zone, instance['name'])
        wait_for_operation(compute, project, zone, operation['name'])
