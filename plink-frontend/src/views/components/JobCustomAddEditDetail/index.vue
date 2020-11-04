<template>
  <div>
    <!-- Page Header -->
    <div>
      <a-page-header style="padding: 5px 5px 0 5px" :title="usageModeHelper.title" :sub-title="usageModeHelper.subTitle" @back="() => $router.go(-1)">
        <template slot="tags">
          <a-tag :style="{ color: [3, 4, -1].includes(data.lastStatus) ? 'red' : 'green' }" v-show="data.lastStatusDesc">{{ data.lastStatusDesc }}</a-tag>
        </template>
        <template slot="extra">
          <a-button type="primary" size="small" v-show="usageModeHelper.showStartButton" style="margin-right: 5px" @click="onStart" :disabled="!data.authMap.start">启动</a-button>
          <a-button type="danger" size="small" v-show="usageModeHelper.showStopButton" style="margin-right: 5px" @click="onStop" :disabled="!data.authMap.stop">停止</a-button>
          <a-button type="primary" size="small" v-show="usageModeHelper.showEditButton" style="margin-right: 5px" @click="onEdit" :disabled="!data.authMap.edit">编辑</a-button>
          <a-button type="primary" size="small" v-show="usageModeHelper.showDetailButton" style="margin-right: 5px" @click="onDetail">详情</a-button>
          <a-button type="danger" size="small" v-show="usageModeHelper.showDeleteButton" style="margin-right: 5px" @click="onDelete" :disabled="!data.authMap.delete">删除</a-button>
          <a-button type="primary" size="small" @click="onGoBack">返回</a-button>
        </template>
      </a-page-header>
    </div>

    <!-- Page Content -->
    <a-tabs default-active-key="1" @change="handleTabChange">
      <a-tab-pane key="job">
        <span slot="tab">
          <a-icon type="file-text" />
          作业信息
        </span>

        <!-- 作业信息 -->
        <a-spin :spinning="usageModeHelper.isLoading" size="large">
          <div style="margin-top: 20px">
            <h2>基本配置</h2>
            <a-form-model ref="ruleForm" :model="data" :rules="rules" :label-col="labelCol" :wrapper-col="wrapperCol">
              <a-form-model-item label="作业名称" prop="name">
                <a-input v-model="data.name" />
              </a-form-model-item>
              <a-form-model-item label="作业类型" prop="type">
                <a-select v-model="data.type" placeholder="请选择作业类型" disabled>
                  <a-select-option v-for="(item, index) in helper.jobTypeList" :key="index" :value="item.value">
                    {{ item.desc }}
                  </a-select-option>
                </a-select>
              </a-form-model-item>
              <a-form-model-item label="作业描述">
                <a-textarea v-model="data.description" />
              </a-form-model-item>

              <H2>作业配置</H2>
              <a-form-model-item label="客户端版本" prop="clientVersion">
                <a-select v-model="data.clientVersion" placeholder="请选择版本">
                  <a-select-option v-for="(item, index) in helper.jobClientVersionList" :key="index" :value="item.value">
                    {{ item.desc }}
                  </a-select-option>
                </a-select>
              </a-form-model-item>
              <!-- 执行文件还没有 -->
              <a-form-model-item label="执行文件" prop="configJarName">
                <a-select v-model="data.flinkConfig.jarName" placeholder="请选择文件">
                  <a-select-option v-for="(item, index) in helper.jobJarList" :key="index" :value="item">
                    {{ item }}
                  </a-select-option>
                </a-select>

                <!-- 上传文件的作业件 -->
                <a-upload name="file" :action="jobUploadJarUrl" @change="handleJarUploadChange">
                  <a-button> <a-icon type="upload" /> 点击上传文件 </a-button>
                </a-upload>
              </a-form-model-item>
              <a-form-model-item label="MainClass" prop="configMainClass">
                <a-input v-model="data.flinkConfig.mainClass" />
              </a-form-model-item>
              <a-form-model-item label="程序参数" prop="configArgs">
                <a-textarea v-model="data.flinkConfig.args" />
              </a-form-model-item>

              <H2>运行参数</H2>
              <a-form-model-item label="作业并行度" prop="configParallelism">
                <a-input-number v-model="data.flinkConfig.parallelism" :min="1" />
              </a-form-model-item>

              <a-form-model-item :wrapper-col="{ span: wrapperCol.span, offset: labelCol.span }">
                <a-button type="primary" @click="onAdd" v-show="usageModeHelper.showAddButton">
                  新建
                </a-button>
                <a-button type="primary" @click="onUpdate" v-show="usageModeHelper.showUpdateButton">
                  更新
                </a-button>
                <a-button style="margin-left: 10px;" @click="onRest" v-show="usageModeHelper.showRest">
                  清空
                </a-button>
              </a-form-model-item>
            </a-form-model>
          </div>
        </a-spin>
      </a-tab-pane>

      <a-tab-pane key="instList">
        <span slot="tab">
          <a-icon type="database" />
          实例列表
        </span>

        <InstList ref="refInstList" :job-id="dataId" :is-auto-flush="true" />
      </a-tab-pane>
    </a-tabs>
  </div>
</template>

<script>
import * as jobApi from "@/api/job";
import * as helperApi from "@/api/helper";
import InstList from "@/views/inst/list";
export default {
  name: "JobCustomAddEditDetail",
  components: {
    InstList
  },
  props: {
    // 使用模式
    usageMode: {
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
        if (this.usageMode === "edit" || this.usageMode === "detail") {
          this.usageModeHelper.title = this.data.name;
          this.usageModeHelper.subTitle = this.data.description;
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
        flinkConfig: {
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
      usageModeHelper: {
        showPassword: false,
        showAddButton: false,
        showStartButton: false,
        showStopButton: false,
        showUpdateButton: false,
        showEditButton: false,
        showDetailButton: false,
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
        if (this.$refs.refInstList) {
          this.$refs.refInstList.clearDataListTimer();
        }
      }
    },
    initAddUsageModel() {
      this.usageModeHelper.showAddButton = true;
      this.usageModeHelper.showUpdateButton = false;
      this.usageModeHelper.showPassword = true;
      this.usageModeHelper.title = "新建作业";
      this.usageModeHelper.subTitle = "请仔细核对作业信息哟！";
    },
    initEditUsageModel() {
      this.usageModeHelper.isLoading = true;
      this.usageModeHelper.showAddButton = false;
      this.usageModeHelper.showUpdateButton = true;
      this.usageModeHelper.showPassword = true;
      this.usageModeHelper.showPassword = true;
      this.usageModeHelper.showDetailButton = true;
      this.usageModeHelper.showStartButton = false;
      this.usageModeHelper.showStopButton = false;
      this.usageModeHelper.title = "编辑作业";
      this.usageModeHelper.subTitle = "请仔细核对作业信息哟！";
    },
    initDetailUsageModel() {
      this.usageModeHelper.showAddButton = false;
      this.usageModeHelper.showUpdateButton = false;
      this.usageModeHelper.showPassword = false;
      this.usageModeHelper.showRest = false;
      this.usageModeHelper.showEditButton = true;
      this.usageModeHelper.showDeleteButton = true;
      this.usageModeHelper.showStartButton = true;
      this.usageModeHelper.showStopButton = true;
      this.usageModeHelper.title = "作业详情";
      this.usageModeHelper.subTitle = "请仔细核对作业信息哟！";
    },
    getData() {
      this.usageModeHelper.isLoading = true;
      // 根据 ID 获取数据详情
      jobApi.getJob(this.dataId).then(resp => {
        this.usageModeHelper.isLoading = false;
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
      switch (this.usageMode) {
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

<style scoped></style>
