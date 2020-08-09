<!-- 函数式组件，无状态 -->
<template functional>
  <a-sub-menu :key="props.subMenuArr.id">
    <!-- icon -->
    <template v-show="props.subMenuArr.meta.icon">
      <span slot="title">
        <a-icon :type="props.subMenuArr.meta.icon" v-if="props.subMenuArr.meta.icon" />
        <span>{{ props.subMenuArr.meta.title }}</span>
      </span>
    </template>

    <!-- 循环处理所有子菜单  -->
    <template v-for="item in props.subMenuArr.children">
      <!-- 有子菜单 -->
      <template v-if="item.children">
        <sub-menu :key="item.id" :subMenuArr="item"></sub-menu>
      </template>
      <!-- 无子菜单 -->
      <template v-else>
        <a-menu-item :key="item.id">
          <template>
            <a-icon :type="item.meta.icon" v-if="item.meta.icon" />
          </template>
          <span>{{ item.meta.title }}</span>
        </a-menu-item>
      </template>
    </template>
  </a-sub-menu>
</template>

<script>
export default {
  name: "SubMenu",
  components: {},
  props: {
    subMenuArr: {}
  }
};
</script>

<style scoped></style>
