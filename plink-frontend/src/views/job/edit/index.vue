<template>
  <div>
    <template v-if="type === 1">
      <JobCustomAddEditDetail :usage-model="'edit'" :data-id="jobId" />
    </template>
    <template v-if="type === 2">
      <JobSqlEdit :usage-model="'edit'" :data-id="jobId" />
    </template>
    <template v-else>
      等待实现中 。。。
    </template>
  </div>
</template>

<script>
import JobCustomAddEditDetail from "@/views/components/JobCustomAddEditDetail";
import JobSqlEdit from "@/views/components/JobSqlEdit";
export default {
  components: {
    JobCustomAddEditDetail,
    JobSqlEdit
  },
  name: "JobEdit",
  data() {
    return {
      jobId: -1,
      type: 2
    };
  },
  methods: {
    parseRoute() {
      this.jobId = this.$route.query.jobId;
      // this.type = this.$route.query.type;
    }
  },
  created() {
    this.parseRoute();
  }
};
</script>
