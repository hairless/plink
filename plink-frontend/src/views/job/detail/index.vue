<template>
  <div>
    <template v-if="type === 1">
      <JobCustomAddEditDetail :usage-model="'detail'" :data-id="jobId" />
    </template>
    <template v-else-if="type === 2">
      <JobSqlDetail :usage-model="'detail'" :data-id="jobId" />
    </template>
    <template v-else>
      等待实现中 。。。
    </template>
  </div>
</template>

<script>
import JobCustomAddEditDetail from "@/views/components/JobCustomAddEditDetail";
import JobSqlDetail from "@/views/components/JobSqlDetail";
export default {
  components: {
    JobCustomAddEditDetail,
    JobSqlDetail
  },
  name: "JobDetail",
  data() {
    return {
      jobId: -1,
      type: -1
    };
  },
  methods: {
    parseRoute() {
      this.jobId = this.$route.query.jobId;
      this.type = this.$route.query.type;
    }
  },
  created() {
    this.parseRoute();
  }
};
</script>
