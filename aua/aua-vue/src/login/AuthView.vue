<template>
  <div>
    <v-card class="px-3 pb-6">
      <v-card-text>
        <v-list-item>
          <v-list-item-avatar color="white" size="50">
            <v-icon color="blue" x-large>{{
              identityProvider.logo_uri
            }}</v-icon>
          </v-list-item-avatar>

          <v-list-item-content>
            <v-list-item-title
              v-html="identityProvider.title"
            ></v-list-item-title>
            <v-list-item-subtitle
              v-html="identityProvider.subtitle"
            ></v-list-item-subtitle>
          </v-list-item-content>

          <v-list-item-action>
            <v-icon color="green darken" large> mdi-check-circle </v-icon>
          </v-list-item-action>
        </v-list-item>

        <p>
          <v-btn text @click="changeIdentityProvider()" color="gray">
            <v-icon> mdi-chevron-left </v-icon>
            Change
          </v-btn>
        </p>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn x-large color="primary" @click="authenticate()" class="px-6">
          <v-img
            width="30"
            :src="require('../assets/health_and_safety-white-18dp.svg')"
          ></v-img>
          Login with HealthID
        </v-btn>
        <v-spacer></v-spacer>
      </v-card-actions>
    </v-card>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { FederationQuery, IdentityProvider } from "./federation";
import { redirectToAuthEndpoint } from "./oidc";

@Component({
  components: {},
})
export default class AuthView extends Vue {
  private identityProvider!: IdentityProvider;

  created() {
    const identityProviderId = localStorage.getItem("identityProviderId");
    const identityProvider = new FederationQuery().identityProviderList.find(
      (provider) => provider.id == identityProviderId
    );
    if (identityProvider != undefined) {
      this.identityProvider = identityProvider;
    } else {
      window.location.href = "/login/";
    }
  }

  mounted() {
    console.log("LoginView mounted");
  }

  changeIdentityProvider() {
    localStorage.removeItem("identityProviderId");
    this.$router.replace("/login/");
  }

  authenticate() {
    const authEndpoint = this.identityProvider.authorization_endpoint;
    const clientId = "aua.spilikin.dev";
    const redirectURI =
      window.location.protocol +
      "//" +
      window.location.host +
      "/login/callback";
    redirectToAuthEndpoint(authEndpoint, clientId, redirectURI);
  }
}
</script>

<style scoped>
#LoginSettings {
  transition: transform 0.3s ease-in-out;
}

#LoginSettings:hover {
  transform: rotate(120deg);
}

.separator {
  padding: 20px;
  margin-right: 10px;
  height: 40px;
  border-right: 1px white solid;
}
</style>
