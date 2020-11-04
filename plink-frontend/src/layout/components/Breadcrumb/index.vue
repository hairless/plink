<template>
  <a-breadcrumb style="margin: 10px 0">
    <a-breadcrumb-item>
      <router-link :to="{ path: '/' }" style="color: cornflowerblue">首页</router-link>
    </a-breadcrumb-item>
    <a-breadcrumb-item v-for="(item, index) in matchedRoutes" :key="item.path">
      <router-link
        :to="{ path: item.meta.url === '' ? item.redirect : item.meta.url }"
        v-if="index !== matchedRoutes.length - 1"
        :style="index !== matchedRoutes.length - 1 ? 'color: cornflowerblue' : ''"
      >
        {{ item.meta.title }}
      </router-link>
      <span v-else>{{ item.meta.title }}</span>
    </a-breadcrumb-item>
  </a-breadcrumb>
</template>
<script>
export default {
  data() {
    return {
      matchedRoutes: []
    };
  },
  watch: {
    $route: function() {
      this.matchedRoutes = this.$route.matched.filter(item => {
        return item.path !== "/page";
      });
    }
  },
  created() {
    this.matchedRoutes = this.$route.matched.filter(item => {
      return item.path !== "/page";
    });
  }
};
</script>
