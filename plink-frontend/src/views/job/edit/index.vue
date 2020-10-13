<template>
  <div>
    <template v-if="type === 1">
      <JobCustomAddEditDetail :usage-model="'edit'" :data-id="jobId" />
    </template>
    <template v-else-if="type === 3">
      <JobSqlAddEditDetail :usage-model="'edit'" :data-id="jobId" />
    </template>
    <template v-else>
      等待实现中 。。。
    </template>
  </div>
</template>

<script>
import JobCustomAddEditDetail from "@/views/components/JobCustomAddEditDetail";
import JobSqlAddEditDetail from "@/views/components/JobSqlAddEditDetail";
export default {
  components: {
    JobCustomAddEditDetail,
    JobSqlAddEditDetail
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
      this.type = Number(this.$route.query.type);
    }
  },
  created() {
    this.parseRoute();
  }
};
</script>
