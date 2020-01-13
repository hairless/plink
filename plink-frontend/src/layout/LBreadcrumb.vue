<template>
  <Breadcrumb :style="{ margin: '12px 0' }">
    <BreadcrumbItem>Plink</BreadcrumbItem>
    <BreadcrumbItem>作业管理</BreadcrumbItem>
    <BreadcrumbItem>作业列表</BreadcrumbItem>
  </Breadcrumb>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";

@Component
export default class LBreadcrumb extends Vue {}
</script>

<style scoped lang="scss"></style>
