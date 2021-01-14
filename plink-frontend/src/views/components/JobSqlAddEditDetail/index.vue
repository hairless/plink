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
          <a-button type="primary" size="small" v-show="usageModeHelper.showUpdateButton" style="margin-right: 5px" @click="onUpdate">更新</a-button>
          <a-button type="primary" size="small" v-show="usageModeHelper.showDetailButton" style="margin-right: 5px" @click="onDetail">详情</a-button>
          <a-button type="danger" size="small" v-show="usageModeHelper.showDeleteButton" style="margin-right: 5px" @click="onDelete" :disabled="!data.authMap.delete">删除</a-button>
          <a-button type="primary" size="small" @click="onGoBack">返回</a-button>
        </template>
      </a-page-header>
    </div>

    <!-- Page Content -->
    <a-tabs v-model="helper.activeKey" @change="handleTabChange">
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
                <a-input v-model="data.name" v-if="['add', 'edit'].includes(usageMode)" />
                <span v-else-if="['detail'].includes(usageMode)">{{ data.name }}</span>
              </a-form-model-item>
              <a-form-model-item label="作业类型" prop="type">
                <a-select v-model="data.type" placeholder="请选择作业类型" disabled v-if="['add', 'edit'].includes(usageMode)">
                  <a-select-option v-for="(item, index) in helper.jobTypeList" :key="index" :value="item.value">
                    {{ item.desc }}
                  </a-select-option>
                </a-select>
                <span v-else-if="['detail'].includes(usageMode)">{{ data.typeDesc }}</span>
              </a-form-model-item>
              <a-form-model-item label="作业描述">
                <a-textarea v-model="data.description" />
              </a-form-model-item>

              <H2>作业配置</H2>
              <a-form-model-item label="主节点内存" prop="flinkConfigJobManagerMemory">
                <a-input v-model="data.flinkConfig.jobManagerMemory" v-if="['add', 'edit'].includes(usageMode)" />
                <span v-else-if="['detail'].includes(usageMode)">{{ data.flinkConfig.jobManagerMemory }}</span>
              </a-form-model-item>
              <a-form-model-item label="执行节点内存" prop="flinkConfigTaskManagerMemory">
                <a-input v-model="data.flinkConfig.taskManagerMemory" v-if="['add', 'edit'].includes(usageMode)" />
                <span v-else-if="['detail'].includes(usageMode)">{{ data.flinkConfig.taskManagerMemory }}</span>
              </a-form-model-item>
              <a-form-model-item label="Slot 数/节点" prop="flinkConfigTaskManagerSlots">
                <a-input-number v-model="data.flinkConfig.taskManagerSlots" :min="1" v-if="['add', 'edit'].includes(usageMode)" />
                <span v-else-if="['detail'].includes(usageMode)">{{ data.flinkConfig.taskManagerSlots }}</span>
              </a-form-model-item>
              <a-form-model-item label="作业并行度" prop="flinkConfigParallelism">
                <a-input-number v-model="data.flinkConfig.parallelism" :min="1" v-if="['add', 'edit'].includes(usageMode)" />
                <span v-else-if="['detail'].includes(usageMode)">{{ data.flinkConfig.parallelism }}</span>
              </a-form-model-item>
              <a-form-model-item label="是否重试" prop="isRetry">
                <a-switch checked-children="开" un-checked-children="关" v-model="data.flinkConfig.isRetry" />
              </a-form-model-item>
              <a-form-model-item label="Flink 参数" prop="flinkConfigConfigs">
                <a-textarea
                  v-model="data.flinkConfig.configsStr"
                  :autoSize="{ minRows: 7 }"
                  placeholder="flink.conf.key1=value1
flink.conf.key2=value2
flink.conf.key3=value3"
                />
                <div v-for="(value, key) in helper.defaultFlinkConfs" :key="key">{{ key + "=" + value }}</div>
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

      <a-tab-pane key="sql">
        <span slot="tab">
          <a-icon type="database" />
          Flink SQL
        </span>

        <a-row class="head">
          <a-alert v-if="alertMessage !== ''" type="error" closable @close="alertMessage = ''" style="margin-bottom: 5px;white-space:pre-wrap">
            <div slot="message">
              {{ alertMessage }}
              <a v-if="alertException" @click="showAlertException = !showAlertException">查看异常信息</a>
              <span v-if="showAlertException" style="white-space:pre-wrap"><br />{{ alertException }}</span>
            </div>
          </a-alert>
          <a-button-group style="float: right">
            <a-button type="primary" icon="medicine-box" @click="sqlGraph">拓扑图</a-button>
            <a-button type="primary" icon="medicine-box" @click="sqlParse">SQL校验</a-button>
            <a-button type="primary" icon="medicine-box" @click="sqlFormat">格式化</a-button>
          </a-button-group>
        </a-row>
        <a-row>
          <SqlCMEditor ref="sqlEditor" v-model="data.extraConfig.sql" height="650" :read-only="usageModeHelper.editorReadOnly" />
        </a-row>
      </a-tab-pane>

      <a-tab-pane key="instList" v-if="['detail'].includes(usageMode)">
        <span slot="tab">
          <a-icon type="database" />
          实例列表
        </span>

        <InstList ref="refInstList" :job-id="dataId" :is-auto-flush="true" />
      </a-tab-pane>
    </a-tabs>

    <!-- 拓扑图 -->
    <a-modal title="拓扑图" :visible="graph.isVisible" @ok="handleGraphOk" @cancel="handleGraphCancel" width="80%">
      <div style="height: 600px">
        <div id="flowGraph" class="graph-flow" />
        <div style="display: flex" v-if="graphHelper.activeNode">
          <div class="graph-node">
            <div class="graph-node-title">基础信息</div>
            <div class="graph-node-info">节点名称 : {{ graphHelper.activeNode.name }}</div>
            <div class="graph-node-info">节点类型 : {{ graphHelper.activeNode.c_type }}</div>
            <div class="graph-node-info">节点描述 : {{ graphHelper.activeNode.comment }}</div>
          </div>
          <div class="graph-node" style="margin: 0 10px 0 10px">
            <div class="graph-node-title">列信息</div>
            <a-table
              :columns="graphHelper.activeNodeColumnList"
              :data-source="graphHelper.activeNode.columnList"
              :row-key="(row, index) => index"
              :pagination="false"
              size="middle"
              :scroll="{ y: 210 }"
            >
            </a-table>
          </div>
          <div class="graph-node">
            <div class="graph-node-title">SQL</div>
            <a-textarea v-model="graphHelper.activeNode.sql" :autosize="{ minRows: 10, maxRows: 10}" :readonly="true" style="min-height: 250px"></a-textarea>
          </div>
        </div>
        <div id="test_length" style="position:absolute;visibility: hidden; white-space: nowrap;z-index: -100"></div>
      </div>
    </a-modal>
  </div>
</template>

<script>
import * as jobApi from "@/api/job";
import * as sqlApi from "@/api/sql";
import * as helperApi from "@/api/helper";
import InstList from "@/views/inst/list";
import SqlCMEditor from "@/components/SqlCMEditor";
import G6 from "@antv/g6";
export default {
  components: {
    InstList,
    SqlCMEditor
  },
  name: "JobSqlAddEditDetail",
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
      alertMessage: "",
      showAlertException: false,
      alertException: "",
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
          jobManagerMemory: "1G",
          taskManagerMemory: "2G",
          taskManagerSlots: "",
          parallelism: 1,
          isRetry: false,
          configsStr: "",
          configs: ""
        },
        extraConfig: {
          sql: ""
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
        name: [{ required: true, message: "请输入作业名！", trigger: "blur" }],
        type: [{ required: true, message: "请选择作业类型！", trigger: "blur" }]

        // 下拉框的校验规则，对面里面还有子对象的规则，未直接生效，后续看看怎么弄
        /*
        flinkConfigJobManagerMemory: [{ required: true, message: "请输入主节点内存！", trigger: "blur" }],
        flinkConfigTaskManagerMemory: [{ required: true, message: "请输入执行节点内存！", trigger: "blur" }],
        flinkConfigTaskManagerSlots: [{ required: true, message: "请输入执行节点核数！", trigger: "blur" }]
        */
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
        editorReadOnly: true,
        title: "",
        subTitle: ""
      },

      // helper
      helper: {
        jobTypeList: [],
        jobJarList: [],
        jobClientVersionList: [],
        activeKey: "job",
        defaultFlinkConfs: {}
      },
      dataTimer: null,

      // 拓扑图
      graph: {
        data: {
          nodeList: [],
          linkList: []
        },
        isVisible: false
      },
      graphHelper: {
        graph: null,
        activeNode: null,
        activeNodeColumnList: [
          {
            title: "列名",
            dataIndex: "name"
          },
          {
            title: "类型",
            dataIndex: "type"
          },
          {
            title: "允许空",
            dataIndex: "nullable"
          },
          {
            title: "描述",
            dataIndex: "comment"
          }
        ]
      }
    };
  },
  methods: {
    showAlert(msg, exception) {
      this.alertMessage = msg;
      this.showAlertException = false;
      if (exception) {
        this.alertException = exception;
      } else {
        this.alertException = null;
      }
    },
    sqlFormat() {
      this.$refs.sqlEditor.formatSql();
    },
    buildNodeType(node) {
      let type = "cycle";
      switch (node.type) {
        case "TABLE":
          type = "ellipse";
          break;
        case "VIEW":
          type = "rect";
          break;
        case "INSERT":
          type = "diamond";
          break;
        default:
      }
      return type;
    },
    drawGraph() {
      let data = {};

      data.nodes = this.graph.data.nodeList.map(node => {
        return {
          id: node.name,
          label: node.name,
          name: node.name,
          type: this.buildNodeType(node),
          c_type: node.type,
          comment: node.comment,
          columnList: node.columnList,
          sql: node.sql,
          size: [this.getTextVisualLength(node.name, "14px") + 10, 20]
        };
      });
      data.edges = this.graph.data.linkList.map(edge => {
        return {
          source: edge.sourceName,
          target: edge.targetName
        };
      });
      let width = document.getElementById("flowGraph").scrollWidth;
      let height = 250;
      if (this.graphHelper.graph) {
        this.graphHelper.graph.changeData(data);
        this.graphHelper.graph.refresh();
        return;
      } else {
        this.graphHelper.graph = new G6.Graph({
          container: "flowGraph", // String | HTMLElement，必须，在 Step 1 中创建的容器 id 或容器本身
          width: width,
          height: height,
          fitView: true,
          fitViewPadding: 30,
          modes: {
            default: ["drag-canvas", "drag-node", "zoom-canvas"]
          },
          layout: {
            type: "dagre",
            rankdir: "LR",
            align: "UL",
            controlPoints: true,
            nodesepFunc: () => 1,
            ranksepFunc: () => 1
          },
          defaultNode: {
            size: [30, 20],
            type: "ellipse",
            style: {
              lineWidth: 1,
              stroke: "#5B8FF9",
              fill: "#C6E5FF"
            },
            labelCfg: {
              position: "center",
              style: {
                fontSize: 11,
                cursor: "pointer"
              }
            }
          },
          defaultEdge: {
            type: "cubic-horizontal",
            size: 1,
            color: "#56e25e",
            style: {
              endArrow: {
                path: "M 0,0 L 8,4 L 8,-4 Z",
                fill: "#e2e2e2"
              },
              radius: 20
            }
          },
          nodeStateStyles: {
            click: {
              stroke: "#008aff",
              lineWidth: 2
            }
          }
        });
        this.graphHelper.graph.data(data); // load data
        this.graphHelper.graph.render(); // render

        // 监听事件
        // 监听鼠标点击节点
        this.graphHelper.graph.on("node:click", e => {
          // 先将所有当前有 click 状态的节点的 click 状态置为 false
          const clickNodes = this.graphHelper.graph.findAllByState("node", "click");
          clickNodes.forEach(cn => {
            this.graphHelper.graph.setItemState(cn, "click", false);
          });
          const nodeItem = e.item;
          // 设置目标节点的 click 状态 为 true
          this.graphHelper.graph.setItemState(nodeItem, "click", true);
          this.graphHelper.activeNode = nodeItem._cfg.model;
        });
      }
    },
    sqlGraph() {
      this.graph.isVisible = true;
      this.$nextTick(() => {
        sqlApi.sqlParse(this.data.extraConfig.sql).then(resp => {
          this.graph.data = resp.data;
          this.drawGraph();
        });
        // this.drawGraph();
      });
    },
    handleGraphOk() {
      this.graph.isVisible = false;
    },
    handleGraphCancel() {
      this.graph.isVisible = false;
    },
    sqlParse() {
      this.alertMessage = "";
      this.$refs.sqlEditor.clearMarker();
      sqlApi
        .sqlParse(this.data.extraConfig.sql)
        .then(res => {
          if (res.code === 10004) {
            this.$refs.sqlEditor.markText(res.data.lineNumber - 1, res.data.columnNumber - 1, res.data.endLineNumber - 1, res.data.endColumnNumber);
            this.showAlert(res.msg);
          } else {
            this.$Notice.success({
              title: "SQL校验成功！"
            });
          }
        })
        .catch(res => {
          console.log(res);
          this.showAlert(res.msg, res.exceptionStackTrace);
        });
    },
    onAdd() {
      this.$refs.ruleForm.validate(valid => {
        if (valid) {
          jobApi.addJob(this.data).then(resp => {
            this.$router.push({
              path: "/page/job/jobDetail",
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
          let configsStr = this.data.flinkConfig.configsStr;
          let configKVs = configsStr ? configsStr.split(/[\n]+/) : [];
          let configKVMap = {};
          if (configKVs.length > 0) {
            configKVs.forEach(item => {
              let kv = item.split("=");
              let k = kv[0];
              let v = kv[1];
              configKVMap[k] = v;
            });
          }
          let data = {
            id: this.data.id,
            name: this.data.name,
            type: this.data.type,
            description: this.data.description,
            flinkConfig: {
              jobManagerMemory: this.data.flinkConfig.jobManagerMemory,
              taskManagerMemory: this.data.flinkConfig.taskManagerMemory,
              taskManagerSlots: this.data.flinkConfig.taskManagerSlots,
              parallelism: this.data.flinkConfig.parallelism,
              isRetry: this.data.flinkConfig.isRetry,
              configs: configKVMap
            },
            extraConfig: this.data.extraConfig
          };
          jobApi.updateJob(data).then(() => {
            this.$router.push({
              path: "/page/job/jobDetail",
              query: {
                type: this.data.type,
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
        path: "/page/job/jobEdit",
        query: {
          type: this.data.type,
          jobId: this.data.id
        }
      });
    },
    onDetail() {
      this.$router.push({
        path: "/page/job/jobDetail",
        query: {
          type: this.data.type,
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
          path: "/page/job/jobList"
        });
      });
    },
    onStart() {
      jobApi.startJob(this.data.id).then(() => {
        this.$Notice.success({
          title: "启动作业成功！"
        });
        this.handleFlush(true);

        if (this.helper.activeKey === "instList") {
          // 子组件刷新数据
          this.$refs.refInstList.getDataList();
        }
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
      } else {
        if (this.$refs.refInstList) {
          this.$refs.refInstList.getDataList();
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
      (this.usageModeHelper.editorReadOnly = false), (this.usageModeHelper.title = "编辑作业");
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
      (this.usageModeHelper.editorReadOnly = true), (this.usageModeHelper.title = "作业详情");
      this.usageModeHelper.subTitle = "请仔细核对作业信息哟！";
    },
    getData() {
      this.usageModeHelper.isLoading = true;
      // 根据 ID 获取数据详情
      jobApi.getJob(this.dataId).then(resp => {
        this.usageModeHelper.isLoading = false;
        this.data = resp.data;

        // test
        if (!this.data["extraConfig"].sql) {
          this.data["extraConfig"] = {
            sql: ""
          };
        }

        // str to map
        let configsStr = "";
        if (this.data.flinkConfig.configs) {
          let count = 0;
          Object.entries(this.data.flinkConfig.configs).forEach(item => {
            if (count > 0) {
              configsStr += "\n";
            }
            configsStr += `${item[0]}=${item[1]}`;
            count += 1;
          });
        }
        this.data.flinkConfig.configsStr = configsStr;

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
    //工具方法获取文字的像素宽度
    getTextVisualLength(text, size) {
      let ruler = document.getElementById("test_length");
      ruler.style.fontSize = size || "inherit";
      ruler.innerText = text;
      return ruler.clientWidth;
    },
    initHelper() {
      helperApi.getJobTypeList().then(resp => {
        this.helper.jobTypeList = resp.data;
      });
      helperApi.getJobClientVersionList().then(resp => {
        this.helper.jobClientVersionList = resp.data;
      });
      helperApi.getDefaultFlinkConfs().then(resp => {
        this.helper.defaultFlinkConfs = resp.data;
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

<style scoped>
.head {
  background: rgba(242, 241, 244, 0.95);
  box-shadow: 0 2px 4px 0 rgba(0, 0, 0, 0.12), 0 0 6px 0 rgba(0, 0, 0, 0.04);
}
.graph-flow {
  border: 1px solid #008aff;
  padding: 10px;
  margin-bottom: 20px;
}
.graph-node {
  border: 1px solid #19d0ff;
  padding: 10px;
  width: 33.3%;
  height: 310px;
}
.graph-node-title {
  border-left: 3px solid #00ff9c;
  padding-left: 3px;
  color: rgba(0, 0, 0, 0.85);
  font-weight: bold;
  font-size: 16px;
  line-height: 1.3;
  margin-bottom: 15px;
}
.graph-node-info {
  line-height: 3;
}
</style>
