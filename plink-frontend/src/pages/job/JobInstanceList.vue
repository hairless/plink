<template>
  <div>
    <div v-if="type === 1">
      <JobInstanceListCustom />
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import JobInstanceListCustom from "@/components/job/JobInstanceListCustom.vue";

@Component({
  components: {
    JobInstanceListCustom
  }
})
export default class JobInstanceList extends Vue {
  type: number = 1;
}
</script>

<style scoped lang="scss"></style>
