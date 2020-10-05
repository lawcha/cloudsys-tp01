import requests
import argparse

parser = argparse.ArgumentParser(
        description=__doc__,
        formatter_class=argparse.RawDescriptionHelpFormatter)
parser.add_argument('filename', help='The name of the file you want to run')
parser.add_argument('project', help='The name of your project')
parser.add_argument('--region', choices=["LS", "ZH"], help='region strings', default="ZH")

args = parser.parse_args()

file = open(args.filename, mode='r')
code = file.read()
file.close()

file = open("token", mode='r')
token = file.read()
file.close()
res = requests.post('https://cloudsys.gind.re', json={"code": code, "project": args.project, "token": token, "region": args.region})
try:
    res = res.json()
    print("status:\n", res['status'])
    print("stdout:\n", res['stdout'])
    print("stderr:\n", res['stderr'])
except ValueError:  # includes simplejson.decoder.JSONDecodeError
    res = requests.post('https://cloudsys.gind.re', json={"code": code, "project": args.project, "token": token, "region": args.region})
    print(res.request.body)
    print(res.request.headers)
    print ('Decoding JSON has failed')
