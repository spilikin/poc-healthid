<template>
  <div class="about">
    <h1>Technical information</h1>
    <h2>ID Token</h2>
    <vue-json-pretty :path="'res'" :data="idTokenData"> </vue-json-pretty>
    <h2>Access Token</h2>
    <vue-json-pretty :path="'res'" :data="accessTokenData"> </vue-json-pretty>
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
  idTokenData!: Record<string, unknown>;
  accessTokenData!: Record<string, unknown>;

  created() {
    console.log("created");
    const idToken = localStorage.getItem("id_token");
    this.idTokenData = JSON.parse(atob(idToken!.split(".")[1]));
    const accessToken = localStorage.getItem("access_token");
    this.accessTokenData = JSON.parse(atob(accessToken!.split(".")[1]));
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
