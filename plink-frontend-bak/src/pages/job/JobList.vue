<template>
  <div>
    <div v-if="type === 1">
      <JobListCustom />
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import JobListCustom from "@/components/job/JobListCustom.vue";

@Component({
  components: {
    JobListCustom
  }
})
export default class JobList extends Vue {
  type: number = 1;
}
</script>

<style scoped lang="scss"></style>
