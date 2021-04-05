from keycloak import KeycloakAdmin
from pprint import pprint

keycloak_admin = KeycloakAdmin(server_url="http://localhost:8080/auth/",
                               username='admin',
                               password='admin',
                               realm_name="master",
                               user_realm_name="master",
                               verify=True)

realm = next((realm for realm in keycloak_admin.get_realms() if realm['realm'] == 'healthid'), None)

#if realm != None:
#    keycloak_admin.delete_realm('healthid')

#keycloak_admin.create_realm(payload={"realm": "healthid", "enabled": True}, skip_exists=False)

keycloak_admin = KeycloakAdmin(server_url="http://localhost:8080/auth/",
                               username='admin',
                               password='admin',
                               realm_name="healthid",
                               user_realm_name="master",
                               verify=True)

new_user = keycloak_admin.create_user({
    "email": "user1@healthid.life",
    "username": "user1",
    "enabled": True,
    "attributes": {"example": "1,2,3,3,"}})

users = keycloak_admin.get_users({})
pprint(users)

flows = keycloak_admin.get_authentication_flows()
pprint(flows)

keycloak_admin.create_authentication_flow(
{'alias': 'healthid-browser-flow5',
  'authenticationExecutions': [{'authenticator': 'auth-cookie',
                                'autheticatorFlow': False,
                                'priority': 0,
                                'requirement': 'ALTERNATIVE',
                                'userSetupAllowed': False},
                               {'authenticator': 'registration-page-form',
                                'autheticatorFlow': True,
                                'flowAlias': 'Forms',
                                'priority': 1,
                                'requirement': 'ALTERNATIVE',
                                'userSetupAllowed': False},
                               {'authenticator': 'auth-username-form',
                                'autheticatorFlow': False,
                                'priority': 2,
                                'requirement': 'REQUIRED',
                                'userSetupAllowed': False},
                               {'authenticator': 'healthid-authenticator',
                                'autheticatorFlow': False,
                                'priority': 3,
                                'requirement': 'REQUIRED',
                                'userSetupAllowed': False}],
  'builtIn': False,
  'description': '',
  'id': 'f1274ead-1abf-4381-b49c-c9a8fa09228d',
  'providerId': 'basic-flow',
  'topLevel': True
})