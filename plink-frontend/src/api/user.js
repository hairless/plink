// import request from "@/utils/request";

/*export function login(data) {
  return request({
    url: "/api/user/login",
    method: "post",
    data
  });
}

export function logout() {
  return request({
    url: "/api/user/logout",
    method: "get"
  });
}

export function getUserInfo() {
  return request({
    url: "/api/user/getUserInfo",
    method: "get"
  });
}

export function getUserRouteList() {
  return request({
    url: "/api/user/getUserRouteList",
    method: "get"
  });
}*/

export function login() {
  return new Promise(resolve => {
    resolve({
      code: 10001,
      msg: "登录成功",
      data: {
        "x-auth-token": "plink"
      }
    });
  });
}

export function logout() {
  return new Promise(resolve => {
    resolve({
      code: 10001,
      msg: "退出登录"
    });
  });
}

export function getUserInfo() {
  return new Promise(resolve => {
    resolve({
      code: 10001,
      msg: "成功",
      data: {
        userName: "admin",
        nickName: "admin"
      }
    });
  });
}

export function getUserRouteList() {
  return new Promise(resolve => {
    resolve({
      data: [
        {
          id: 1,
          moduleId: 1,
          moduleName: "作业管理",
          parentId: -1,
          name: "Job",
          path: "/job",
          component: "layout/BasicLayout.vue",
          meta: {
            showOnTop: 1,
            showOnLeft: 0,
            icon: "project",
            title: "作业管理",
            url: "/job"
          },
          level: 1,
          sequence: -1,
          remark: "",
          redirect: "/job/jobList",
          children: [
            {
              id: 111,
              moduleId: 1,
              moduleName: "作业管理",
              parentId: 1,
              name: "JobList",
              path: "jobList",
              component: "views/job/list/index.vue",
              meta: {
                showOnTop: 0,
                showOnLeft: 1,
                icon: "project",
                title: "作业列表",
                url: "/job/jobList"
              },
              level: 1,
              sequence: -1,
              remark: ""
            },
            {
              id: 112,
              moduleId: 1,
              moduleName: "作业管理",
              parentId: 1,
              name: "JobAdd",
              path: "jobAdd",
              component: "views/job/add/index.vue",
              meta: {
                showOnTop: 0,
                showOnLeft: 0,
                icon: "project",
                title: "作业添加",
                url: "/job/jobAdd"
              },
              level: 1,
              sequence: -1,
              remark: ""
            },
            {
              id: 113,
              moduleId: 1,
              moduleName: "作业管理",
              parentId: -1,
              name: "JobEdit",
              path: "jobEdit",
              component: "views/job/edit/index.vue",
              meta: {
                showOnTop: 0,
                showOnLeft: 0,
                icon: "project",
                title: "作业编辑",
                url: "/job/jobEdit"
              },
              level: 1,
              sequence: -1,
              remark: ""
            },
            {
              id: 114,
              moduleId: 1,
              moduleName: "作业管理",
              parentId: -1,
              name: "JobDetail",
              path: "jobDetail",
              component: "views/job/detail/index.vue",
              meta: {
                showOnTop: 0,
                showOnLeft: 0,
                icon: "project",
                title: "作业详情",
                url: "/job/jobDetail"
              },
              level: 1,
              sequence: -1,
              remark: ""
            },
            {
              id: 121,
              moduleId: 1,
              moduleName: "作业管理",
              parentId: 1,
              name: "InstList",
              path: "instList",
              component: "views/inst/list/index.vue",
              meta: {
                showOnTop: 0,
                showOnLeft: 1,
                icon: "project",
                title: "实例列表",
                url: "/job/instList"
              },
              level: 1,
              sequence: -1,
              remark: ""
            },
            {
              id: 122,
              moduleId: 1,
              moduleName: "作业管理",
              parentId: -1,
              name: "InstDetail",
              path: "InstDetail",
              component: "layout/BlankLayout.vue",
              meta: {
                showOnTop: 0,
                showOnLeft: 0,
                icon: "project",
                title: "实例详情",
                url: "/job/instList"
              },
              level: 1,
              sequence: -1,
              remark: ""
            }
          ]
        },
        {
          id: 2,
          moduleId: 1,
          moduleName: "Flink SQL",
          parentId: -1,
          name: "SQL",
          path: "/sql",
          component: "layout/BlankLayout.vue",
          meta: {
            showOnTop: 1,
            showOnLeft: 1,
            icon: "project",
            title: "Flink SQL",
            url: "/sql"
          },
          level: 1,
          sequence: -1,
          remark: "",
          redirect: "/job/jobList"
        },
        {
          id: 3,
          moduleId: 1,
          moduleName: "用户管理",
          parentId: -1,
          name: "User",
          path: "/user",
          component: "layout/BlankLayout.vue",
          meta: {
            showOnTop: 1,
            showOnLeft: 1,
            icon: "project",
            title: "用户管理",
            url: "/user"
          },
          level: 1,
          sequence: -1,
          remark: "",
          redirect: "/user/userList"
        }
      ]
    });
  });
}
