<template>
  <div>
    <!-- Data Filter -->
    <div style="margin-bottom: 10px; padding: 5px; background-image: linear-gradient(100deg, rgba(60, 213, 255, 0.5), rgba(60, 213, 255, 0.3));">
      <a-row :gutter="16">
        <a-col class="gutter-row" :span="14" style="padding-left: 20px">
          <div class="gutter-box">
            <span
              >ID :
              <a-input v-model="dataFilter.id" placeholder="eg. 1001" style="width: 100px" size="small" allowClear />
            </span>
            <!--<span style="margin-left: 10px"
              >名称 :
              <a-input v-model="dataFilter.name" placeholder="eg. 测试作业" style="width: 200px" size="small" allowClear />
            </span>-->
            <!--<span style="margin-left: 10px"
              >类型 :
              <a-select v-model="dataFilter.type" style="width: 100px" size="small" allowClear>
                <a-select-option v-for="(item, index) in helper.jobTypeList" :key="index" :value="item.value">
                  {{ item.desc }}
                </a-select-option>
              </a-select>
            </span>-->
            <span style="margin-left: 10px"
              >状态 :
              <a-select v-model="dataFilter.status" style="width: 100px" size="small" allowClear @change="getDataList">
                <a-select-option v-for="(item, index) in helper.instStatusList" :key="index" :value="item.value">
                  {{ item.desc }}
                </a-select-option>
              </a-select>
            </span>
          </div>
        </a-col>
        <a-col class="gutter-row" :span="10" align="right">
          <div class="gutter-box">
            <a-tooltip title="周期刷新">
              <a-switch checked-children="开" un-checked-children="关" v-model="helper.isAutoFlush" @change="handleDataListFlush" />
            </a-tooltip>
            <a-button type="primary" size="small" class="filter-tool" @click="onQuery">查询</a-button>
            <a-button type="primary" size="small" class="filter-tool" @click="onGoBack">返回</a-button>
          </div>
        </a-col>
      </a-row>
    </div>

    <!-- Data Table List -->
    <div>
      <a-table :columns="dataColumnList" :data-source="dataList" :rowKey="item => item.id" :loading="isLoading" :pagination="false" :scroll="{ x: 1920 }">
        <span slot="jobName" slot-scope="current, row">
          {{ row.job.name }}
        </span>
        <span slot="jobType" slot-scope="current, row">
          {{ row.job.typeDesc }}
        </span>
        <span slot="appId" slot-scope="current, row">
          <a :href="row.uiAddress" target="_blank">{{ current }}</a>
        </span>
        <span slot="statusDesc" slot-scope="current, row">
          <span :style="{ color: [3, 4, -1].includes(row.status) ? 'red' : 'green' }">{{ current }}</span>
        </span>
        <span slot="action" slot-scope="row">
          <a @click="onLog(row)">日志</a>
          <a-divider type="vertical" />
          <router-link :to="{ path: 'inst/instDetail', query: { instId: row.id } }" disabled>详情</router-link>
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
              <span style="font-size: 16px">周期刷新 : </span>
              <a-switch checked-children="开" un-checked-children="关" v-model="helper.isAutoFlush" @change="handleDataListFlush" />
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

    <!-- 实例日志的对话框 -->
    <a-modal v-model="instLog.isVisible" title="实例日志" @ok="handleInstLogModalOk" @cancel="handleInstLogModalCancel" :width="helper.clientWidth * 0.8">
      <!--      <a-textarea v-model="instLog.data" autoSize="auto" />-->
      <SqlCMEditor v-model="instLog.data" :read-only="true" :auto-scroll-to-bottom-on-changes="true" />
    </a-modal>
  </div>
</template>
<script>
import * as instApi from "@/api/inst";
import * as helperApi from "@/api/helper";
import * as utils from "@/utils/utils";
import SqlCMEditor from "@/components/SqlCMEditor";
export default {
  components: {
    SqlCMEditor
  },
  name: "InstList",
  props: {
    jobId: {
      default: null
    },
    isAutoFlush: {
      default: false
    }
  },
  data() {
    return {
      // DataFilter
      dataFilter: {
        id: null,
        jobId: this.jobId,
        status: "",
        pageNum: 1,
        pageSize: 10
      },

      // Data Table List
      dataColumnList: [
        {
          title: "ID",
          dataIndex: "id",
          width: 80
        },
        {
          title: "名称",
          width: 200,
          scopedSlots: { customRender: "jobName" }
        },
        {
          title: "类型",
          width: 100,
          scopedSlots: { customRender: "jobType" }
        },
        {
          title: "创建时间",
          dataIndex: "createTime",
          width: 170
        },
        {
          title: "开始时间",
          dataIndex: "startTime",
          width: 170
        },
        {
          title: "结束时间",
          dataIndex: "stopTime",
          width: 170
        },
        {
          title: "Flink UI",
          dataIndex: "appId",
          width: 280,
          scopedSlots: { customRender: "appId" }
        },
        {
          title: "状态",
          dataIndex: "statusDesc",
          width: 100,
          align: "center",
          fixed: "right",
          scopedSlots: { customRender: "statusDesc" }
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

      instLog: {
        isVisible: false,
        data: "",
        timer: null
      },

      // Bottom Tool
      page: {
        total: 0
      },
      dataListTimer: null,

      // helper
      helper: {
        jobTypeList: [],
        instStatusList: [],
        isAutoFlush: this.isAutoFlush,
        clientWidth: document.documentElement.clientWidth
      }
    };
  },
  methods: {
    onQuery() {
      this.getDataList();
    },
    onEdit() {
      //
    },
    onGoBack() {
      this.$router.go(-1);
    },
    onDataTableSelectedChange(selectedRowKeys) {
      this.dataTableSelectedRowKeys = selectedRowKeys;
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
      instApi.getInstPageList(utils.objectDeleteBlankVK(this.dataFilter)).then(resp => {
        this.isLoading = false;
        this.dataList = resp.data.list;
        this.page.total = resp.data.total;

        // 获取实例列表中最新的实例，不是最终状态就开启周期刷新，否则关闭
        let lastInst = this.dataList[0];
        if (lastInst) {
          // 3:启动失败，4:运行失败，5:已停止，6:运行成功
          if ([3, 4, 5, 6].includes(lastInst.status)) {
            // 关闭刷新
            this.handleDataListFlush(false);
          } else {
            // 开启刷新
            this.helper.isAutoFlush = true;
            this.handleDataListFlush(true);
          }
        }
      });
    },
    handleInstLogModalOk() {
      this.instLog.isVisible = false;
      this.clearInstLogTimer();
    },
    handleInstLogModalCancel() {
      this.clearInstLogTimer();
    },
    /* 定时器自动刷新 */
    handleDataListFlush(checked) {
      if (checked) {
        if (!this.dataListTimer) {
          this.dataListTimer = this.getDataListTimer();
        }
      } else {
        this.clearDataListTimer();
      }
    },
    getDataListTimer() {
      return setInterval(() => {
        this.getDataList();
      }, 1000);
    },
    clearDataListTimer() {
      clearInterval(this.dataListTimer);
      this.dataListTimer = null;
      this.helper.isAutoFlush = false;
    },
    handleInstLogFlush(checked, row) {
      if (checked) {
        if (!this.instLog.timer) {
          this.instLog.timer = this.getInstLogTimer(row);
        }
      } else {
        this.clearInstLogTimer();
      }
    },
    getInstLogTimer(row) {
      return setInterval(() => {
        this.getInstLog(row);
      }, 1000);
    },
    clearInstLogTimer() {
      clearInterval(this.instLog.timer);
      this.instLog.timer = null;
    },
    getInstLog(row) {
      instApi.getInstLog(row.id).then(resp => {
        this.instLog.data = resp.data;
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
    },
    onLog(row) {
      this.instLog.data = "";
      this.instLog.isVisible = true;

      // 周期刷新
      this.handleInstLogFlush(true, row);
    }
  },
  beforeDestroy() {
    this.clearDataListTimer();
    this.clearInstLogTimer();
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
