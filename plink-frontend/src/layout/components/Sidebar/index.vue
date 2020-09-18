<template>
  <a-layout-sider v-model="collapsed" width="180" style="background: #fff" collapsible>
    <SideMenu/>
  </a-layout-sider>
</template>
<script>
import SideMenu from "@/layout/components/SideMenu";
export default {
  data() {
    return {
      collapsed: false
    };
  },
  components: {
    SideMenu
  }
};
</script>
