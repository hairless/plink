<template>
  <div>
    <JobCustomAddEditDetail :usage-model="'add'" />
  </div>
</template>

<script>
import JobCustomAddEditDetail from "@/views/components/JobCustomAddEditDetail";
export default {
  name: "JobAdd",
  components: {
    JobCustomAddEditDetail
  }
};
</script>
