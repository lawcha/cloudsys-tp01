
################################################# WEB METHOD !!Does not work!! #################################################
import requests
class WebAzureDeployer(object):
    # Auth infos
    resource = "https://management.azure.com"
    client_id = "955580cc-1bfc-4d98-9d51-efe0a513a373"
    tenant_id = "a372f724-c0b2-4ea0-abfb-0eb8c6f84e40"
    token = ""
    
    location = "westeurope"
    sub_id = ""
    res_grp = ""
    vm_name = ""
    def __init__(self, client_secret, sub_id, res_grp, location):
        if (sub_id == "" or res_grp == ""):
            raise ValueError("Please provide subID and resource group.")
        self.sub_id = sub_id
        self.res_grp = res_grp
        self.location = location
        self.token = self.get_token(client_secret)
        
    def get_token(self, client_secret):
        url= "https://login.microsoftonline.com/a372f724-c0b2-4ea0-abfb-0eb8c6f84e40/oauth2/token"
        data = {
            "grant_type": "client_credentials",
            "client_id": self.client_id,
            "client_secret": client_secret,
            "resource": self.resource,
        }
        print(data)
        response = requests.post(url, json=data)
        print(response.content)
        return ""

    def get_storakge_profile(self, sub_id, image_name):
        properties = {
          "imageReference": {
            "id": "/subscriptions/{0}/resourceGroups/myResourceGroup/providers/Microsoft.Compute/images/{1}".format(sub_id, image_name)
          },
          "osDisk": {
            "caching": "ReadWrite",
            "managedDisk": {
              "storageAccountType": "Standard_LRS"
            },
            "name": "{0}-disk".format(image_name),
            "createOption": "FromImage"
          }
        }
        return properties

    def create(self, vm_name, image_name):
        url = 'https://management.azure.com/subscriptions/{0}/resourceGroups/{1}/providers/Microsoft.Compute/virtualMachines/{2}?api-version=2020-06-01'.format(self.sub_id, self.res_grp, vm_name)
        data = {
            "location": self.location,
            "properties": self.get_image_properties(self.sub_id, image_name)
        }
        response = requests.put(url, json=data)
        print(response)

    def start(self, vm_name):
        url = 'https://management.azure.com/subscriptions/{0}/resourceGroups/{1}/providers/Microsoft.Compute/virtualMachines/{2}/start?api-version=2020-06-01'.format(self.sub_id, self.res_grp, vm_name)
        response = requests.post(url)

    def stop(self, vm_name):
        url = 'https://management.azure.com/subscriptions/{0}/resourceGroups/{1}/providers/Microsoft.Compute/virtualMachines/{2}/powerOff?api-version=2020-06-01'.format(self.sub_id, self.res_grp, vm_name)
        response = requests.post(url)

##################################################################################################################################
from azure.identity import ClientSecretCredential
from azure.mgmt.resource import ResourceManagementClient
from azure.mgmt.compute import ComputeManagementClient
from azure.mgmt.network import NetworkManagementClient
from azure.mgmt.compute.models import DiskCreateOption

class AzureDeployer(object):
    compute_client = None
    network_client = None
    security_group = "CloudSys-front-nsg"

    def __init__(self, client_secret, sub_id, res_grp, location):
        if (sub_id == "" or res_grp == ""):
            raise ValueError("Please provide subID and resource group.")
        self.sub_id = sub_id
        self.res_grp = res_grp
        self.location = location
        
        credentials = self.get_credentials()
        self.compute_client = ComputeManagementClient(
            credentials,
            subscription_id
        )
        self.network_client = NetworkManagementClient(
            credentials,
            sub_id
        )

    def get_credentials(self):
        credentials = ClientSecretCredential(
            client_id = "955580cc-1bfc-4d98-9d51-efe0a513a373",
            client_secret = client_secret,
            tenant_id = "a372f724-c0b2-4ea0-abfb-0eb8c6f84e40"
        )
        return credentials
    
    def get_storage_profile(self, sub_id, image_name):
        properties = {
          "imageReference": {
            "id": "/subscriptions/{0}/resourceGroups/{1}/providers/Microsoft.Compute/images/{2}".format(sub_id, self.res_grp, image_name)
          },
          "osDisk": {
            "caching": "ReadWrite",
            "managedDisk": {
              "storageAccountType": "Standard_LRS"
            },
            "name": "{0}-disk".format(image_name),
            "createOption": "FromImage"
          }
        }
        return properties

    def create_vnet(self, vm_name):
        vnet_params = {
            'location': 'westeurope',
            'address_space': {
                'address_prefixes': ['10.0.0.0/16']
            }
        }
        creation_result = self.network_client.virtual_networks.begin_create_or_update(
            self.res_grp,
            vm_name + '-vnet',
            vnet_params
        )
        return creation_result.result()

    def create_public_ip_address(self, vm_name):
        public_ip_addess_params = {
            'location': self.location,
            'public_ip_allocation_method': 'Dynamic'
        }
        creation_result = self.network_client.public_ip_addresses.begin_create_or_update(
            self.res_grp,
            vm_name + '-ip',
            public_ip_addess_params
        )
        return creation_result.result()

    def create_subnet(self, vm_name):
        subnet_params = {
            'address_prefix': '10.0.0.0/24'
        }
        creation_result = self.network_client.subnets.begin_create_or_update(
            self.res_grp,
            vm_name + '-vnet',
            vm_name + '-subnet',
            subnet_params
        )

        return creation_result.result()

    def create_nic(self, vm_name):
        subnet_info = self.network_client.subnets.get(
            self.res_grp,
            vm_name + '-vnet',
            vm_name + '-subnet'
        )
        publicIPAddress = self.network_client.public_ip_addresses.get(
            self.res_grp,
            vm_name + "-ip"
        )
        nic_params = {
            'location': "westeurope",
            'ip_configurations': [{
                'name': 'myIPConfig',
                'public_ip_address': publicIPAddress,
                'subnet': subnet_info
            }],
            "network_security_group": {
                "id": "/subscriptions/{0}/resourceGroups/CloudSys/providers/Microsoft.Network/networkSecurityGroups/{1}".format(self.sub_id, self.security_group)
            }
        }
        creation_result = self.network_client.network_interfaces.begin_create_or_update(
            self.res_grp,
            vm_name + '-nic',
            nic_params
        )
        return publicIPAddress.ip_address

    def create_vm(self, vm_name, image_name):
        nic = self.network_client.network_interfaces.get(
           self.res_grp,
           vm_name + '-nic'
        )
        #avset = self.compute_client.availability_sets.get(
        #    GROUP_NAME,
        #    'myAVSet'
        #)
        vm_parameters = {
            'location': self.location,
            'os_profile': {
                'computer_name': vm_name,
                'admin_username': 'azureuser',
                'admin_password': 'Emf123456'
            },
            'hardware_profile': {
                'vm_size': 'Standard_B1ls'
            },
            'storage_profile': self.get_storage_profile(self.sub_id, image_name),
            "network_profile": {
                "network_interfaces": [{
                    "id": nic.id,
                }]
            }
        }
        creation_result = self.compute_client.virtual_machines.begin_create_or_update(
            self.res_grp, 
            vm_name, 
            vm_parameters
        )

        return creation_result.result()


client_id = "955580cc-1bfc-4d98-9d51-efe0a513a373"
tenant_id = "a372f724-c0b2-4ea0-abfb-0eb8c6f84e40"
subscription_id = "2feb225c-7655-45c2-8f53-b106fd4c357d" # To get with command "az login"
client_secret = "L.O8rpUi9UFqy2BXXxGON2b~gs9WQuu.5-"
resources_group = "CloudSys"

vms = [
  { "vm": "CloudSys-front", "image": "CloudSys-front-image" },
  { "vm": "CloudSys-back", "image": "CloudSys-back-image" },
  { "vm": "CloudSys-db", "image": "CloudSys-db-image" },
]

deployer = AzureDeployer(client_secret, subscription_id, resources_group, "westeurope")
# deployer.create_vnet("CloudSys")
# deployer.create_subnet("CloudSys")

for vm in vms:
    print("Creating", vm["vm"], "from", vm["image"], "...")
    # deployer.create_public_ip_address(vm["vm"])
    ip = deployer.create_nic(vm["vm"])
    # deployer.create_vm(vm["vm"], vm["image"])
    print("Creation complete! Runs at IP :", ip)
