#  Copyright (c) 2024 Contributors to the Eclipse Foundation
#
#  This program and the accompanying materials are made available under the
#  terms of the Apache License, Version 2.0 which is available at
#  https://www.apache.org/licenses/LICENSE-2.0.
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
#  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
#  License for the specific language governing permissions and limitations
#  under the License.
#
#  SPDX-License-Identifier: Apache-2.0

import json
import os
import sys

from jinja2 import Environment, FileSystemLoader

# Configuration for the host and port
input_file_path = '../src/main/resources/VehicleService.json'

if len(sys.argv) > 1:
    input_file_path = sys.argv[1]

# Load JSON data
with open(input_file_path, 'r') as file:
    data = json.load(file)


# Define filters to convert the first letter of a string to lowercase and uppercase
def lower_first(s):
    return s[0].lower() + s[1:] if s else s


def upper_first(s):
    return s[0].upper() + s[1:] if s else s


# Load templates
env = Environment(loader=FileSystemLoader('templates'))

# Register filters to Jinja2 environment
env.filters['lower_first'] = lower_first
env.filters['upper_first'] = upper_first
env.filters['capitalize'] = str.capitalize

# Define a type mapping from JSON types to Java types
type_mapping = {
    "int32": "int",
    "int64": "long",
    "bool": "boolean",
    "double": "double",
    "string": "String",
    "bytes": "Bytes",
    "void": "void",  # Ensure void is handled
}


# Ensure parameters are properly structured
def preprocess_params(params):
    for param in params:
        param['name'] = param['name'].split(".")[-1]
        param['type'] = convert_type(param['type'])
    return params


# Example conversion function
def convert_type(json_type):
    return type_mapping.get(json_type, json_type)


# Load templates for client, server, and main classes
client_template = env.get_template('client_template.j2')

# Extract the services and SDK information
services = data['ServiceDsl']['services']
sdk_info = data['VehicleSdk']
output_file_name = f"{sdk_info['name']}.java"

package_name = sdk_info['package']
package_path = package_name.replace(".", "/")
output_dir_client = f"../build/generated/source/proto/main/java/{package_path}"

os.makedirs(output_dir_client, exist_ok=True)

# Extract hostname and port from SDK info
hostname = sdk_info['host']['hostname'] if sdk_info['host']['hostname'] else "127.0.0.1"
port = sdk_info['host']['port']

# Update request and response parameters with the correct Java types and preprocess names
for service in services:
    for rpc in service['rpcs']:
        rpc['request']['parameter'] = preprocess_params(rpc['request']['parameter'])
        rpc['response']['parameter'] = preprocess_params(rpc['response']['parameter'])

for api in sdk_info['apis']:
    api['parameter'] = preprocess_params(api['parameter'])
    for step_index, step in enumerate(api['flow']):
        step['parameters'] = []
        for param in step['parameter']:
            param_name = param.split(".")[-1]
            param_mapped = param_name
            if param_mapped.find("=") != -1:
                pieces = param_mapped.split("=")
                param_name = pieces[0]
                param_mapped = pieces[1]
            param_type = type_mapping.get(param_name, param_name)
            step['parameters'].append({
                'param_name': param_name,
                'param_type': param_type,
                'param_mapped': param_mapped
            })
        # Dynamically generate the request and response names
        step['request_var'] = f"request{step_index + 1 if step_index > 0 else ''}"
        step['response_var'] = f"response{step_index + 1 if step_index > 0 else ''}"
    api['response']['type'] = convert_type(api['response']['type'])

# Render the templates for client and server classes without the main function
client_code = client_template.render(
    package_name=package_name,
    services=services,
    sdk_info=sdk_info,
    hostname=hostname,
    port=port
)

# Save the generated code to corresponding files
output_file_path = os.path.join(output_dir_client, output_file_name)
with open(output_file_path, 'w') as client_file:
    client_file.write(client_code)
    print(f"gRPC Client API '{output_file_name}' generated successfully!")
