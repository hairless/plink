import Vue from "vue";
import VueRouter from "vue-router";
import rt from "./routes";

Vue.use(VueRouter);

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes: rt.routes
});

export default router;
