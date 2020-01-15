<template>
  <div>
    <div v-if="type === 1">
      <JobDetailCustom />
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import JobDetailCustom from "@/components/job/JobDetailCustom.vue";

@Component({
  components: {
    JobDetailCustom
  }
})
export default class JobDetail extends Vue {
  type: number = 1;
}
</script>

<style scoped lang="scss"></style>
