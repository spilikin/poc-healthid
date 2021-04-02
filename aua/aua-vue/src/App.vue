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
      <v-spacer/>
      <router-link class="menu" to="/">I have pain</router-link> |
      <router-link class="menu" to="/about">Under the hood</router-link> |
      <v-tooltip bottom>
        <template v-slot:activator="{ on, attrs }">
          <v-btn
            icon
            color="primary"
            dark
            v-bind="attrs"
            v-on="on"
            onclick="window.location.href='/login/'"
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
      ...
    </v-footer>
  </v-app>
</template>

<style>
</style>

<script lang="ts">
import { Component, Vue, Prop } from 'vue-property-decorator'

@Component({
  components: {
  }
})
export default class AppView extends Vue {
}
</script>

<style scoped>
.menu {
  margin: 10px;
}
</style>