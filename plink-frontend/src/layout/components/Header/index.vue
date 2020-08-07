<template>
  <div>
    <a-layout-header class="header" style="background-image: linear-gradient(100deg,#3cd5ff,#007aff);">
      <!-- Logo -->
      <Logo></Logo>

      <!-- HeaderMenu -->
      <HeaderMenu />

      <!-- 右侧 -->
      <HeaderRight />
    </a-layout-header>
  </div>
</template>
<script>
import Logo from "@/layout/components/Logo";
import HeaderMenu from "@/layout/components/HeaderMenu";
import HeaderRight from "@/layout/components/HeaderRight";
export default {
  components: {
    Logo,
    HeaderMenu,
    HeaderRight
  }
};
</script>
