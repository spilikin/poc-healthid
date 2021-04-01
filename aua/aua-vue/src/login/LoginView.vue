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
            <v-card class="elevation-12">
              
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="primary" x-large>
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
import { Component, Vue } from 'vue-property-decorator'
import Buzzwords from '../components/Buzzwords.vue'

@Component({
  components: {
    Buzzwords
  }
})
export default class LoginView extends Vue {
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