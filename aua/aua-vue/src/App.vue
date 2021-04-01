<template>
  <v-app>
    <v-app-bar app>
       <router-link to="/">Home</router-link> |
       <router-link to="/about">About</router-link> |
       <a href="/login/">Logout</a>
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