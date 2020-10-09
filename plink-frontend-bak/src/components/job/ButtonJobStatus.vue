<template>
  <div>
    <Button :type="statusToType" :size="size" v-text="text" />
  </div>
</template>

<script lang="ts">
import { Component, Vue, Watch, Prop } from "vue-property-decorator";

@Component
export default class JobDetailCustom extends Vue {
  @Prop({ default: "" })
  text: string | undefined;
  @Prop({ default: "-1" })
  status: any;
  @Prop({ default: "default" })
  size: string | undefined;

  get statusToType() {
    let t = this.jobInstanceEnumMap[this.status];
    if (t === undefined) {
      return "warning";
    }
    return t.type;
  }

  jobInstanceEnumMap: any = {
    "0": {
      value: 0,
      desc: "待启动",
      type: "info"
    },
    "1": {
      value: 0,
      desc: "启动中",
      type: "info"
    },
    "2": {
      value: 0,
      desc: "运行中",
      type: "success"
    },
    "3": {
      value: 0,
      desc: "启动失败",
      type: "error"
    },
    "4": {
      value: 0,
      desc: "运行失败",
      type: "error"
    },
    "5": {
      value: 0,
      desc: "已停止",
      type: "success"
    },
    "6": {
      value: 0,
      desc: "运行成功",
      type: "success"
    },
    "-1": {
      value: 0,
      desc: "未知",
      type: "warning"
    }
    // WAITING_START(0, "待启动", false),
    // STARTING(1, "启动中", false),
    // RUNNING(2, "运行中", false),
    // START_FAILED(3, "启动失败", true),
    // RUN_FAILED(4, "运行失败", true),
    // STOPPED(5, "已停止", true),
    // SUCCESS(6, "运行成功", true),
    // UNKNOWN(-1, "未知", false);
  };
}
</script>

<style scoped></style>
