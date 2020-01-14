export default {
  routes: [
    {
      path: "/",
      redirect: "/job/list"
    },
    {
      path: "/",
      component: () => import("@/layout/Home.vue"),
      meta: { title: "LayoutHome" },
      children: [
        {
          path: "job",
          name: "Job",
          component: () => import("@/pages/job/Job.vue"),
          children: [
            {
              path: "/overview",
              name: "Overview",
              component: () => import("@/pages/Overview.vue"),
              meta: { title: "平台概览", permission: true }
            },
            //  List
            {
              path: "list",
              name: "JobList",
              component: () => import("@/pages/job/JobList.vue"),
              meta: { title: "作业列表", permission: true }
            },
            {
              path: "instanceList",
              name: "JobInstanceList",
              component: () => import("@/pages/job/JobInstanceList.vue"),
              meta: { title: "作业列表", permission: true }
            },
            // Edit
            {
              path: "edit",
              name: "JobEdit",
              component: () => import("@/pages/job/JobEdit.vue"),
              meta: { title: "作业编辑", permission: true }
            }
          ]
        }
      ]
    },
    {
      path: "/login",
      component: () => import("@/pages/login.vue")
    },
    {
      path: "/403",
      name: "403",
      component: () => import("@/pages/403.vue")
    },
    {
      path: "/404",
      name: "404",
      component: () => import("@/pages/404.vue")
    },
    {
      path: "/500",
      name: "500",
      component: () => import("@/pages/500.vue")
    },
    {
      path: "/error",
      name: "error",
      component: () => import("@/pages/error.vue")
    },
    {
      path: "/about",
      name: "About",
      component: () => import("@/views/About.vue")
    },
    {
      path: "*",
      redirect: "/404"
    }
  ]
};
