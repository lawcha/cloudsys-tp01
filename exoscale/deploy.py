import exoscale
from ssh2.session import Session
import os
import socket

exo = exoscale.Exoscale()


def exec_ssh_cmd(cmd, host):
    username = 'ubuntu'

    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    sock.connect((host, 22))

    session = Session()
    session.handshake(sock)
    session.agent_auth(username)

    session = Session()
    session.userauth_publickey_fromfile(username, '~/.ssh/keys/exoscale')
    channel = session.open_session()
    channel.execute(cmd)

    size, data = channel.read()

    while size > 0:
        print(data)
        size, data = channel.read()
    channel.close()
    print("Exit status: %s" % channel.get_exit_status())


def instance_not_exists(zone, name):
    try:
        list(exo.compute.list_instances(zone, name))[0]
    except IndexError as e:
        return True
    return False


def new_instance(exo, name, template_name, zone, security_group, user_data, lan_ip, private_network, type='tiny'):
    # My account doesn't have enough credit to reserve and assign elastic ip to the instance
    # Here's the function to create and assing elastic ip
    # elastic_ip = exo.compute.create_elastic_ip(zone)
    # instance.attach_elastic_ip(elastic_ip)

    ssh_key_name = exo.compute.get_ssh_key('exoscale')

    # exoscale api doesn't store personnal templates in list_templates
    personnal_templates = {
        'database-template': '3556f73c-7af4-464d-888d-2652739f011f', 'frontend-template': 'df0f0c0d-85a1-43b4-a81e-66ee5cbc72d0', 'backend-template': '99cb93a5-c3ab-488c-b6db-a8394bcf409f'}
    template_id = exo.compute.get_instance_template(
        zone=zone, id=personnal_templates.get(template_name))

    if instance_not_exists(zone, name):
        print("Start the deployment of vm %s with private ip %s" % (name, lan_ip))
        try:
            instance = exo.compute.create_instance(
                name=name,
                zone=zone,
                type=exo.compute.get_instance_type(type),
                template=template_id,
                volume_size=10,
                security_groups=[security_group],
                user_data=user_data.format(lan_ip=lan_ip),
                private_networks=[private_network],
                ssh_key=ssh_key_name
            )

            print("VM %s is deployed. Accessible at ssh ubuntu@%s -i ~/.ssh/keys/exoscale" %
                  (instance.name, instance.ipv4_address))
            return instance
        except exoscale.api.APIException as e:
            print(e)
            pass

    else:
        id_instance = list(exo.compute.list_instances(zone, name))[0].id
        instance = exo.compute.get_instance(zone=zone, id=id_instance)
        print("VM %s is already deployed. Accessible at ssh ubuntu@%s -i ~/.ssh/keys/exoscale" %
              (instance.name, instance.ipv4_address))
        return instance


def create_priv_nets(name):
    try:
        priv_nets = exo.compute.list_private_networks(zone=zone_gva2)
        for priv_net_exist in priv_nets:
            print(priv_net_exist)
            if priv_net_exist.name == name:
                lab1_exist = True
                return priv_net_exist
                break

        if not(lab1_exist):
            priv_net = exo.compute.create_private_network(
                zone=zone_gva2, name=name, description="lan", start_ip="10.0.0.2", end_ip="10.0.0.253", netmask="255.255.255.0")
        print(priv_net.id)
    except exoscale.api.APIException as e:
        print(e)


zone_gva2 = exo.compute.get_zone("ch-gva-2")


try:
    security_group_web = exo.compute.create_security_group("web")

    rules = [
        exoscale.api.compute.SecurityGroupRule.ingress(
            description="HTTP",
            network_cidr="0.0.0.0/0",
            port="80",
            protocol="tcp"
        ),
        exoscale.api.compute.SecurityGroupRule.ingress(
            description="HTTP",
            network_cidr="0.0.0.0/0",
            port="8080",
            protocol="tcp"
        ),
        exoscale.api.compute.SecurityGroupRule.ingress(
            description="SSH",
            network_cidr="0.0.0.0/0",
            port="443",
            protocol="tcp"
        ),
        exoscale.api.compute.SecurityGroupRule.ingress(
            description="SSH",
            network_cidr="0.0.0.0/0",
            port="22",
            protocol="tcp"
        )
    ]
    for rule in rules:
        security_group_web.add_rule(rule)
except exoscale.api.APIException as e:
    print(e)
    pass

user_data = """#cloud-config
    package_upgrade: true
    packages:
        - nginx
    write_files:
    - path: /etc/netplan/eth1.yaml
        content: |
            network:
            version: 2
            renderer: networkd
            ethernets:
                eth1:
                    addresses:
                        - {lan_ip}/24
    runcmd:
        - [netplan,apply]
"""

created_instances = []

created_instances.append(new_instance(
    exo=exo, name="database", template_name="database-template", zone=zone_gva2, security_group=exo.compute.get_security_group(name='default'), user_data=user_data, lan_ip='10.0.0.138', private_network=create_priv_nets('LAN')))
created_instances.append(new_instance(
    exo=exo, name="backend", template_name="backend-template", zone=zone_gva2, security_group=exo.compute.get_security_group(name='web'), user_data=user_data, lan_ip='10.0.0.40', private_network=create_priv_nets('LAN')))
created_instances.append(new_instance(
    exo=exo, name="frontend", template_name="frontend-template", zone=zone_gva2, security_group=exo.compute.get_security_group(name='web'), user_data=user_data, lan_ip='10.0.0.28', private_network=create_priv_nets('LAN')))


for instance in created_instances:
    if instance.name == 'backend':
        exec_ssh_cmd(
            '/home/ubuntu/cloudsys-tp01/code/backend/gradlew bootRuRun --daemon &', instance.ipv4_address)
    if instance.name == 'frontend':
        exec_ssh_cmd(
            'cd /home/ubuntu/cloudsys-tp01/code/frontend/ ; ng serve --port=80 --host=0.0.0.0', instance.ipv4_address)
