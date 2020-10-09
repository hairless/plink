<template>
  <Header>
    <Menu mode="horizontal" theme="dark" active-name="1">
      <!--<span class="layout-logo">
        哈哈
      </span>-->
      <span style="color: #FFFFFF; font-size: 24px;">Plink</span>
      <div class="layout-nav">
        <!--<MenuItem name="1">
          <Icon type="ios-navigate"></Icon>
          作业管理
        </MenuItem>-->
      </div>
    </Menu>
  </Header>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";

@Component
export default class LHeader extends Vue {}
</script>

<style scoped lang="scss">
.layout-logo {
  width: 100px;
  height: 30px;
  background: #5b6270;
  border-radius: 3px;
  float: left;
  position: relative;
  top: 15px;
  left: 20px;
}
.layout-nav {
  width: 420px;
  margin: 0 0 0 120px;
  margin-right: 20px;
}
</style>
