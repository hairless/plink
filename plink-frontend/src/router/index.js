import Vue from "vue";
import VueRouter from "vue-router";
import BasicLayout from "@/layout/BasicLayout";

const originalPush = VueRouter.prototype.push;
VueRouter.prototype.push = function(location) {
  return originalPush.call(this, location).catch(err => err);
};

Vue.use(VueRouter);

export const constantRoutes = [
  {
    path: "/",
    name: "Home",
    redirect: "/job/jobList"
  },
  {
    path: "/about",
    name: "About",
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ "../views/About.vue")
  },
  {
    path: "/login",
    name: "Login",
    component: () => import("@/views/user/login/"),
    meta: {
      title: "登录",
      icon: "dashboard",
      roles: ["admin"],
      hidden: true
    }
  },
  /* 用户 */
  {
    path: "/user",
    name: "User",
    redirect: "/user/profile",
    component: BasicLayout,
    meta: {
      title: "用户管理",
      icon: "dashboard",
      roles: ["admin"],
      hidden: true
    },
    children: [
      {
        path: "profile",
        name: "UserProfile",
        component: () => import("@/views/user/profile"),
        meta: {
          title: "用户配置",
          icon: "dashboard",
          roles: ["admin"],
          hidden: true
        }
      }
    ]
  }
];

export const asyncRoutes = [];

const createRouter = () =>
  new VueRouter({
    mode: "history", // require service support
    base: process.env.BASE_URL,
    routes: constantRoutes
  });

const router = createRouter();

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter();
  router.matcher = newRouter.matcher; // reset router
}

export default router;
