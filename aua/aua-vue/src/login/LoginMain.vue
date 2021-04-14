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
  </v-app>
</template>

<style>
h1 {
  text-align: center;
}
</style>
