import Vue from "vue";
import App from "./App.vue";
import vuetify from "./plugins/vuetify";
import router from "./router";

Vue.config.productionTip = false;

if (localStorage.getItem("id_token") == null) {
  location.href = "/login/";
} else {
  new Vue({
    vuetify,
    router,
    render: (h) => h(App),
  }).$mount("#app");
}
