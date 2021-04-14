<template>
  <div>
    <v-card class="px-3 pb-6">
      <v-card-text>
        <v-alert
          v-if="errorMessage != ''"
          border="bottom"
          type="error"
          elevation="2"
        >
          <v-row align="center">
            <v-col class="grow">
              {{ errorMessage }}
            </v-col>
            <v-col class="shrink">
              <v-btn @click="tryAgain">Try again</v-btn>
            </v-col>
          </v-row>
        </v-alert>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-progress-circular
          v-if="errorMessage == ''"
          :size="70"
          :width="7"
          color="primary"
          indeterminate
        ></v-progress-circular>
        <v-spacer></v-spacer>
      </v-card-actions>
    </v-card>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Prop } from "vue-property-decorator";
import { retrieveToken } from "./oidc";

@Component({
  components: {},
})
export default class CallbackView extends Vue {
  private errorMessage = "";

  created() {
    console.log("Callback created");

    const clientId = "aua.spilikin.dev";
    const tokenEndpoint =
      "https://id.acme.spilikin.dev/auth/realms/healthid/protocol/openid-connect/token";

    retrieveToken(clientId, tokenEndpoint, new URLSearchParams(location.search))
      .then((token) => {
        this.errorMessage = "";
        location.href = "/";
      })
      .catch((error) => {
        const err = error as Error;
        this.errorMessage = err.message;
      });
  }

  tryAgain() {
    this.errorMessage = "";
    this.$router.replace("/login/auth");
  }

  mounted() {
    console.log("Callback mounted");
  }
}
</script>

<style scoped></style>
