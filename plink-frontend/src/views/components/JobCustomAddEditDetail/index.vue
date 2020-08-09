<template>
  <div>
    <!-- Page Header -->
    <div>
      <a-page-header style="padding: 5px 5px 0 5px" :title="usageModelHelper.title" :sub-title="usageModelHelper.subTitle" @back="() => $router.go(-1)">
        <template slot="extra">
          <a-button type="primary" size="small" v-show="usageModelHelper.showStartButton" style="margin-right: 5px" @click="onStart" :disabled="!data.id">启动</a-button>
          <a-button type="danger" size="small" v-show="usageModelHelper.showStopButton" style="margin-right: 5px" @click="onStop" :disabled="!data.id">停止</a-button>
          <a-button type="primary" size="small" v-show="usageModelHelper.showEditButton" style="margin-right: 5px" @click="onEdit" :disabled="!data.id">编辑</a-button>
          <a-button type="primary" size="small" v-show="usageModelHelper.showDetailButton" style="margin-right: 5px" @click="onDetail">详情</a-button>
          <a-button type="danger" size="small" v-show="usageModelHelper.showDeleteButton" style="margin-right: 5px" @click="onDelete" :disabled="!data.id">删除</a-button>
          <a-button type="primary" size="small" @click="onGoBack">返回</a-button>
        </template>
      </a-page-header>
    </div>

    <!-- Page Content -->
    <a-tabs default-active-key="1">
      <a-tab-pane key="1">
        <span slot="tab">
          <a-icon type="file-text" />
          作业信息
        </span>

        <!-- 作业信息 -->
        <a-spin :spinning="usageModelHelper.isLoading" size="large">
          <div style="margin-top: 20px">
            <H3>基本配置</H3>
            <a-form-model ref="ruleForm" :model="data" :rules="rules" :label-col="labelCol" :wrapper-col="wrapperCol">
              <a-form-model-item label="作业名称" prop="name">
                <a-input v-model="data.name" />
              </a-form-model-item>
              <a-form-model-item label="作业类型" prop="type">
                <a-select v-model="data.type" placeholder="请选择角色">
                  <a-select-option v-for="(item, index) in helper.jobTypeList" :key="index" :value="item.value">
                    {{ item.desc }}
                  </a-select-option>
                </a-select>
              </a-form-model-item>
              <a-form-model-item label="作业描述">
                <a-textarea v-model="data.description" />
              </a-form-model-item>

              <H3>作业配置</H3>
              <a-form-model-item label="客户端版本" prop="clientVersion">
                <a-select v-model="data.clientVersion" placeholder="请选择版本">
                  <a-select-option v-for="(item, index) in helper.jobClientVersionList" :key="index" :value="item.value">
                    {{ item.desc }}
                  </a-select-option>
                </a-select>
              </a-form-model-item>
              <!-- 执行文件还没有 -->
              <a-form-model-item label="执行文件" prop="configJarName">
                <a-select v-model="data.config.jarName" placeholder="请选择文件">
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
                <a-input v-model="data.config.mainClass" />
              </a-form-model-item>
              <a-form-model-item label="程序参数" prop="configArgs">
                <a-textarea v-model="data.config.args" />
              </a-form-model-item>

              <H3>运行参数</H3>
              <a-form-model-item label="作业并行度" prop="configParallelism">
                <a-input-number v-model="data.config.parallelism" :min="1" />
              </a-form-model-item>

              <a-form-model-item :wrapper-col="{ span: wrapperCol.span, offset: labelCol.span }">
                <a-button type="primary" @click="onAdd" v-show="usageModelHelper.showAddButton">
                  新建
                </a-button>
                <a-button type="primary" @click="onUpdate" v-show="usageModelHelper.showUpdateButton">
                  更新
                </a-button>
                <a-button style="margin-left: 10px;" @click="onRest" v-show="usageModelHelper.showRest">
                  清空
                </a-button>
              </a-form-model-item>
            </a-form-model>
          </div>
        </a-spin>
      </a-tab-pane>

      <a-tab-pane key="2">
        <span slot="tab">
          <a-icon type="database" />
          实例列表
        </span>

        <InstList :job-id="dataId" />
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
  data() {
    return {
      labelCol: { span: 4 },
      wrapperCol: { span: 16 },
      data: {
        id: null,
        name: "",
        type: 1,
        clientVersion: "",
        config: {
          jarName: "",
          mainClass: "",
          args: "",
          parallelism: 1
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
      }
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
        this.getData();
      });
    },
    onStop() {
      jobApi.startJob(this.data.id).then(() => {
        this.$Notice.success({
          title: "停止作业成功！"
        });
        this.getData();
      });
    },
    onRest() {
      this.data = {};
    },
    handleJarUploadChange(info) {
      if (info.file.status !== "uploading") {
        console.log(info.file, info.fileList);
      }
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
      this.usageModelHelper.showDetailButton = true;
      this.usageModelHelper.showStartButton = false;
      this.usageModelHelper.showStopButton = false;
      this.usageModelHelper.title = "编辑作业";
      this.usageModelHelper.subTitle = "请仔细核对作业信息哟！";
      this.getData();
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
      this.getData();
    },
    getData() {
      this.usageModelHelper.isLoading = true;
      // 根据 ID 获取数据详情
      jobApi.getJob(this.dataId).then(resp => {
        this.usageModelHelper.isLoading = false;
        this.$nextTick(() => {
          this.data = resp.data;
        });
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
          this.initEditUsageModel();
          break;
        case "detail":
          this.initDetailUsageModel();
          break;
      }
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
