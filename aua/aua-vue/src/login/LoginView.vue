<template>
    <v-app>
    <v-main>
      <v-container fluid fill-height>
        <v-layout align-center justify-center>
          <v-flex xs12 sm8 md4>
            <v-card>
              <v-card-text>
                <v-img
                  style="margin: auto;"
                  width="200"
                  :src="require('../assets/logo.png')"
                ></v-img>
                <v-img
                  style="margin: auto;"
                  width="200"
                  :src="require('../assets/aua.png')"
                ></v-img>
              </v-card-text>

            </v-card>
            <v-card class="px-3 pb-6">
              <v-card-text>
                <v-select
                  x-large
                  :items="identityProviderList"
                  item-text="title"
                  item-value="id"
                  style="font-size: 150%;"
                  v-model="identityProviderId"
                >
                </v-select>
              </v-card-text>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="primary" x-large @click="onLogin()">
                <v-img
                  width="30"
                  :src="require('../assets/health_and_safety-white-18dp.svg')"
                ></v-img>
                  Login with HealthID
                <span class="separator"> </span>
                <v-icon
                  medium
                  id="LoginSettings"
                >
                mdi-cog
                </v-icon>
                    </v-btn>
                <v-spacer></v-spacer>
              </v-card-actions>
            </v-card>
            <Buzzwords/>
          </v-flex> 
        </v-layout>
      </v-container>
    </v-main>
  </v-app>
</template>

<script lang="ts">
import { Component, Vue, Prop } from 'vue-property-decorator'
import Buzzwords from './Buzzwords.vue'

interface IdentityProvider {
  id: string;
  title: string;
  logo: string;
  authorization_endpoint: string;
}

@Component({
  components: {
    Buzzwords
  }
})
export default class LoginView extends Vue {

  set identityProviderId(id: string | null) {
    localStorage.setItem("identityProviderId", id)
  }

  get identityProviderId(): string | null {
    return localStorage.getItem("identityProviderId")
  }

  mounted() {
    if (window.location.search) {
      const tokenEndpoint = "https://id.acme.spilikin.dev/auth/realms/healthid/protocol/openid-connect/token"
      var args = new URLSearchParams(window.location.search);
      var code = args.get("code");
      if (code) {
        var xhr = new XMLHttpRequest();

        xhr.onload = function() {
            var response = xhr.response;
            var message;

            if (xhr.status == 200) {
                message = "Access Token: " + response.access_token;
                localStorage.setItem("accessToken", response.access_token)
                location.href = "/"
            }
            else {
                alert("Error: " + response.error_description + " (" + response.error + ")");
                location.href = "/login/"
            }

        };
        xhr.responseType = 'json';
        xhr.open("POST", tokenEndpoint, true);
        xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
        xhr.send(new URLSearchParams({
            client_id: "aua.spilikin.dev",
            code_verifier: ""+window.sessionStorage.getItem("code_verifier"),
            grant_type: "authorization_code",
            redirect_uri: location.href.replace(location.search, ''),
            code: code
        }));
      }
    }

  }

  get identityProviderList(): Array<IdentityProvider> {
    return [
      {
        id: "id.acme.spilikin.dev",
        title: "ACME HealthID Provider",
        logo: "",
        authorization_endpoint: "https://id.acme.spilikin.dev/auth/realms/healthid/protocol/openid-connect/auth"
      }
    ]  
  }

  onLogin() {
    let identityProvider = this.identityProviderList.find(provider => provider.id == this.identityProviderId)
    if (identityProvider == null) {
      alert("Select your HealthID Provider")
      return
    }

    const authorizeEndpoint = identityProvider.authorization_endpoint
    const redirectURI = window.location.protocol + "//" + window.location.host + "/login"

    const codeVerifier = this.generateRandomString(64);
    this.generateCodeChallenge(codeVerifier).then(function(codeChallenge) {
      window.sessionStorage.setItem("code_verifier", codeVerifier);

      let args = new URLSearchParams({
          response_type: "code",
          client_id: encodeURI("aua.spilikin.dev"),
          code_challenge_method: "S256",
          code_challenge: codeChallenge,
          redirect_uri: redirectURI
      });
      window.location.href = authorizeEndpoint + "?" + args;
    });
  }

  /** See https://github.com/curityio/pkce-javascript-example */
  generateRandomString(length: number) {
    let text = "";
    const possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    for (var i = 0; i < length; i++) {
      text += possible.charAt(Math.floor(Math.random() * possible.length));
    }

    return text;
  }

  async generateCodeChallenge(codeVerifier: string) {
    var digest = await crypto.subtle.digest("SHA-256",
      new TextEncoder().encode(codeVerifier));

      return btoa(String.fromCharCode(...new Uint8Array(digest)))
        .replace(/=/g, '').replace(/\+/g, '-').replace(/\//g, '_')
  }
}
</script>

<style scoped>
#LoginSettings {
  transition: transform .3s ease-in-out;
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