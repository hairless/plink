<template>
  <a-layout id="components-layout-demo-top-side-2">
    <Header />
    <a-layout>
      <Sidebar />
      <a-layout style="padding: 0 10px 10px">
        <Breadcrumb />
        <a-layout-content
          :style="{
            background: '#ffffff',
            padding: '10px',
            margin: 0
          }"
        >
          <router-view />
        </a-layout-content>
      </a-layout>
    </a-layout>
  </a-layout>
</template>

<script>
import Header from "@/layout/components/Header";
import Sidebar from "@/layout/components/Sidebar";
import Breadcrumb from "@/layout/components/Breadcrumb";

export default {
  name: "BasicLayout",
  data() {
    return {
      collapsed: false,
    };
  },
  components: {
    Header,
    Sidebar,
    Breadcrumb
  }
};
</script>

<style scoped>
#components-layout-demo-top-side-2 {
}
</style>
