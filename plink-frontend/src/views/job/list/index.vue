<template>
  <div>
    <!-- Data Filter -->
    <div style="margin-bottom: 10px; padding: 5px; background-image: linear-gradient(100deg, rgba(60, 213, 255, 0.5), rgba(60, 213, 255, 0.3));">
      <a-row :gutter="16">
        <a-col class="gutter-row" :span="20" style="padding-left: 20px">
          <div class="gutter-box">
            <span
              >ID :
              <a-input v-model="dataFilter.id" placeholder="eg. 1001" style="width: 100px" size="small" allowClear />
            </span>
            <span style="margin-left: 10px"
              >名称 :
              <a-input v-model="dataFilter.name" placeholder="eg. 测试作业" style="width: 200px" size="small" allowClear />
            </span>
            <span style="margin-left: 10px"
              >类型 :
              <a-select v-model="dataFilter.type" style="width: 100px" size="small" allowClear @change="getDataList">
                <a-select-option v-for="(item, index) in helper.jobTypeList" :key="index" :value="item.value">
                  {{ item.desc }}
                </a-select-option>
              </a-select>
            </span>
            <span style="margin-left: 10px"
              >状态 :
              <a-select v-model="dataFilter.lastStatus" style="width: 100px" size="small" allowClear @change="getDataList">
                <a-select-option v-for="(item, index) in helper.instStatusList" :key="index" :value="item.value">
                  {{ item.desc }}
                </a-select-option>
              </a-select>
            </span>
          </div>
        </a-col>
        <a-col class="gutter-row" :span="4" align="right">
          <div class="gutter-box">
            <a-button type="primary" size="small" class="filter-tool" @click="onQuery">查询</a-button>
            <a-button type="primary" size="small" class="filter-tool" @click="onAdd">新建</a-button>
            <a-button type="primary" size="small" class="filter-tool" @click="onGoBack">返回</a-button>
          </div>
        </a-col>
      </a-row>
    </div>

    <!-- Data Table List -->
    <div>
      <a-table
        :columns="dataColumns"
        :data-source="dataList"
        :rowKey="item => item.id"
        :loading="isLoading"
        :pagination="false"
        :scroll="{ x: 1920 }"
        :row-selection="{
          selectedRowKeys: dataTableSelectedRowKeys,
          onChange: onDataTableSelectedChange
        }"
      >
        <span slot="name" slot-scope="current, row">
          <router-link :to="{ path: '/page/job/jobDetail', query: { type: row.type, jobId: row.id } }">{{ current }}</router-link>
        </span>
        <span slot="lastStatusDesc" slot-scope="current, row">
          <span :style="{ color: [3, 4, -1].includes(row.lastStatus) ? 'red' : 'green' }">{{ current }}</span>
        </span>
        <span slot="action" slot-scope="row">
          <router-link :to="{ path: '/page/job/JobDetail', query: { type: row.type, jobId: row.id } }">详情</router-link>
          <a-divider type="vertical" />
          <router-link :to="{ path: '/page/job/JobEdit', query: { type: row.type, jobId: row.id } }" @click="onEdit(row)">编辑</router-link>
        </span>
      </a-table>
    </div>

    <!-- Bottom Tool -->
    <!-- Tools -->
    <div style="margin-top: 10px; padding: 5px; background-image: linear-gradient(100deg, rgba(60,213,255,0.30), rgba(60,213,255,0.20));">
      <row>
        <a-row :gutter="16">
          <a-col class="gutter-row" :span="12">
            <div class="gutter-box">
              <a-button type="primary" size="small" class="filter-tool" @click="onStartJobList" :disabled="helper.isDisabledStartJobListButton">启动</a-button>
              <a-button type="danger" size="small" class="filter-tool" @click="onRestartJobList" :disabled="helper.isDisabledRestartJobListButton">重启</a-button>
              <a-button type="primary" size="small" class="filter-tool" @click="onStopJobList" :disabled="helper.isDisabledStopJobListButton">停止</a-button>
              <a-button type="primary" size="small" class="filter-tool" @click="onDeleteJobList" :disabled="helper.isDisabledDeleteJobListButton">删除</a-button>
            </div>
          </a-col>
          <a-col class="gutter-row" :span="12" align="right">
            <div class="gutter-box">
              <a-pagination
                size="small"
                :pageSizeOptions="['1', '2', '10', '20', '30', '40', '50', '100', '500', '1000']"
                :total="page.total"
                :show-total="(total, range) => `第 ${range[0]} ~ ${range[1]} 项，共 ${page.total} 项`"
                show-size-changer
                show-quick-jumper
                @change="onPageNumChange"
                @showSizeChange="onPageSizeChange"
              />
            </div>
          </a-col>
        </a-row>
      </row>
    </div>

    <!-- 新建作业的对话框 -->
    <div>
      <a-modal v-model="jobAddModal.isVisible" title="新建作业" ok-text="确认" cancel-text="取消" @ok="onJobAddModalOk">
        <a-form-model ref="ruleForm" :model="jobAddModal.data" :rules="jobAddModal.rules" :label-col="jobAddModal.labelCol" :wrapper-col="jobAddModal.wrapperCol">
          <a-form-model-item label="作业名称" prop="name">
            <a-input v-model="jobAddModal.data.name" />
          </a-form-model-item>
          <a-form-model-item label="作业类型" prop="type">
            <a-select v-model="jobAddModal.data.type">
              <a-select-option v-for="(item, index) in helper.jobTypeList" :key="index" :value="item.value" :disabled="!item.enable">
                {{ item.desc }}
              </a-select-option>
            </a-select>
          </a-form-model-item>
          <a-form-model-item label="作业描述">
            <a-textarea v-model="jobAddModal.data.description" />
          </a-form-model-item>
        </a-form-model>
      </a-modal>
    </div>
  </div>
</template>
<script>
import * as jobApi from "@/api/job";
import * as helperApi from "@/api/helper";
import * as utils from "@/utils/utils";
export default {
  name: "JobList",
  data() {
    return {
      // DataFilter
      dataFilter: {
        id: null,
        name: "",
        type: "",
        lastStatus: "",
        pageNum: 1,
        pageSize: 10
      },

      // Data Table List
      dataColumns: [
        {
          key: "id",
          title: "ID",
          dataIndex: "id",
          width: 80
        },
        {
          title: "名称",
          dataIndex: "name",
          width: 200,
          scopedSlots: { customRender: "name" }
        },
        {
          title: "类型",
          dataIndex: "typeDesc",
          width: 100
        },
        {
          title: "开始时间",
          dataIndex: "createTime",
          width: 170
        },
        {
          title: "结束时间",
          dataIndex: "updateTime",
          width: 170
        },
        {
          title: "Flink UI",
          dataIndex: "lastAppId",
          width: 280
        },
        {
          title: "状态",
          dataIndex: "lastStatusDesc",
          width: 100,
          align: "center",
          fixed: "right",
          scopedSlots: { customRender: "lastStatusDesc" }
        },
        {
          title: "操作",
          width: 130,
          align: "center",
          fixed: "right",
          scopedSlots: { customRender: "action" }
        }
      ],
      dataList: [],
      dataTableSelectedRowKeys: [],
      isLoading: false,

      // Bottom Tool
      page: {
        total: 0
      },

      // helper
      helper: {
        jobTypeList: [],
        instStatusList: [],
        isDisabledStartJobListButton: true,
        isDisabledRestartJobListButton: true,
        isDisabledStopJobListButton: true,
        isDisabledDeleteJobListButton: true
      },

      // jobAddModal
      jobAddModal: {
        isVisible: false,
        labelCol: { span: 4 },
        wrapperCol: { span: 12 },
        data: {
          name: "",
          type: 1,
          description: ""
        },
        rules: {
          name: [{ required: true, message: "请输入作业名！", trigger: "blur" }]
        }
      }
    };
  },
  methods: {
    onQuery() {
      this.getDataList();
    },
    onAdd() {
      this.jobAddModal.isVisible = true;
    },
    onJobAddModalOk() {
      // this.jobAddModal.isVisible = false;
      this.$refs.ruleForm.validate(valid => {
        if (valid) {
          jobApi.addJob(this.jobAddModal.data).then(resp => {
            this.$router.push({
              path: "/page/job/jobEdit",
              query: {
                type: resp.data.type,
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
    onEdit(row) {
      this.$router.push({
        path: "/page/job/jobEdit",
        query: {
          jobId: row.id
        }
      });
    },
    onGoBack() {
      this.$router.go(-1);
    },
    onDataTableSelectedChange(selectedRowKeys, selectedRows) {
      this.dataTableSelectedRowKeys = selectedRowKeys;

      if (selectedRowKeys.length === 0) {
        this.helper.isDisabledStartJobListButton = true;
        this.helper.isDisabledRestartJobListButton = true;
        this.helper.isDisabledStopJobListButton = true;
        this.helper.isDisabledDeleteJobListButton = true;
        return;
      }

      let finalStart = true;
      let finalRestart = true;
      let finalStop = true;
      let finalDelete = true;
      selectedRows.forEach(row => {
        finalStart = finalStart && row.authMap.start;
        finalRestart = finalRestart && row.authMap.restart;
        finalStop = finalStop && row.authMap.stop;
        finalDelete = finalDelete && row.authMap.delete;
      });
      this.helper.isDisabledStartJobListButton = !finalStart;
      this.helper.isDisabledRestartJobListButton = !finalRestart;
      this.helper.isDisabledStopJobListButton = !finalStop;
      this.helper.isDisabledDeleteJobListButton = !finalDelete;
    },
    // eslint-disable-next-line no-unused-vars
    onPageNumChange(pageNum, pageSize) {
      this.dataFilter.pageNum = pageNum;
      this.getDataList();
    },
    onPageSizeChange(pageNum, pageSize) {
      this.dataFilter.pageNum = pageNum;
      this.dataFilter.pageSize = pageSize;
      this.getDataList();
    },
    getDataList() {
      this.isLoading = true;
      jobApi.getJobPageList(utils.objectDeleteBlankVK(this.dataFilter)).then(resp => {
        this.dataList = resp.data.list;
        this.page.total = resp.data.total;
        this.isLoading = false;
      });
    },
    onStartJobList() {
      jobApi.startJobList(this.dataTableSelectedRowKeys).then(() => {
        this.$Notice.success({
          title: "启动多个作业成功！",
          duration: 1
        });

        this.getDataList();
      });
    },
    onRestartJobList() {
      jobApi.restartJobList(this.dataTableSelectedRowKeys).then(() => {
        this.$Notice.success({
          title: "重启多个作业成功！",
          duration: 1
        });

        this.getDataList();
      });
    },
    onStopJobList() {
      jobApi.stopJobList(this.dataTableSelectedRowKeys).then(() => {
        this.$Notice.success({
          title: "停止多个作业成功！",
          duration: 1
        });

        this.getDataList();
      });
    },
    onDeleteJobList() {
      jobApi.deleteJobList(this.dataTableSelectedRowKeys).then(() => {
        this.getDataList();
        this.$Notice.success({
          title: "删除多个作业成功！",
          duration: 1
        });

        this.getDataList();
      });
    },
    /* Helper */
    initHelper() {
      helperApi.getJobTypeList().then(resp => {
        this.helper.jobTypeList = resp.data;
      });
      helperApi.getInstStatusList().then(resp => {
        this.helper.instStatusList = resp.data;
      });
    }
  },
  created() {
    this.initHelper();
    this.getDataList();
  }
};
</script>
<style scoped>
.filter-tool {
  margin-left: 5px;
}
</style>
