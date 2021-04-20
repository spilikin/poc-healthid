<template>
  <v-app>
    <v-main>
      <v-container fluid fill-height>
        <v-layout justify-center>
          <v-flex xs12 sm8 md4>
            <v-card>
              <v-card-text>
                <v-img
                  style="margin: auto"
                  width="170"
                  :src="require('../assets/logo.png')"
                ></v-img>
                <v-img
                  style="margin: auto"
                  width="170"
                  :src="require('../assets/aua.png')"
                ></v-img>
              </v-card-text>
            </v-card>
            <router-view />
          </v-flex>
        </v-layout>
      </v-container>
    </v-main>
    <v-footer>
      <v-spacer/>
      <div>Icons made by <a href="https://www.flaticon.com/authors/icon-pond" title="Icon Pond">Icon Pond</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a></div>      
    </v-footer>
  </v-app>
</template>

<style>
h1 {
  text-align: center;
}
</style>
