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
              <a-input v-model="dataFilter.userName" placeholder="eg. 测试作业" style="width: 200px" size="small" allowClear />
            </span>
            <span style="margin-left: 10px"
              >类型 :
              <a-input v-model="dataFilter.nickName" placeholder="eg. " style="width: 200px" size="small" allowClear />
            </span>
            <span style="margin-left: 10px"
              >状态 :
              <a-input v-model="dataFilter.email" placeholder="eg. " style="width: 250px" size="small" allowClear />
            </span>
          </div>
        </a-col>
        <a-col class="gutter-row" :span="4" align="right">
          <div class="gutter-box">
            <a-button type="primary" size="small" class="filter-tool" @click="onQuery">查询</a-button>
            <a-button type="primary" size="small" class="filter-tool" @click="onAdd">增加</a-button>
            <a-button type="primary" size="small" class="filter-tool" @click="onSync" :disabled="dataFilter.userName.length <= 0">同步</a-button>
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
        :scroll="{ x: 1300 }"
        :row-selection="{
          selectedRowKeys: dataTableSelectedRowKeys,
          onChange: onDataTableSelectedChange
        }"
      >
        <span slot="userGroupList" slot-scope="row">
          <a-tag color="blue" v-for="item in row" :key="item.id">
            {{ item.groupName }}
          </a-tag>
        </span>
        <span slot="action" slot-scope="row">
          <router-link :to="{ name: 'UmcUserDetail', query: { userId: row.id } }">详情</router-link>
          <a-divider type="vertical" />
          <router-link :to="{ name: 'UmcUserEdit', query: { userId: row.id } }" @click="onEdit(row)">编辑</router-link>
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
              <a-button type="primary" size="small" class="filter-tool" disabled @click="onDisableList">禁用</a-button>
              <a-button type="danger" size="small" class="filter-tool" :disabled="dataTableSelectedRowKeys.length <= 0" @click="onDeleteList">删除</a-button>
              <a-button type="primary" size="small" class="filter-tool" disabled @click="onDeleteList">同步</a-button>
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
import * as jobApi from "@/api/job";
import * as utils from "@/utils/utils";
export default {
  name: "JobList",
  data() {
    return {
      // DataFilter
      dataFilter: {
        id: null,
        userName: "",
        nickName: "",
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
          dataIndex: "userName",
          width: 200
        },
        {
          title: "类型",
          dataIndex: "nickName",
          width: 200
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
          dataIndex: "updateTime",
          width: 150
        },
        {
          title: "状态",
          dataIndex: "updateTime",
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
          userId: row.id
        }
      });
    },
    onSync() {
      //
    },
    onGoBack() {
      //
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
      jobApi.getJobPageList(utils.objectDeleteBlankVK(this.dataFilter)).then(resp => {
        this.dataList = resp.data.list;
        this.page.total = resp.data.total;
      });
    },
    onDisableList() {},
    onDeleteList() {
      jobApi.deleteJobList(this.dataTableSelectedRowKeys).then(() => {
        this.getDataList();
        this.$Notice.success({
          title: "删除多个用户成功！",
          duration: 1
        });
      });
    }
  },
  created() {
    this.getDataList();
  }
};
</script>
<style scoped>
.filter-tool {
  margin-left: 5px;
}
</style>
