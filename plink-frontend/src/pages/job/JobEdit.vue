<template>
  <div>
    <div v-if="type === 1">
      <JobEditCustom />
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import JobEditCustom from "@/components/job/JobEditCustom.vue";

@Component({
  components: {
    JobEditCustom
  }
})
export default class JobEdit extends Vue {
  type: number = 1;
}
</script>

<style scoped lang="scss"></style>
