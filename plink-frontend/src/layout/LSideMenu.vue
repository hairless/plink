<template>
  <Menu active-name="1-2" theme="light" width="auto" :open-names="['1']">
    <MenuItem name="1">
      <Icon type="ios-navigate"></Icon>
      平台概览
    </MenuItem>
    <Submenu name="2">
      <template slot="title">
        <Icon type="ios-navigate"></Icon>
        作业管理
      </template>
      <MenuItem name="2-1" to="/job/list">作业列表</MenuItem>
      <MenuItem name="2-2" to="/job/edit">作业编辑</MenuItem>
      <MenuItem name="2-3" to="/job/detail">作业详情</MenuItem>
      <MenuItem name="2-4" to="/job/instanceList">实例列表</MenuItem>
      <!--<MenuItem name="2-1">作业概览</MenuItem>
      <MenuItem name="2-2">新建作业</MenuItem>
      <MenuItem name="2-4">历史作业</MenuItem>-->
    </Submenu>
    <!--<Submenu name="3">
      <template slot="title">
        <Icon type="ios-keypad"></Icon>
        平台监控
      </template>
      <MenuItem name="3-1"><a href="/">监控概览</a></MenuItem>
      <MenuItem name="3-2">JobManager</MenuItem>
      <MenuItem name="3-3">TaskManager</MenuItem>
      <MenuItem name="3-4">API</MenuItem>
    </Submenu>-->
  </Menu>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";

@Component
export default class LSide extends Vue {}
</script>

<style scoped lang="scss"></style>
