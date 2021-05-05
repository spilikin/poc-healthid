<template>
  <v-app>
    <v-app-bar app>
      <v-img
        class="mx-2"
        :src="require('./assets/pain.svg')"
        max-height="50"
        max-width="50"
        contain
      ></v-img>
      <v-toolbar-title>
        <v-img
          class="mx-2"
          :src="require('./assets/aua.png')"
          max-height="60"
          max-width="120"
          contain
        ></v-img>
      </v-toolbar-title>
      <v-spacer />
      <v-tooltip bottom>
        <template v-slot:activator="{ on, attrs }">
          <v-btn icon color="red" dark v-bind="attrs" v-on="on" to="/" x-large>
            <v-icon>mdi-bullseye</v-icon>
          </v-btn>
        </template>
        <span>Record pain episode</span>
      </v-tooltip>

      <v-tooltip bottom>
        <template v-slot:activator="{ on, attrs }">
          <v-btn icon color="primary" dark v-bind="attrs" v-on="on">
            <v-icon>mdi-chart-areaspline</v-icon>
          </v-btn>
        </template>
        <span>History</span>
      </v-tooltip>


      <v-tooltip bottom>
        <template v-slot:activator="{ on, attrs }">
          <v-btn icon color="primary" dark v-bind="attrs" v-on="on" to="/about">
            <v-icon>mdi-cog</v-icon>
          </v-btn>
        </template>
        <span>Under the hood</span>
      </v-tooltip>

      <v-tooltip bottom>
        <template v-slot:activator="{ on, attrs }">
          <v-btn
            icon
            color="primary"
            dark
            v-bind="attrs"
            v-on="on"
            @click="logout()"
          >
            <v-icon>mdi-logout</v-icon>
          </v-btn>
        </template>
        <span>Logout</span>
      </v-tooltip>
    </v-app-bar>

    <v-main>
      <v-container fluid>
        <router-view></router-view>
      </v-container>
    </v-main>

    <v-footer app>
      <div class="buzzwords">
        <div class="buzzword">
          <v-img
            class="icon"
            :src="require('./assets/health_and_safety-white-18dp.svg')"
          ></v-img>
        </div>

        <div class="buzzword">
          <v-img
            class="icon"
            :src="require('./assets/developer_board-white-18dp.svg')"
          ></v-img>
        </div>

        <div class="buzzword">
          <v-img
            class="icon"
            :src="require('./assets/local_fire_department-white-18dp.svg')"
          ></v-img>
        </div>

        <div class="buzzword">
          <v-img
            class="icon"
            :src="require('./assets/all_inclusive-white-18dp.svg')"
          ></v-img>
        </div>
      </div>
    </v-footer>
  </v-app>
</template>

<style></style>

<script lang="ts">
import { Component, Vue, Prop } from "vue-property-decorator";

import { requestOpenIDConfiguration } from "./login/oidc"

@Component({
  components: {},
})
export default class AppView extends Vue {
  logout() {
    const accessToken = localStorage.getItem("access_token")!;
    const accessTokenData = JSON.parse(atob(accessToken!.split(".")[1]));
    var isIOS = /iPad|iPhone|iPod/.test(navigator.userAgent) && !window.MSStream;
    if (isIOS) {
      redirect_uri: window.location.protocol + "//" + window.location.host + "/login/" ,
    } else {   
      requestOpenIDConfiguration(accessTokenData["iss"]).then( config => {
        const args = new URLSearchParams({
          redirect_uri: window.location.protocol + "//" + window.location.host + "/login/" ,
        });
        window.location.href = config["end_session_endpoint"] + "?" + args;
      }).catch( error => alert(error))
    }
  }
}
</script>

<style scoped>
.menu {
  margin: 10px;
}
.icon {
  float: left;
  width: 34px;
}
.buzzwords {
  opacity: 0.4;
  width: 100%;
  text-align: center;
}
.buzzword {
  margin-right: 12px;
  display: inline-block;
}
</style>
