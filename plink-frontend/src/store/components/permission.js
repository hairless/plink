import { constantRoutes } from "@/router";

const state = {
  routeList: null,
  addRouteList: []
};

/**
 * Filter asynchronous routing tables by recursion
 * @param routes asyncRoutes
 */
export function filterAsyncRouteList(routes) {
  const accessedRouters = routes.filter(router => {
    // 有 component
    if (router.component) {
      const component = router.component;
      router.component = loadComponent(component);
    }

    // meta 中加入 id
    if (router.id) {
      if (!router.meta) {
        router.meta = {};
      }
      router.meta.id = router.id;
    }

    // 递归处理子节点
    if (router.children && router.children.length) {
      router.children = filterAsyncRouteList(router.children);
    }
    return true;
  });
  return accessedRouters;
}

const mutations = {
  SET_ROUTE_LIST: (state, routes) => {
    state.addRouteList = routes;
    state.routeList = constantRoutes.concat(routes);
  }
};

const actions = {
  /**
   * 根据路由列表转换为组件对象
   *
   * @param commit
   * @param routeList
   * @returns {Promise<unknown>}
   */
  generateRoutes({ commit }, routeList) {
    return new Promise(resolve => {
      let accessedRoutes = filterAsyncRouteList(routeList);
      commit("SET_ROUTE_LIST", accessedRoutes);
      resolve(accessedRoutes);
    });
  }
};

/**
 * 加载组件
 *
 * @param component 组件
 */
export function loadComponent(component) {
  return () => import(`@/${component}`);
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
};
