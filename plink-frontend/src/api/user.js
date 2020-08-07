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
              component: "layout/BlankLayout.vue",
              meta: {
                showOnTop: 0,
                showOnLeft: 1,
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
              path: "jobList",
              component: "layout/BlankLayout.vue",
              meta: {
                showOnTop: 0,
                showOnLeft: 1,
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
              component: "layout/BlankLayout.vue",
              meta: {
                showOnTop: 0,
                showOnLeft: 1,
                icon: "project",
                title: "作业详情",
                url: "/job/jobList"
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
                showOnLeft: 1,
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

/*
export function getUserRouteList() {
  return new Promise(resolve => {
    resolve({
      data: [
        {
          id: 1,
          moduleId: 1,
          moduleName: "用户管理中心",
          parentId: -1,
          name: "Umc",
          path: "/umc",
          component: "layout/BlankLayout.vue",
          meta: {
            showOnTop: 1,
            showOnLeft: 0,
            icon: "project",
            title: "用户管理中心",
            url: "/umc"
          },
          level: 1,
          sequence: -1,
          remark: "",
          redirect: "/umc/user",
          enable: 1,
          enableLabel: "禁用",
          createTime: "2020-07-09 18:25:16",
          updateTime: "2020-07-09 18:25:16",
          children: [
            {
              id: 2,
              moduleId: 1,
              moduleName: "用户管理中心",
              parentId: 1,
              name: "UmcUser",
              path: "user",
              component: "layout/BasicLayout.vue",
              meta: {
                showOnTop: 0,
                showOnLeft: 1,
                icon: "project",
                title: "用户管理",
                url: "/umc/user"
              },
              level: 1,
              sequence: -1,
              remark: "",
              redirect: "/umc/user/list",
              enable: 1,
              enableLabel: "禁用",
              createTime: "2020-07-09 18:51:00",
              updateTime: "2020-07-09 18:51:00",
              children: [
                {
                  id: 3,
                  moduleId: 1,
                  moduleName: "用户管理中心",
                  parentId: 2,
                  name: "UmcUserList",
                  path: "userList",
                  component: "views/umc/user/list/index.vue",
                  meta: {
                    showOnTop: 0,
                    showOnLeft: 1,
                    icon: "project",
                    title: "用户列表",
                    url: "/umc/user/userList"
                  },
                  level: 1,
                  sequence: -1,
                  remark: "",
                  enable: 1,
                  enableLabel: "禁用",
                  createTime: "2020-07-09 18:51:13",
                  updateTime: "2020-07-09 18:51:13"
                },
                {
                  id: 4,
                  moduleId: 1,
                  moduleName: "用户管理中心",
                  parentId: 2,
                  name: "UmcUserADD",
                  path: "userAdd",
                  component: "views/umc/user/add/index.vue",
                  meta: {
                    showOnTop: 0,
                    showOnLeft: 0,
                    icon: "project",
                    title: "添加用户",
                    url: "/umc/user/userAdd"
                  },
                  level: 3,
                  sequence: -1,
                  remark: "",
                  enable: 1,
                  enableLabel: "禁用",
                  createTime: "2020-07-10 10:04:50",
                  updateTime: "2020-07-10 10:04:50"
                },
                {
                  id: 5,
                  moduleId: 1,
                  moduleName: "用户管理中心",
                  parentId: 2,
                  name: "UmcUserEdit",
                  path: "userEdit",
                  component: "views/umc/user/edit/index.vue",
                  meta: {
                    showOnTop: 0,
                    showOnLeft: 0,
                    icon: "project",
                    title: "编辑用户",
                    url: "/umc/user/userEdit"
                  },
                  level: 3,
                  sequence: -1,
                  remark: "",
                  enable: 1,
                  enableLabel: "禁用",
                  createTime: "2020-07-10 10:05:00",
                  updateTime: "2020-07-10 10:05:00"
                },
                {
                  id: 6,
                  moduleId: 1,
                  moduleName: "用户管理中心",
                  parentId: 2,
                  name: "UmcUserDetail",
                  path: "userDetail",
                  component: "views/umc/user/detail/index.vue",
                  meta: {
                    showOnTop: 0,
                    showOnLeft: 0,
                    icon: "project",
                    title: "用户详情",
                    url: "/umc/user/userDetail"
                  },
                  level: 3,
                  sequence: -1,
                  remark: "",
                  enable: 1,
                  enableLabel: "禁用",
                  createTime: "2020-07-10 10:05:24",
                  updateTime: "2020-07-10 10:05:24"
                },
                {
                  id: 7,
                  moduleId: 1,
                  moduleName: "用户管理中心",
                  parentId: 2,
                  name: "UmcGroupList",
                  path: "groupList",
                  component: "views/umc/group/list/index.vue",
                  meta: {
                    showOnTop: 0,
                    showOnLeft: 1,
                    icon: "project",
                    title: "组列表",
                    url: "/umc/user/groupList"
                  },
                  level: 3,
                  sequence: -1,
                  remark: "",
                  enable: 1,
                  enableLabel: "禁用",
                  createTime: "2020-07-10 10:24:33",
                  updateTime: "2020-07-10 10:24:33"
                },
                {
                  id: 8,
                  moduleId: 1,
                  moduleName: "用户管理中心",
                  parentId: 2,
                  name: "UmcGroupADD",
                  path: "groupAdd",
                  component: "views/umc/group/add/index.vue",
                  meta: {
                    showOnTop: 0,
                    showOnLeft: 0,
                    icon: "project",
                    title: "添加组",
                    url: "/umc/user/groupAdd"
                  },
                  level: 3,
                  sequence: -1,
                  remark: "",
                  enable: 1,
                  enableLabel: "禁用",
                  createTime: "2020-07-10 10:24:33",
                  updateTime: "2020-07-10 10:24:33"
                },
                {
                  id: 9,
                  moduleId: 1,
                  moduleName: "用户管理中心",
                  parentId: 2,
                  name: "UmcGroupEdit",
                  path: "groupEdit",
                  component: "views/umc/group/edit/index.vue",
                  meta: {
                    showOnTop: 0,
                    showOnLeft: 0,
                    icon: "project",
                    title: "编辑组",
                    url: "/umc/user/groupEdit"
                  },
                  level: 3,
                  sequence: -1,
                  remark: "",
                  enable: 1,
                  enableLabel: "禁用",
                  createTime: "2020-07-10 10:24:33",
                  updateTime: "2020-07-10 10:24:33"
                },
                {
                  id: 10,
                  moduleId: 1,
                  moduleName: "用户管理中心",
                  parentId: 2,
                  name: "UmcGroupDetail",
                  path: "groupDetail",
                  component: "views/umc/group/detail/index.vue",
                  meta: {
                    showOnTop: 0,
                    showOnLeft: 0,
                    icon: "project",
                    title: "组详情",
                    url: "/umc/user/groupDetail"
                  },
                  level: 3,
                  sequence: -1,
                  remark: "",
                  enable: 1,
                  enableLabel: "禁用",
                  createTime: "2020-07-10 10:24:33",
                  updateTime: "2020-07-10 10:24:33"
                },
                {
                  id: 11,
                  moduleId: 1,
                  moduleName: "用户管理中心",
                  parentId: 2,
                  name: "UmcRoleList",
                  path: "roleList",
                  component: "views/umc/role/list/index.vue",
                  meta: {
                    showOnTop: 0,
                    showOnLeft: 1,
                    icon: "project",
                    title: "角色列表",
                    url: "/umc/user/roleList"
                  },
                  level: 3,
                  sequence: -1,
                  remark: "",
                  enable: 1,
                  enableLabel: "禁用",
                  createTime: "2020-07-10 10:38:02",
                  updateTime: "2020-07-10 10:38:02"
                },
                {
                  id: 12,
                  moduleId: 1,
                  moduleName: "用户管理中心",
                  parentId: 2,
                  name: "UmcRoleADD",
                  path: "roleAdd",
                  component: "views/umc/role/add/index.vue",
                  meta: {
                    showOnTop: 0,
                    showOnLeft: 0,
                    icon: "project",
                    title: "添加角色",
                    url: "/umc/user/roleAdd"
                  },
                  level: 3,
                  sequence: -1,
                  remark: "",
                  enable: 1,
                  enableLabel: "禁用",
                  createTime: "2020-07-10 10:38:02",
                  updateTime: "2020-07-10 10:38:02"
                },
                {
                  id: 13,
                  moduleId: 1,
                  moduleName: "用户管理中心",
                  parentId: 2,
                  name: "UmcRoleEdit",
                  path: "roleEdit",
                  component: "views/umc/role/edit/index.vue",
                  meta: {
                    showOnTop: 0,
                    showOnLeft: 0,
                    icon: "project",
                    title: "编辑角色",
                    url: "/umc/user/roleEdit"
                  },
                  level: 3,
                  sequence: -1,
                  remark: "",
                  enable: 1,
                  enableLabel: "禁用",
                  createTime: "2020-07-10 10:38:02",
                  updateTime: "2020-07-10 10:38:02"
                },
                {
                  id: 14,
                  moduleId: 1,
                  moduleName: "用户管理中心",
                  parentId: 2,
                  name: "UmcRoleDetail",
                  path: "roleDetail",
                  component: "views/umc/role/detail/index.vue",
                  meta: {
                    showOnTop: 0,
                    showOnLeft: 0,
                    icon: "project",
                    title: "角色详情",
                    url: "/umc/user/roleDetail"
                  },
                  level: 3,
                  sequence: -1,
                  remark: "",
                  enable: 1,
                  enableLabel: "禁用",
                  createTime: "2020-07-10 10:38:02",
                  updateTime: "2020-07-10 10:38:02"
                },
                {
                  id: 15,
                  moduleId: 1,
                  moduleName: "用户管理中心",
                  parentId: 2,
                  name: "UmcPermissionList",
                  path: "permissionList",
                  component: "views/umc/permission/list/index.vue",
                  meta: {
                    showOnTop: 0,
                    showOnLeft: 1,
                    icon: "project",
                    title: "权限列表",
                    url: "/umc/user/permissionList"
                  },
                  level: 3,
                  sequence: -1,
                  remark: "",
                  enable: 1,
                  enableLabel: "禁用",
                  createTime: "2020-07-10 10:46:43",
                  updateTime: "2020-07-10 10:46:43"
                },
                {
                  id: 16,
                  moduleId: 1,
                  moduleName: "用户管理中心",
                  parentId: 2,
                  name: "UmcPermissionADD",
                  path: "permissionAdd",
                  component: "views/umc/permission/add/index.vue",
                  meta: {
                    showOnTop: 0,
                    showOnLeft: 0,
                    icon: "project",
                    title: "添加权限",
                    url: "/umc/user/permissionAdd"
                  },
                  level: 3,
                  sequence: -1,
                  remark: "",
                  enable: 1,
                  enableLabel: "禁用",
                  createTime: "2020-07-10 10:46:43",
                  updateTime: "2020-07-10 10:46:43"
                },
                {
                  id: 17,
                  moduleId: 1,
                  moduleName: "用户管理中心",
                  parentId: 2,
                  name: "UmcPermissionEdit",
                  path: "permissionEdit",
                  component: "views/umc/permission/edit/index.vue",
                  meta: {
                    showOnTop: 0,
                    showOnLeft: 0,
                    icon: "project",
                    title: "编辑权限",
                    url: "/umc/user/permissionEdit"
                  },
                  level: 3,
                  sequence: -1,
                  remark: "",
                  enable: 1,
                  enableLabel: "禁用",
                  createTime: "2020-07-10 10:46:43",
                  updateTime: "2020-07-10 10:46:43"
                },
                {
                  id: 18,
                  moduleId: 1,
                  moduleName: "用户管理中心",
                  parentId: 2,
                  name: "UmcPermissionDetail",
                  path: "permissionDetail",
                  component: "views/umc/permission/detail/index.vue",
                  meta: {
                    showOnTop: 0,
                    showOnLeft: 0,
                    icon: "project",
                    title: "权限详情",
                    url: "/umc/user/permissionDetail"
                  },
                  level: 3,
                  sequence: -1,
                  remark: "",
                  enable: 1,
                  enableLabel: "禁用",
                  createTime: "2020-07-10 10:46:43",
                  updateTime: "2020-07-10 10:46:43"
                },
                {
                  id: 28,
                  moduleId: 1,
                  moduleName: "用户管理中心",
                  parentId: 2,
                  name: "UmcOrganizationList",
                  path: "organizationList",
                  component: "views/umc/organization/list/index.vue",
                  meta: {
                    showOnTop: 0,
                    showOnLeft: 1,
                    icon: "project",
                    title: "组织架构",
                    url: "/umc/user/organizationList"
                  },
                  level: null,
                  sequence: -1,
                  remark: "",
                  enable: 1,
                  enableLabel: "禁用",
                  createTime: "2020-07-10 14:34:03",
                  updateTime: "2020-07-10 14:34:03"
                },
                {
                  id: 29,
                  moduleId: 1,
                  moduleName: "用户管理中心",
                  parentId: 2,
                  name: "UmcOrganizationAdd",
                  path: "organizationAdd",
                  component: "views/umc/organization/add/index.vue",
                  meta: {
                    showOnTop: 0,
                    showOnLeft: 0,
                    icon: "dashboard",
                    title: "添加组织架构",
                    url: "/umc/user/organizationAdd"
                  },
                  level: null,
                  sequence: -1,
                  remark: "",
                  redirect: "",
                  enable: 1,
                  enableLabel: "禁用",
                  createTime: "2020-07-10 14:37:42",
                  updateTime: "2020-07-10 14:37:42"
                },
                {
                  id: 30,
                  moduleId: 1,
                  moduleName: "用户管理中心",
                  parentId: 2,
                  name: "UmcOrganizationEdit",
                  path: "organizationEdit",
                  component: "views/umc/organization/edit/index.vue",
                  meta: {
                    showOnTop: 0,
                    showOnLeft: 0,
                    icon: "project",
                    title: "编辑组织架构",
                    url: "/umc/res/organizationEdit"
                  },
                  level: 3,
                  sequence: -1,
                  remark: "",
                  enable: 1,
                  enableLabel: "禁用",
                  createTime: "2020-07-10 15:20:52",
                  updateTime: "2020-07-10 15:20:52"
                },
                {
                  id: 31,
                  moduleId: 1,
                  moduleName: "用户管理中心",
                  parentId: 2,
                  name: "UmcOrganizationDetail",
                  path: "organizationDetail",
                  component: "views/umc/organization/detail/index.vue",
                  meta: {
                    showOnTop: 0,
                    showOnLeft: 0,
                    icon: "project",
                    title: "组织架构详情",
                    url: "/umc/res/organizationDetail"
                  },
                  level: 3,
                  sequence: -1,
                  remark: "",
                  enable: 1,
                  enableLabel: "禁用",
                  createTime: "2020-07-10 15:20:52",
                  updateTime: "2020-07-10 15:20:52"
                }
              ]
            },
            {
              id: 19,
              moduleId: 1,
              moduleName: "用户管理中心",
              parentId: 1,
              name: "UmcRes",
              path: "res",
              component: "layout/BasicLayout.vue",
              meta: {
                showOnTop: 0,
                showOnLeft: 1,
                icon: "project",
                title: "资源管理",
                url: "/umc/res"
              },
              level: 2,
              sequence: -1,
              remark: "",
              redirect: "/umc/res/list",
              enable: 1,
              enableLabel: "禁用",
              createTime: "2020-07-10 11:22:40",
              updateTime: "2020-07-10 11:22:40",
              children: [
                {
                  id: 20,
                  moduleId: 1,
                  moduleName: "用户管理中心",
                  parentId: 19,
                  name: "UmcModuleList",
                  path: "moduleList",
                  component: "views/umc/module/list/index.vue",
                  meta: {
                    showOnTop: 0,
                    showOnLeft: 1,
                    icon: "project",
                    title: "模块列表",
                    url: "/umc/res/moduleList"
                  },
                  level: 3,
                  sequence: -1,
                  remark: "",
                  enable: 1,
                  enableLabel: "禁用",
                  createTime: "2020-07-10 11:22:54",
                  updateTime: "2020-07-10 11:22:54"
                },
                {
                  id: 21,
                  moduleId: 1,
                  moduleName: "用户管理中心",
                  parentId: 19,
                  name: "UmcModuleADD",
                  path: "moduleAdd",
                  component: "views/umc/module/add/index.vue",
                  meta: {
                    showOnTop: 0,
                    showOnLeft: 0,
                    icon: "project",
                    title: "添加模块",
                    url: "/umc/res/moduleAdd"
                  },
                  level: 3,
                  sequence: -1,
                  remark: "",
                  enable: 1,
                  enableLabel: "禁用",
                  createTime: "2020-07-10 11:31:16",
                  updateTime: "2020-07-10 11:31:16"
                },
                {
                  id: 22,
                  moduleId: 1,
                  moduleName: "用户管理中心",
                  parentId: 19,
                  name: "UmcModuleEdit",
                  path: "moduleEdit",
                  component: "views/umc/module/edit/index.vue",
                  meta: {
                    showOnTop: 0,
                    showOnLeft: 0,
                    icon: "project",
                    title: "编辑模块",
                    url: "/umc/res/moduleEdit"
                  },
                  level: 3,
                  sequence: -1,
                  remark: "",
                  enable: 1,
                  enableLabel: "禁用",
                  createTime: "2020-07-10 11:31:16",
                  updateTime: "2020-07-10 11:31:16"
                },
                {
                  id: 23,
                  moduleId: 1,
                  moduleName: "用户管理中心",
                  parentId: 19,
                  name: "UmcModuleDetail",
                  path: "moduleDetail",
                  component: "views/umc/module/detail/index.vue",
                  meta: {
                    showOnTop: 0,
                    showOnLeft: 0,
                    icon: "project",
                    title: "模块详情",
                    url: "/umc/res/moduleDetail"
                  },
                  level: 3,
                  sequence: -1,
                  remark: "",
                  enable: 1,
                  enableLabel: "禁用",
                  createTime: "2020-07-10 11:31:16",
                  updateTime: "2020-07-10 11:31:16"
                },
                {
                  id: 24,
                  moduleId: 1,
                  moduleName: "用户管理中心",
                  parentId: 19,
                  name: "UmcRouteList",
                  path: "routeList",
                  component: "views/umc/route/list/index.vue",
                  meta: {
                    showOnTop: 0,
                    showOnLeft: 1,
                    icon: "project",
                    title: "路由列表",
                    url: "/umc/res/routeList"
                  },
                  level: 3,
                  sequence: -1,
                  remark: "",
                  enable: 1,
                  enableLabel: "禁用",
                  createTime: "2020-07-10 11:46:14",
                  updateTime: "2020-07-10 11:46:14"
                },
                {
                  id: 25,
                  moduleId: 1,
                  moduleName: "用户管理中心",
                  parentId: 19,
                  name: "UmcRouteADD",
                  path: "routeAdd",
                  component: "views/umc/route/add/index.vue",
                  meta: {
                    showOnTop: 0,
                    showOnLeft: 0,
                    icon: "project",
                    title: "添加路由",
                    url: "/umc/res/routeAdd"
                  },
                  level: 3,
                  sequence: -1,
                  remark: "",
                  enable: 1,
                  enableLabel: "禁用",
                  createTime: "2020-07-10 11:46:14",
                  updateTime: "2020-07-10 11:46:14"
                },
                {
                  id: 26,
                  moduleId: 1,
                  moduleName: "用户管理中心",
                  parentId: 19,
                  name: "UmcRouteEdit",
                  path: "routeEdit",
                  component: "views/umc/route/edit/index.vue",
                  meta: {
                    showOnTop: 0,
                    showOnLeft: 0,
                    icon: "project",
                    title: "编辑路由",
                    url: "/umc/res/routeEdit"
                  },
                  level: 3,
                  sequence: -1,
                  remark: "",
                  enable: 1,
                  enableLabel: "禁用",
                  createTime: "2020-07-10 11:46:14",
                  updateTime: "2020-07-10 11:46:14"
                },
                {
                  id: 27,
                  moduleId: 1,
                  moduleName: "用户管理中心",
                  parentId: 19,
                  name: "UmcRouteDetail",
                  path: "routeDetail",
                  component: "views/umc/route/detail/index.vue",
                  meta: {
                    showOnTop: 0,
                    showOnLeft: 0,
                    icon: "project",
                    title: "路由详情",
                    url: "/umc/res/routeDetail"
                  },
                  level: 3,
                  sequence: -1,
                  remark: "",
                  enable: 1,
                  enableLabel: "禁用",
                  createTime: "2020-07-10 11:46:14",
                  updateTime: "2020-07-10 11:46:14"
                }
              ]
            }
          ]
        }
      ]
    });
  });
}
*/
