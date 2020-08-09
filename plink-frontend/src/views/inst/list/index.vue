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
              <a-select v-model="dataFilter.status" style="width: 100px" size="small" allowClear>
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
        :columns="dataColumnList"
        :data-source="dataList"
        :rowKey="item => item.id"
        :loading="isLoading"
        :pagination="false"
        :scroll="{ x: 1300 }"
        :row-selection="{
          selectedRowKeys: dataTableSelectedRowKeys,
          onChange: onDataTableSelectedChange
        }"
      >
        <span slot="jobName" slot-scope="current, row">
          {{ row.job.name }}
        </span>
        <span slot="jobType" slot-scope="current, row">
          {{ row.job.typeDesc }}
        </span>
        <span slot="appId" slot-scope="current, row">
          <a :href="row.uiAddress" target="_blank">{{ current }}</a>
        </span>
        <span slot="action" slot-scope="row">
          <router-link :to="{ name: 'JobDetail', query: { id: row.id } }">详情</router-link>
          <a-divider type="vertical" />
          <router-link :to="{ name: 'JobEdit', query: { id: row.id } }" @click="onEdit(row)">编辑</router-link>
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
              <a-button type="primary" size="small" class="filter-tool" disabled @click="onStartJobList">启动</a-button>
              <a-button type="danger" size="small" class="filter-tool" disabled @click="onRestartJobList">重启</a-button>
              <a-button type="primary" size="small" class="filter-tool" disabled @click="onStopJobList">停止</a-button>
              <a-button type="primary" size="small" class="filter-tool" disabled @click="onDeleteJobList">删除</a-button>
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
  </div>
</template>
<script>
import * as instApi from "@/api/inst";
import * as helperApi from "@/api/helper";
import * as utils from "@/utils/utils";
export default {
  name: "InstList",
  props: {
    jobId: null
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
          fixed: "right"
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
        instStatusList: []
      }
    };
  },
  methods: {
    onQuery() {
      this.getDataList();
    },
    onAdd() {
      this.$router.push({
        path: "/job/jobAdd"
      });
    },
    onEdit(row) {
      this.$router.push({
        path: "/job/jobEdit",
        query: {
          id: row.id
        }
      });
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
      instApi.getInstPageList(utils.objectDeleteBlankVK(this.dataFilter)).then(resp => {
        this.dataList = resp.data.list;
        this.page.total = resp.data.total;
      });
    },
    onStartJobList() {
      //
    },
    onRestartJobList() {
      //
    },
    onStopJobList() {
      //
    },
    onDeleteJobList() {
      //
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
