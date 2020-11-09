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

def ssh_port(conn):
  sec_group = conn.network.find_security_group(SECURITY_GROUP)
  if not sec_group:
    print("Create a security group and set up SSH ingress:")
    print('------------------------------------------------------------------------\n')

    sec_group = conn.network.create_security_group(
        name=SECURITY_GROUP)

    ssh_rule = conn.network.create_security_group_rule(
        security_group_id=sec_group.id,
        direction='ingress',
        remote_ip_prefix='0.0.0.0/0',
        protocol='TCP',
        port_range_max='22',
        port_range_min='22',
        ethertype='IPv4')

  return sec_group

def create_network(conn):
  print("Use network : " + NETWORK_NAME)
  network = conn.network.find_network(NETWORK_NAME)
  return network
  
def create_keypair(conn):
  keypair = conn.compute.find_keypair(KEYPAIR_NAME)
  if not keypair:
  
      print("Create a Key Pair:")
      print('------------------------------------------------------------------------\n')
      keypair = conn.compute.create_keypair(name=KEYPAIR_NAME)

      try:
          os.mkdir(SSH_DIR)
      except OSError as e:
          if e.errno != errno.EEXIST:
              raise e

      with open(PRIVATE_KEYPAIR_FILE, 'w') as f:
          f.write("%s" % keypair.private_key)

      #os.chmod(PRIVATE_KEYPAIR_FILE, 0o400)
      
      print(PUBLIC_KEYPAIR_FILE)

  return keypair
  
def create_security_group_server(conn):
  print("SG-Server")
  print('------------------------------------------------------------------------\n')

  sec_group = conn.network.create_security_group(
        name="WebServerBackend")

  ssh_rule = conn.network.create_security_group_rule(
        security_group_id=sec_group.id,
        direction='ingress',
        remote_ip_prefix='0.0.0.0/0',
        protocol='TCP',
        port_range_max='8080',
        port_range_min='8080',
        ethertype='IPv4')

  return sec_group
  
def create_security_group_database(conn):
  print("SG-Server")
  print('------------------------------------------------------------------------\n')

  sec_group = conn.network.create_security_group(
        name="Database")

  ssh_rule = conn.network.create_security_group_rule(
        security_group_id=sec_group.id,
        direction='ingress',
        remote_ip_prefix='0.0.0.0/0',
        protocol='TCP',
        port_range_max='5432',
        port_range_min='5432',
        ethertype='IPv4')

  return sec_group
  
def create_security_group_client(conn):
  print("SG-Client")
  print('------------------------------------------------------------------------\n')

  sec_group = conn.network.create_security_group(
        name="WebServerClient")

  ssh_rule = conn.network.create_security_group_rule(
        security_group_id=sec_group.id,
        direction='ingress',
        remote_ip_prefix='0.0.0.0/0',
        protocol='TCP',
        port_range_max='4200',
        port_range_min='4200',
        ethertype='IPv4')

  return sec_group

def create_instance_database(conn):
  image = conn.compute.find_image("Database-snapshot")
  flavor = conn.compute.find_flavor("m1.small")
  network = create_network(conn)
  security_group = [conn.network.find_security_group("SSH"),create_security_group_database(conn)]
  keypair = create_keypair(conn)

  server = conn.compute.create_server(
      name="Database-PassionCuisine-API", image_id=image.id, flavor_id=flavor.id,
      networks=[{"uuid": network.id}], key_name=keypair.name, security_groups=security_group)
  server = conn.compute.wait_for_server(server)
  
def create_instance_server(conn):
  image = conn.compute.find_image("Server-Snapshot")
  flavor = conn.compute.find_flavor("m1.small")
  network = create_network(conn)
  security_group = [conn.network.find_security_group("SSH"),create_security_group_server(conn)]
  keypair = create_keypair(conn)

  server = conn.compute.create_server(
      name="Server-PassionCuisine-API", image_id=image.id, flavor_id=flavor.id,
      networks=[{"uuid": network.id}], key_name=keypair.name, security_groups=security_group)
  server = conn.compute.wait_for_server(server)
  
def create_instance_client(conn):
  image = conn.compute.find_image("PassionCuisine-Client-Snapshot")
  flavor = conn.compute.find_flavor("m1.small")
  network = create_network(conn)
  security_group = [conn.network.find_security_group("SSH"),create_security_group_client(conn)]
  keypair = create_keypair(conn)

  server = conn.compute.create_server(
      name="Client-PassionCuisine-API", image_id=image.id, flavor_id=flavor.id,
      networks=[{"uuid": network.id}], key_name=keypair.name, security_groups=security_group)
  server = conn.compute.wait_for_server(server)
 
def create_connection_from_config():
  return openstack.connect()

  
conn = create_connection_from_config()
  
create_instance_database(conn)
create_instance_server(conn)
create_instance_client(conn)
