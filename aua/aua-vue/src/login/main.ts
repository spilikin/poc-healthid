import Vue from "vue";
import App from "./LoginMain.vue";
import vuetify from "../plugins/vuetify";

import VueRouter, { RouteConfig } from "vue-router";
import ChooserView from "./ChooserView.vue";
import AuthView from "./AuthView.vue";
import CallbackView from "./CallbackView.vue";

if (localStorage.getItem("id_token") != null) {
  localStorage.removeItem("id_token");
}

if (location.pathname == "/login" || location.pathname == "/login/") {
  if (localStorage.getItem("identityProviderId") != null) {
    location.href = "/login/auth";
  }
} else {
  Vue.use(VueRouter);

  const routes: Array<RouteConfig> = [
    {
      path: "/login",
      name: "ChooserView",
      component: ChooserView,
    },
    {
      path: "/login/auth",
      name: "AuthView",
      component: AuthView,
    },
    {
      path: "/login/callback",
      name: "CallbackView",
      component: CallbackView,
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
}
