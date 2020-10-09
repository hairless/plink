<template>
  <div style="height: 100%">
    <!-- Page Header -->
    <div>
      <a-page-header style="padding: 5px 5px 0 5px" :title="usageModelHelper.title" :sub-title="usageModelHelper.subTitle" @back="() => $router.go(-1)">
        <template slot="tags">
          <a-tag :style="{ color: [3, 4, -1].includes(data.lastStatus) ? 'red' : 'green' }" v-show="data.lastStatusDesc">{{ data.lastStatusDesc }}</a-tag>
        </template>
        <template slot="extra">
          <a-button type="primary" size="small" v-show="usageModelHelper.showStartButton" style="margin-right: 5px" @click="onStart" :disabled="!data.authMap.start">启动</a-button>
          <a-button type="danger" size="small" v-show="usageModelHelper.showStopButton" style="margin-right: 5px" @click="onStop" :disabled="!data.authMap.stop">停止</a-button>
          <a-button type="primary" size="small" v-show="usageModelHelper.showEditButton" style="margin-right: 5px" @click="onEdit" :disabled="!data.authMap.edit">编辑</a-button>
          <a-button type="primary" size="small" v-show="usageModelHelper.showDetailButton" style="margin-right: 5px" @click="onDetail">详情</a-button>
          <a-button type="primary" size="small" v-show="usageModelHelper.showSaveButton" style="margin-right: 5px" @click="onSave">保存</a-button>
          <a-button type="danger" size="small" v-show="usageModelHelper.showDeleteButton" style="margin-right: 5px" @click="onDelete" :disabled="!data.authMap.delete">删除</a-button>
          <a-button type="primary" size="small" @click="onGoBack">返回</a-button>
        </template>
      </a-page-header>
    </div>

    <!-- Page Content -->
    <div>
      <a-row>
        <!-- SQL 编辑器 -->
        <a-col span="16" class="sql-editor-container">
          <div style="background-color: #f7f7f7; padding: 5px">
            <a-row :gutter="16">
              <a-col span="12"><a-button type="primary" size="small">查看执行计划</a-button></a-col>
              <a-col span="12" align="right">
                <!--<a-button type="primary" size="small">格式化</a-button>-->
                <a-button type="primary" size="small" style="margin-left: 5px" @click="onDebug">调试</a-button>
              </a-col>
            </a-row>
          </div>
          <SqlCMEditor height="700" theme="eclipse" />
        </a-col>

        <!-- 参数配置 -->
        <a-col span="8" class="job-detail">
          <a-tabs type="card" default-active-key="1">
            <a-tab-pane key="1">
              <span slot="tab">
                <a-icon type="file-text" />
                作业信息
              </span>
            </a-tab-pane>

            <a-tab-pane key="2">
              <span slot="tab">
                <a-icon type="file-text" />
                高级配置
              </span>
            </a-tab-pane>
          </a-tabs>
        </a-col>
      </a-row>
    </div>
  </div>
</template>

<script>
import * as jobApi from "@/api/job";
import * as helperApi from "@/api/helper";
import SqlCMEditor from "@/components/SqlCMEditor";
export default {
  components: {
    SqlCMEditor
  },
  name: "JobSqlEdit",
  props: {
    // 使用模式
    usageModel: {
      default: "" // add | edit | update
    },
    dataId: {
      default: -1
    }
  },
  computed: {
    jobUploadJarUrl: function() {
      return jobApi.UPLOAD_JAR_URL.replace("{jobId}", this.dataId);
    }
  },
  watch: {
    data: {
      deep: true,
      handler() {
        if (this.usageModel === "edit" || this.usageModel === "detail") {
          this.usageModelHelper.title = this.data.name;
          this.usageModelHelper.subTitle = this.data.description;
        }
      }
    }
  },
  data() {
    return {
      labelCol: { span: 4 },
      wrapperCol: { span: 16 },
      data: {
        id: null,
        name: "",
        type: 1,
        lastStatus: null,
        statusDesc: "",
        clientVersion: "",
        config: {
          jarName: "",
          mainClass: "",
          args: "",
          parallelism: 1
        },
        authMap: {
          edit: false,
          delete: false,
          start: false,
          stop: false,
          restart: false
        }
      },
      rules: {
        name: [{ required: true, message: "请输入作业名！", trigger: "blur" }]

        // 下拉框的校验规则，对面里面还有子对象的规则，未直接生效，后续看看怎么弄
        // type: [{ required: true, message: "请选择类型！", trigger: "blur" }],
        // clientVersion: [{ required: true, message: "请选择客户端版本！", trigger: "blur" }],
        // configJarName: [{ required: true, message: "请选择执行文件！", trigger: "blur" }],
        // configMainClass: [{ required: true, message: "请输入 Java Main Class！", trigger: "blur" }]
      },
      usageModelHelper: {
        showPassword: false,
        showAddButton: false,
        showStartButton: false,
        showStopButton: false,
        showUpdateButton: false,
        showEditButton: false,
        showDetailButton: false,
        showSaveButton: true,
        showDeleteButton: false,
        showRest: true,
        isLoading: false,
        title: "",
        subTitle: ""
      },

      // helper
      helper: {
        jobTypeList: [],
        jobJarList: [],
        jobClientVersionList: []
      },
      dataTimer: null
    };
  },
  methods: {
    onAdd() {
      this.$refs.ruleForm.validate(valid => {
        if (valid) {
          jobApi.addJob(this.data).then(resp => {
            this.$router.push({
              path: "/job/jobDetail",
              query: {
                jobId: resp.data.id
              }
            });
            this.$Notice.success({
              title: "新建作业成功！"
            });
          });
        } else {
          this.$message.error("请检查必填项！！！");
          return false;
        }
      });
    },
    onUpdate() {
      this.$refs.ruleForm.validate(valid => {
        if (valid) {
          jobApi.updateJob(this.data).then(() => {
            this.$router.push({
              path: "/job/jobDetail",
              query: {
                jobId: this.data.id
              }
            });
            this.$Notice.success({
              title: "更新作业成功！"
            });
          });
        } else {
          this.$message.error("请检查必填项");
          return false;
        }
      });
    },
    onEdit() {
      this.$router.push({
        path: "/job/jobEdit",
        query: {
          jobId: this.data.id
        }
      });
    },
    onDetail() {
      this.$router.push({
        path: "/job/jobDetail",
        query: {
          jobId: this.data.id
        }
      });
    },
    onSave() {
      // save ...
    },
    onDelete() {
      jobApi.deleteJob(this.data.id).then(() => {
        this.$Notice.success({
          title: "删除作业成功！"
        });
        this.$router.push({
          path: "/job/jobList"
        });
      });
    },
    onStart() {
      jobApi.startJob(this.data.id).then(() => {
        this.$Notice.success({
          title: "启动作业成功！"
        });
        this.handleFlush(true);
      });
    },
    onStop() {
      jobApi.stopJob(this.data.id).then(() => {
        this.$Notice.success({
          title: "停止作业成功！"
        });
        this.getData();
      });
    },
    /* 定时器自动刷新 */
    handleFlush(checked) {
      if (checked) {
        if (!this.dataTimer) {
          this.dataTimer = this.getDataTimer();
        }
      } else {
        this.clearDataTimer();
      }
    },
    getDataTimer() {
      return setInterval(() => {
        this.getData();
      }, 1000);
    },
    clearDataTimer() {
      clearInterval(this.dataTimer);
      this.dataTimer = null;
    },
    onRest() {
      this.data = {};
    },
    handleJarUploadChange(info) {
      if (info.file.status === "done") {
        this.$message.success(`${info.file.name} 上传成功！`);

        this.getJobUploadJarList();
      } else if (info.file.status === "error") {
        this.$message.error(`${info.file.name} 上传失败！`);
      }
    },
    onGoBack() {
      this.$router.go(-1);
    },
    handleTabChange(activeKey) {
      if (activeKey !== "instList") {
        // 实例列表清除定时器
        this.$refs.refInstList.clearDataListTimer();
      }
    },
    initAddUsageModel() {
      this.usageModelHelper.showAddButton = true;
      this.usageModelHelper.showUpdateButton = false;
      this.usageModelHelper.showPassword = true;
      this.usageModelHelper.title = "新建作业";
      this.usageModelHelper.subTitle = "请仔细核对作业信息哟！";
    },
    initEditUsageModel() {
      this.usageModelHelper.isLoading = true;
      this.usageModelHelper.showAddButton = false;
      this.usageModelHelper.showUpdateButton = true;
      this.usageModelHelper.showPassword = true;
      this.usageModelHelper.showPassword = true;
      this.usageModelHelper.showDetailButton = false;
      this.usageModelHelper.showStartButton = false;
      this.usageModelHelper.showStopButton = false;
      this.usageModelHelper.title = "编辑作业";
      this.usageModelHelper.subTitle = "请仔细核对作业信息哟！";
    },
    initDetailUsageModel() {
      this.usageModelHelper.showAddButton = false;
      this.usageModelHelper.showUpdateButton = false;
      this.usageModelHelper.showPassword = false;
      this.usageModelHelper.showRest = false;
      this.usageModelHelper.showEditButton = true;
      this.usageModelHelper.showDeleteButton = true;
      this.usageModelHelper.showStartButton = true;
      this.usageModelHelper.showStopButton = true;
      this.usageModelHelper.title = "作业详情";
      this.usageModelHelper.subTitle = "请仔细核对作业信息哟！";
    },
    getData() {
      this.usageModelHelper.isLoading = true;
      // 根据 ID 获取数据详情
      jobApi.getJob(this.dataId).then(resp => {
        this.usageModelHelper.isLoading = false;
        this.data = resp.data;

        // 最终状态清除定时器
        if ([3, 4, 5, 6].includes(this.data.lastStatus)) {
          this.clearDataTimer();
        }
      });
    },
    getJobUploadJarList() {
      jobApi.getJobJarList(this.dataId).then(resp => {
        this.helper.jobJarList = resp.data;
      });
    },
    initHelper() {
      helperApi.getJobTypeList().then(resp => {
        this.helper.jobTypeList = resp.data;
      });
      helperApi.getJobClientVersionList().then(resp => {
        this.helper.jobClientVersionList = resp.data;
      });
      this.getJobUploadJarList();
    },
    initUsageModel() {
      // 使用模式
      switch (this.usageModel) {
        case "add":
          this.initAddUsageModel();
          break;
        case "edit":
          this.getData();
          this.initEditUsageModel();
          break;
        case "detail":
          this.getData();
          this.initDetailUsageModel();
          break;
      }
    },
    beforeDestroy() {
      this.clearDataTimer();
    },
    init() {
      this.initHelper();
      this.initUsageModel();
    }
  },
  created() {
    this.init();
  }
};
</script>

<style lang="stylus" rel="stylesheet/stylus" scoped>
.sql-editor-container
  border: solid 1px #dcdcdc
  min-height: 700px

.job-detail
  border: solid 1px #dcdcdc
  min-height: 736px
</style>
