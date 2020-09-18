import { login, logout, getUserInfo, getUserRouteList } from "@/api/user";
import { getToken, setToken, removeToken } from "@/utils/auth";
import { resetRouter } from "@/router";
import { Notice } from "view-design";

const state = {
  token: getToken(),
  userName: "",
  nickName: "",
  routeList: null,
  moduleId: null,
  topMenuList: [],
  leftMenuList: []
};

const mutations = {
  SET_TOKEN: (state, token) => {
    state.token = token;
  },
  SET_USER_NAME: (state, userName) => {
    state.userName = userName;
  },
  SET_NICK_NAME: (state, nickName) => {
    state.nickName = nickName;
  },
  SET_ROUTE_LIST: (state, routeList) => {
    state.routeList = routeList;
  },
  SET_MODULE_ID: (state, moduleId) => {
    state.moduleId = moduleId;
  },
  SET_TOP_MENU_LIST: (state, topMenuList) => {
    state.topMenuList = topMenuList;
  },
  SET_LEFT_MENU_LIST: (state, leftMenuList) => {
    state.leftMenuList = leftMenuList;
  }
};

const actions = {
  // user login
  login({ commit }, userInfo) {
    const { username, password } = userInfo;
    return new Promise((resolve, reject) => {
      login({ username: username, password: password })
        .then(resp => {
          const { data } = resp;
          commit("SET_TOKEN", data["x-auth-token"]);
          setToken(data["x-auth-token"]);
          resolve();
        })
        .catch(error => {
          reject(error);
        });
    });
  },

  // get user info
  getUserInfo({ commit }) {
    return new Promise((resolve, reject) => {
      getUserInfo()
        .then(resp => {
          const { data } = resp;

          if (!data) {
            reject("Get user info failed, please Login again.");
          }

          const { userName, nickName } = data;

          commit("SET_USER_NAME", userName);
          commit("SET_NICK_NAME", nickName);
          resolve(data);
        })
        .catch(error => {
          Notice.error({
            title: error || "Get User Info Error !"
          });
          reject(error);
        });
    });
  },

  // get user menu
  getUserRouteList({ commit }) {
    return new Promise((resolve, reject) => {
      getUserRouteList()
        .then(resp => {
          let routeList = resp.data;

          if (!routeList) {
            reject("Verification failed, please Login again.");
          }

          commit("SET_ROUTE_LIST", routeList);

          resolve(routeList);
        })
        .catch(error => {
          Notice.error({
            title: error || "Get User Route List Error !"
          });
          reject(error);
        });
    });
  },

  // user logout
  logout({ commit, state }) {
    return new Promise((resolve, reject) => {
      logout(state.token)
        .then(() => {
          commit("SET_TOKEN", "");
          commit("SET_ROUTE_LIST", null);
          removeToken();
          resetRouter();
          resolve();
        })
        .catch(error => {
          Notice.error({
            title: error || "Logout Error !"
          });
          reject(error);
        });
    });
  },

  // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      commit("SET_TOKEN", "");
      commit("SET_ROUTE_LIST", null);
      removeToken();
      resolve();
    });
  }
};

export default {
  namespaced: true,
  state,
  mutations,
  actions
};
