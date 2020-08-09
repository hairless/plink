<template>
  <a-menu mode="inline" :default-open-keys="openKeys" :default-selected-keys="selectedKeys" :style="{ height: '100%', borderRight: 0 }" @click="onClickMenuItem">
    <template v-for="item in menuList">
      <!-- 有子菜单 -->
      <sub-menu v-if="item.children" :key="item.id" :sub-menu-arr="item" />

      <!-- 无子菜单 -->
      <a-menu-item v-else :key="item.id">
        <a-icon :type="item.meta.icon" v-if="item.meta.icon" />
        <span>{{ item.meta.title }}</span>
      </a-menu-item>
    </template>
  </a-menu>
</template>
<script>
import SubMenu from "@/layout/components/SubMenu";
export default {
  components: { SubMenu },
  data() {
    return {
      collapsed: false,
      matchedRouteList: [],
      menuList: [],
      openKeys: [],
      selectedKeys: [],
      routeMap: {}
    };
  },
  methods: {
    generateMenuList() {
      let tmpMenuList = this.$store.state.user.leftMenuList;
      let newMenuList = [];
      this.transform(tmpMenuList, newMenuList);
      this.menuList = newMenuList;
    },
    generateMenuSelected() {
      // selectedKeys
      this.matchedRouteList = this.$route.matched;
      let leafRoute = this.matchedRouteList[this.matchedRouteList.length - 1];
      if (leafRoute) {
        let leafRouteId = leafRoute.meta.id;
        if (leafRouteId) {
          this.selectedKeys.splice(0);
          this.selectedKeys.push(leafRouteId);
        }
      }

      // openKeys
      for (let i = 0; i < this.matchedRouteList.length - 1; i++) {
        let currRoute = this.matchedRouteList[i];
        let currRouteId = currRoute.meta.id;
        if (currRouteId) {
          this.openKeys.splice(0);
          this.openKeys.push(currRouteId);
        }
      }
    },
    onClickMenuItem(menuItem) {
      let routeId = menuItem.key;
      this.selectedKeys = [routeId];
      let route = this.routeMap[routeId];
      if (route) {
        let routeUrl = route.meta.url !== "" ? route.meta.url : route.redirect;
        this.$router.push({ path: routeUrl });
      }
    },
    transform(routeList, newRouteList) {
      for (let i = 0; i < routeList.length; i++) {
        let currRoute = routeList[i];

        if (!currRoute.children) {
          // 叶子节点
          if (currRoute.meta.showOnLeft !== 0) {
            // 显示在左侧
            newRouteList.push(currRoute);
          }
          this.routeMap[currRoute.id] = currRoute;
        } else {
          // 非叶子节点
          if (currRoute.meta.showOnLeft !== 0) {
            // 显示在左侧
            let newRoute = JSON.parse(JSON.stringify(currRoute));
            newRoute.children = [];
            newRouteList.push(newRoute);
            this.transform(currRoute.children, newRoute.children);
          } else {
            this.transform(currRoute.children, newRouteList);
          }
        }
      }
    }
  },
  watch: {
    "$store.state.user.leftMenuList": function() {
      this.generateMenuList();
      this.generateMenuSelected();
    }
  },
  created() {
    this.generateMenuList();
    this.generateMenuSelected();
  }
};
</script>
