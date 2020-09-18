<template>
  <div>
    <JobCustomAddEditDetail :usage-model="'detail'" :data-id="jobId" />
  </div>
</template>

<script>
import JobCustomAddEditDetail from "@/views/components/JobCustomAddEditDetail";
export default {
  name: "JobDetail",
  components: {
    JobCustomAddEditDetail
  },
  data() {
    return {
      jobId: -1
    };
  },
  methods: {
    init() {
      this.jobId = this.$route.query.jobId;
    }
  },
  created() {
    this.init();
  }
};
</script>
