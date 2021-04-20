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
        logo_uri: "/acme-logo.png",
        authorization_endpoint:
          "https://id.acme.spilikin.dev/auth/realms/healthid/protocol/openid-connect/auth",
      },
      {
        id: "<globex>",
        title: "Globex National",
        subtitle: "Comming soon",
        logo_uri: "/pharmacy.png",
        authorization_endpoint: "",
      },
      {
        id: "<hooly>",
        title: "hooli MobileID",
        subtitle: "Comming soon",
        logo_uri: "/smartphone.png",
        authorization_endpoint: "",
      },
      {
        id: "<initech>",
        title: "Initech eID",
        subtitle: "Comming soon",
        logo_uri: "/credit-card.png",
        authorization_endpoint: "",
      },
    ];
  }
}
