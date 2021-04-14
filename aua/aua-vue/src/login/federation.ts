export interface IdentityProvider {
  id: string;
  title: string;
  subtitle: string;
  logo_uri: string;
  authorization_endpoint: string;
}

export class FederationQuery {
  get identityProviderList(): Array<IdentityProvider> {
    return [
      {
        id: "id.acme.spilikin.dev",
        title: "ACME HealthID Provider",
        subtitle: "App Authentication",
        logo_uri: "mdi-rocket-launch-outline",
        authorization_endpoint:
          "https://id.acme.spilikin.dev/auth/realms/healthid/protocol/openid-connect/auth",
      },
      {
        id: "<globex>",
        title: "Globex National",
        subtitle: "Comming soon",
        logo_uri: "mdi-atom",
        authorization_endpoint: "",
      },
      {
        id: "<hooly>",
        title: "hooli MobileID",
        subtitle: "Comming soon",
        logo_uri: "mdi-hat-fedora",
        authorization_endpoint: "",
      },
    ];
  }
}
