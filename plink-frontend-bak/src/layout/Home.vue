<template>
  <div id="app">
    <div class="layout">
      <Layout>
        <LHeader />
        <Layout>
          <Sider hide-trigger :style="{ background: '#fff' }">
            <LSideMenu />
          </Sider>
          <Layout :style="{ padding: '0 12px 12px' }">
            <LBreadcrumb />
            <LContent />
          </Layout>
        </Layout>
      </Layout>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import LHeader from "@/layout/LHeader.vue";
import LSideMenu from "@/layout/LSideMenu.vue";
import LBreadcrumb from "@/layout/LBreadcrumb.vue";
import LContent from "@/layout/LContent.vue";

@Component({
  components: {
    LHeader,
    LSideMenu,
    LBreadcrumb,
    LContent
  }
})
export default class Home extends Vue {
}
</script>

<style lang="scss">
.layout {
  border: 1px solid #d7dde4;
  background: #f5f7f9;
  position: relative;
  border-radius: 4px;
  overflow: hidden;
}
</style>
