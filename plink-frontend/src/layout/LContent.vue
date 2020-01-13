<template>
  <Content :style="{ padding: '12px', minHeight: '768px', background: '#fff' }">
    <router-view />
  </Content>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";

@Component
export default class LContent extends Vue {}
</script>

