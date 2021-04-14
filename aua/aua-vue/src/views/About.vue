<template>
  <div class="about">
    <h1>Technical information</h1>
    <h2>Access Token</h2>
    <vue-json-pretty :path="'res'" :data="jwt"> </vue-json-pretty>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop } from "vue-property-decorator";
import "vue-json-pretty/lib/styles.css";
import VueJsonPretty from "vue-json-pretty";

@Component({
  components: { VueJsonPretty },
})
export default class Home extends Vue {
  jwt!: Record<string, unknown>;

  created() {
    console.log("created");
    const token = localStorage.getItem("accessToken");
    this.jwt = JSON.parse(atob(token!.split(".")[1]));
  }
}
</script>

<style scoped>
.vjs-tree,
.is-root {
  text-align: left;
  margin-left: 25px;
}
</style>
