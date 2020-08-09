<template>
  <div>
    <JobCustomAddEditDetail :usage-model="'edit'" :data-id="jobId" />
  </div>
</template>

<script>
import JobCustomAddEditDetail from "@/views/components/JobCustomAddEditDetail";
export default {
  name: "JobEdit",
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
