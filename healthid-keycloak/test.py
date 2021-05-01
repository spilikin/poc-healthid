# Code is based on this tutorial:
#    - https://www.stefaanlippens.net/oauth-code-flow-pkce.html
#
import base64
import hashlib
import html
import json
import os
import re
import urllib.parse
import requests
from pprint import pprint

verify=False
provider = "http://localhost:8080/auth/realms/healthid"
username = "user1"
challenge_signature_function = lambda challenge: challenge

client_id = "account-console"
redirect_uri = "http://localhost:8080/auth/realms/healthid/account/#/"

code_verifier = base64.urlsafe_b64encode(os.urandom(40)).decode('utf-8')
code_verifier = re.sub('[^a-zA-Z0-9]+', '', code_verifier)
print(code_verifier, len(code_verifier) )

code_challenge = hashlib.sha256(code_verifier.encode('utf-8')).digest()
code_challenge = base64.urlsafe_b64encode(code_challenge).decode('utf-8')
code_challenge = code_challenge.replace('=', '')
print (code_challenge, len(code_challenge))

state = base64.urlsafe_b64encode(os.urandom(40))
resp = requests.get(
    url=provider + "/protocol/openid-connect/auth",
    params={
        "response_type": "code",
        "client_id": client_id,
        "scope": "openid",
        "redirect_uri": redirect_uri,
        "state": state,
        "code_challenge": code_challenge,
        "code_challenge_method": "S256",
    },
    headers={'Accept': 'application/json'},
    allow_redirects=False,
    verify=verify
)

if resp.status_code >= 400:
    exit (resp.text)

cookie = resp.headers['Set-Cookie']
cookie = '; '.join(c.split(';')[0] for c in cookie.split(', '))
print(cookie)

print(resp.headers['content-type'])
print (resp.text)
challenge = resp.json()

resp = requests.post(
    url=challenge['endpoint'], 
    data={
        "command": "poll",
    }, 
    headers={"Cookie": cookie, 'Accept': 'application/json'},
    allow_redirects=False,
    verify=verify
)
print(resp.status_code)
print(resp.headers['content-type'])
print(resp.text)

# Keycloak endpoint URI can only be used once, so update it
challenge['endpoint'] = resp.json()['endpoint']

resp = requests.post(
    url=challenge['endpoint'], 
    data={
        "command": "challenge_response",
        "username": username,
        "signature": challenge_signature_function(challenge['challenge'])
    }, 
    headers={"Cookie": cookie, 'Accept': 'application/json'},
    allow_redirects=False,
    verify=verify
)
print(resp.status_code)
print(resp.text)

challenge['endpoint'] = resp.json()['endpoint']

resp = requests.post(
    url=challenge['endpoint'], 
    data={
        "command": "finish",
        "device_code": challenge['device_code'],
    }, 
    headers={"Cookie": cookie},
    allow_redirects=False,
    verify=verify
)

print(resp.status_code)

if not 'Location' in resp.headers:
    exit("Error: redirect was expected")

redirect = resp.headers['Location']
print(redirect)

query = urllib.parse.urlparse(redirect).query
redirect_params = urllib.parse.parse_qs(query)
print(redirect_params)

auth_code = redirect_params['code'][0]
print(auth_code)

resp = requests.post(
    url=provider + "/protocol/openid-connect/token",
    data={
        "grant_type": "authorization_code",
        "client_id": client_id,
        "redirect_uri": redirect_uri,
        "code": auth_code,
        "code_verifier": code_verifier,
    },
    allow_redirects=False,
    verify=verify
)
print(resp.status_code)

result = resp.json()
print(result)

def _b64_decode(data):
    data += '=' * (4 - len(data) % 4)
    return base64.b64decode(data).decode('utf-8')

def jwt_payload_decode(jwt):
    _, payload, _ = jwt.split('.')
    return json.dumps(json.loads(_b64_decode(payload)), indent=2)

print(jwt_payload_decode(result['access_token']))

print(jwt_payload_decode(result['id_token']))