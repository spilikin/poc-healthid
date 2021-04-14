<template>
  <div class="home">
    <v-img
      style="margin: auto; height: 75vh"
      :src="require('../assets/body-male.svg')"
      contain
      @click="onclick"
    >
      <div
        id="painpoint"
        style="visibility: hidden; position: absolute; left: 0px; top: 0px"
      >
        <v-icon color="red">mdi-bullseye</v-icon>
      </div>
    </v-img>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";

@Component({
  components: {},
})
export default class HomeView extends Vue {
  onclick(event: MouseEvent) {
    const el = document.getElementById("painpoint")!;
    el.style.left = event.offsetX - 15 + "px";
    el.style.top = event.offsetY - 15 + "px";
    el.style.visibility = "visible";
  }
}
</script>

<style></style>
