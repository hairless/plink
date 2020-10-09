<template>
  <div>
    <template v-if="type === 1">
      等待实现中 。。。
    </template>
    <template v-if="type === 2">
      等待实现中 。。。
    </template>
    <template v-else>
      等待实现中 。。。
    </template>
  </div>
</template>

<script>
export default {
  name: "JobAdd",
  data() {
    return {
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
