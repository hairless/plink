<template>
  <span>
    <span style="margin-left: 30px" />
    <span :class="{ 't-header-menu-common': true, 't-header-menu-bottom': item.showBottomStyle }" v-for="item in routeHeaderList" :key="item.id" @click="onClick(item)">{{ item.meta.title }}</span>
  </span>
</template>
<script>
import * as utils from "@/utils/utils";
export default {
  name: "HeaderMenu",
  data() {
    return {
      routeHeaderList: [],
      module: {
        id: -1
      }
    };
  },
  methods: {
    transformRouteList() {
      let routeList = this.$store.getters.routeList;
      let pageRouteList = null;
      routeList = utils.deepCopy(routeList);
      routeList.forEach(item => {
        if (item.path === "/page") {
          pageRouteList = item.children;
        }
      });
      routeList = pageRouteList;
      if (routeList && routeList.length > 0) {
        this.routeHeaderList = routeList.filter(route => {
          route["children"] = null;
          route.showBottomStyle = false;
          if (route.meta && route.meta.showOnTop && route.meta.showOnTop === 1) {
            return true;
          } else {
            return false;
          }
        });
      }
    },
    onClick(item) {
      this.module = item;
      this.$store.commit("user/SET_MODULE_ID", this.module.id);
    },
    updateModuleHighlight() {
      for (let i = 0; i < this.routeHeaderList.length; i++) {
        let currItem = this.routeHeaderList[i];
        if (this.module.id === currItem.id) {
          currItem.showBottomStyle = true;
        } else {
          currItem.showBottomStyle = false;
        }
      }
      // 强制更新
      this.$forceUpdate();
    },
    updateStoreUserLeftMenuList() {
      let routeList = this.$store.getters.routeList;
      let leftMenuList = routeList.filter(route => {
        return route.id === this.module.id;
      });
      this.$store.commit("user/SET_LEFT_MENU_LIST", leftMenuList);
    },
    updateRoute() {
      if (this.module.redirect) {
        this.$router.push({
          path: this.module.redirect
        });
      }
    },
    parseRoute() {
      // 路由路径
      let path = this.$route.path;
      let pathSplits = path.split("/");
      let modulePath = "";
      if (pathSplits.length > 1) {
        modulePath = "/" + pathSplits[1];
      }

      // module.id
      this.routeHeaderList.forEach(route => {
        if (route.path === modulePath) {
          this.module.id = route.id;
        }
      });

      // 通过路由没配到，默认模块为第 1 个
      if (this.module.id === -1) {
        let route = this.routeHeaderList[0];
        if (route && route.id) {
          this.module.id = route.id;
        }
      }

      this.$store.commit("user/SET_MODULE_ID", this.module.id);
      this.updateModuleHighlight();
    }
  },
  watch: {
    "$store.state.user.moduleId": function() {
      this.updateModuleHighlight();
      this.updateStoreUserLeftMenuList();
      this.updateRoute();
    }
  },
  created() {
    this.transformRouteList();
    this.parseRoute();
  }
};
</script>
<style scoped>
.t-header-menu-common {
  padding: 0 15px;
  font-size: 17px;
  font-weight: bold;
  color: #ecffeb;
  height: 63px;
  display: inline-block;
  cursor: pointer;
}
.t-header-menu-bottom {
  border-bottom: 3px solid #0084ff;
}
</style>
