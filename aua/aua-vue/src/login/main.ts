import Vue from "vue";
import App from "./Main.vue";
import vuetify from "../plugins/vuetify";

import VueRouter, { RouteConfig } from "vue-router";
import LoginView from "./LoginView.vue";

Vue.use(VueRouter);

const routes: Array<RouteConfig> = [
  {
    path: "/login",
    name: "LoginView",
    component: LoginView,
  },
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

Vue.config.productionTip = false;

new Vue({
  vuetify,
  router,
  render: (h) => h(App),
}).$mount("#app");
