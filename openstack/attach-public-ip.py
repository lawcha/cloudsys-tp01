import os
import sys
import errno
import openstack

KEYPAIR_NAME="PassionCuisineAPI"

SSH_DIR = '/home/nicolas/.ssh'
PUBLIC_KEYPAIR_FILE = '{ssh_dir}/passioncuisineapi.pub'.format(ssh_dir=SSH_DIR)
PRIVATE_KEYPAIR_FILE = '{ssh_dir}/passioncuisineapi'.format(ssh_dir=SSH_DIR)
NETWORK_NAME = 'private'
SECURITY_GROUP = "SSH"  
  
def attach_floating_ip(conn, server_name):
  print('Attaching floating IP to instance:')
  print('------------------------------------------------------------------------\n')
  instance = conn.compute.find_server(server_name)
  floating_IP = conn.network.find_available_ip()

  if floating_IP:
    conn.compute.add_floating_ip_to_server(instance,floating_IP.floating_ip_address)
    print('Allocated a floating IP. To access your instance use : ssh -i {key} ubuntu@{ip} -o "StrictHostKeyChecking no"'.format(key=PRIVATE_KEYPAIR_FILE, ip=floating_IP.floating_ip_address))
  else:
    conn.network.create_ip(floating_network_id='849ab1e9-7ac5-4618-8801-e6176fbbcf30')
    floating_IP = conn.network.find_available_ip()
    conn.compute.add_floating_ip_to_server(instance,floating_IP.floating_ip_address)
    print('Created a floating IP. To access your instance use : ssh -i {key} ubuntu@{ip} -o "StrictHostKeyChecking no"'.format(key=PRIVATE_KEYPAIR_FILE, ip=floating_IP.floating_ip_address))


  return floating_IP
  
def create_connection_from_config():
  return openstack.connect()

  
conn = create_connection_from_config()
  
server_ip = attach_floating_ip(conn,"Server-PassionCuisine-API").floating_ip_address
client_ip = attach_floating_ip(conn,"Client-PassionCuisine-API").floating_ip_address
db_ip = attach_floating_ip(conn,"Database-PassionCuisine-API").floating_ip_address

"""
When the IP is attached, we can access the instances through SSH and start the applications

For client : 
    cd /home/ubuntu/cloudsys-tp01/code/frontend/
    ng serve --port=4200 --host=0.0.0.0'

For server : 
    /home/ubuntu/cloudsys-tp01/code/backend/.gradlew bootRun
"""
