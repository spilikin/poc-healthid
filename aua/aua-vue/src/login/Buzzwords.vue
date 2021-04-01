<template>
<div>
    <v-card> 
        <v-card-title>
        <v-divider></v-divider>
        </v-card-title>
        <v-card-text>
        <v-layout>
            <v-row>
            <v-col>
                <v-img
                class="icon"
                :src="require('../assets/health_and_safety-white-18dp.svg')"
                ></v-img>
                <div class="buzzword">
                Verified Identities für Healthcare Applications
                </div>
            </v-col>
            <v-col>
                <v-img
                class="icon"
                :src="require('../assets/developer_board-white-18dp.svg')"
                ></v-img>
                <div class="buzzword">
                Public Sector Confidential Computing
                </div>
            </v-col>
            </v-row>
        </v-layout>
        </v-card-text>
    </v-card>
    <v-card> 
        <v-card-title>
        <v-divider></v-divider>
        </v-card-title>
        <v-card-text>
        <v-layout>
            <v-row>
            <v-col>
                <v-img 
                class="icon"
                :src="require('../assets/local_fire_department-white-18dp.svg')"
                ></v-img>
                <div class="buzzword">
                Healthcare Interoperability Resources API
                </div>
            </v-col>
            <v-col>
                <v-img
                class="icon"
                style="opacity: 40%; float: left; margin-right: 5px;"
                :src="require('../assets/all_inclusive-white-18dp.svg')"
                ></v-img>
                <div class="buzzword">
                Brilliantly begetting benign buzzwords.
                </div>
            </v-col>
            </v-row>
        </v-layout>
        </v-card-text>
    </v-card>
</div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'

@Component
export default class Buzzwords extends Vue {
}
</script>

<style scoped>
.icon {
    opacity: 40%; 
    float: left;
    width: 34px;
}
.buzzword {
    margin-left: 40px;
    font-size: 85%;
}
</style>