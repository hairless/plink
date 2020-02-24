<template>
  <div>
    <!-- Job Query-->
    <div style="margin-bottom: 10px; padding: 5px; background: #f8f8f9">
      <Row :gutter="10">
        <Col span="20">
          <span> ID : </span>
          <Input
            v-model="jobListFilter.id"
            placeholder=""
            style="width: 80px"
            size="small"
            clearable
          />
          <span> 名称 : </span>
          <Input
            v-model="jobListFilter.name"
            placeholder=""
            style="width: 200px"
            size="small"
            clearable
          />
          <span> 类型 : </span>
          <Select
            v-model="jobListFilter.type"
            size="small"
            style="width: 120px"
            clearable
          >
            <Option
              v-for="item in hintJobTypeEnum"
              :value="item.value"
              :key="item.value"
              >{{ item.desc }}</Option
            >
          </Select>
          <span> 状态 : </span>
          <Select
            v-model="jobListFilter.lastStatus"
            size="small"
            style="width: 120px"
            clearable
          >
            <Option
              v-for="item in hintJobInstanceStatusEnum"
              :value="item.value"
              :key="item.value"
              >{{ item.desc }}</Option
            >
          </Select>
        </Col>
        <Col span="4" align="right">
          <Button
            type="primary"
            size="small"
            style="margin-right: 10px;"
            @click="clickQuery"
            >查询</Button
          >
          <Button type="success" size="small" @click="clickCreate">新建</Button>
        </Col>
      </Row>
    </div>
    <!-- Job List -->
    <div>
      <Table
        stripe
        ref="selection"
        :columns="jobListColumns"
        :data="jobList"
        @on-select-all-cancel="handleSelectAllCancel"
        @on-select-all="handleSelectAll"
        @on-select="handleSelect"
        @on-select-cancel="handleSelectCancel"
      >
        <template slot-scope="{ row }" slot="lastStatus">
          <ButtonJobStatus
            size="small"
            :status="row.lastStatus"
            :text="row.lastStatusDesc"
            v-if="row.lastStatusDesc"
          />
        </template>
        <template slot-scope="{ row }" slot="operator">
          <div>
            <Button type="info" size="small" @click="clickDetail(row)"
              >详情</Button
            >
            <Button
              type="info"
              size="small"
              style="margin-left: 10px;"
              @click="clickEdit(row)"
              >编辑</Button
            >
          </div>
        </template>
      </Table>
    </div>
    <!-- Job List Tools -->
    <div style="margin-top: 10px; padding: 5px; background: #f8f8f9">
      <Row :gutter="10">
        <Col span="8">
          <Button
            type="success"
            size="small"
            :disabled="multiSelectJobIds.length === 0"
            @click="clickStartJobs"
            >启动</Button
          >
          <Button
            type="warning"
            size="small"
            style="margin-left: 10px;"
            :disabled="multiSelectJobIds.length === 0"
            @click="clickRestartJobs"
            >重启</Button
          >
          <Button
            type="error"
            size="small"
            style="margin-left: 10px;"
            :disabled="multiSelectJobIds.length === 0"
            @click="clickStopJobs"
            >停止</Button
          >
          <Button
            type="error"
            size="small"
            style="margin-left: 10px;"
            :disabled="multiSelectJobIds.length === 0"
            @click="clickDeleteJobs"
            >删除</Button
          >
        </Col>
        <Col span="16" align="right">
          <Page
            :total="jobListFilterPage.total"
            :current="jobListFilter.pageNum"
            :page-size="jobListFilter.pageSize"
            :page-size-opts="[5, 10, 20, 50, 100]"
            @on-change="pageChange"
            @on-page-size-change="pageSizeChange"
            show-total
            show-sizer
            show-elevator
            size="small"
          />
        </Col>
      </Row>
    </div>
    <!-- Job Create -->
    <div>
      <Modal
        v-model="jobCreateModal"
        title="新建作业"
        @on-ok="clickCreateOk"
        @on-cancel="clickCreateCancel"
        draggable
        ok-text="新建"
      >
        <Form :model="jobCreateItems" :label-width="80">
          <FormItem label="作业名称 :">
            <Input v-model="jobCreateItems.name" placeholder="" />
          </FormItem>
          <FormItem label="作业类型 : ">
            <Select v-model="jobCreateItems.type">
              <Option
                v-for="item in hintJobTypeEnum"
                :value="item.value"
                :key="item.value"
                >{{ item.desc }}</Option
              >
            </Select>
          </FormItem>
          <FormItem label="作业描述 :">
            <Input
              type="textarea"
              v-model="jobCreateItems.description"
              placeholder=""
              :rows="4"
            />
          </FormItem>
        </Form>
      </Modal>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import jobApi from "@/api/jobApi";
import enumApi from "@/api/enumApi";
import { IJob } from "@/model/jobModel";
import date from "@/utils/date";
import collection from "@/utils/collection";
import ButtonJobStatus from "./ButtonJobStatus.vue";
// @ts-ignore
import tqs from "@/utils/tqs";

@Component({
  components: {
    ButtonJobStatus
  }
})
export default class JobList extends Vue {
  // hint
  hintJobTypeEnum: any[] = [];
  hintJobInstanceStatusEnum: any[] = [];
  // Job Props
  jobListColumns: object[] = [
    /*{
      type: "expand",
      key: "id",
      width: 30
    },*/
    {
      type: "selection",
      title: "ID",
      key: "id",
      width: 50
    },
    {
      title: "ID",
      key: "id",
      align: "center",
      width: 100
    },
    {
      title: "名称",
      key: "name",
      align: "center"
    },
    {
      title: "类型",
      key: "typeDesc",
      align: "center"
    },
    /*{
      title: "描述",
      key: "description",
      align: "center"
    },*/
    {
      title: "开始时间",
      key: "lastStartTime",
      align: "center",
      render: function(h: any, params: any) {
        return h("div", date.dateFormat(params.row.lastStartTime));
      }
    },
    {
      title: "结束时间",
      key: "lastStopTime",
      align: "center",
      render: function(h: any, params: any) {
        return h("div", date.dateFormat(params.row.lastStopTime));
      }
    },
    {
      title: "状态",
      key: "lastStatus",
      align: "center",
      slot: "lastStatus"
    },
    {
      title: "操作",
      fixed: "right",
      align: "center",
      slot: "operator",
      width: 140
    }
  ];
  jobList: IJob[] = [];
  // Job Query
  jobListFilter: any = {
    id: null,
    name: null,
    type: null,
    lastStatus: null,
    pageNum: 1,
    pageSize: 10
  };
  jobListFilterPage: any = {
    total: null
  };
  clickQuery() {
    history.pushState(
      {},
      "",
      window.location.pathname +
        "?" +
        tqs.stringifyExcludedBlank(this.jobListFilter)
    );
    this.getJobList();
  }
  pageChange(num: number) {
    this.jobListFilter.pageNum = num;
    this.getJobList();
  }
  pageSizeChange(size: number) {
    this.jobListFilter.pageSize = size;
    this.getJobList();
  }
  // Job Create
  jobCreateModal: boolean = false;
  jobCreateItems: IJob = {
    name: "name",
    type: 1,
    description: "desc"
  };
  clickCreate() {
    this.jobCreateModal = true;
  }
  clickCreateOk() {
    jobApi
      .addJob(this.jobCreateItems)
      .then(res => {
        this.getJobList();
      })
      .then(res => {
        this.$Notice.success({
          title: "新建作业成功"
        });
      });
  }
  clickCreateCancel() {
    this.$Notice.warning({
      title: "取消新建作业"
    });
  }
  // Job Click Actions
  clickDetail(row: any) {
    this.$router.push({
      path: "/page/job/detail",
      query: {
        id: row.id
      }
    });
  }
  clickEdit(row: any) {
    this.$router.push({
      path: "/page/job/edit",
      query: {
        id: row.id
      }
    });
  }
  // Multi Select
  multiSelectJobIds: any[] = [];
  handleSelectAllCancel(selection: any[]) {
    this.multiSelectJobIds = [];
  }
  handleSelectAll(selection: any[]) {
    this.multiSelectJobIds = selection.map(row => {
      return row.id;
    });
  }
  handleSelect(selection: any[], row: any) {
    this.multiSelectJobIds.push(row.id);
  }
  handleSelectCancel(selection: any[], row: any) {
    this.multiSelectJobIds.splice(this.multiSelectJobIds.indexOf(row.id), 1);
  }
  clickStartJobs() {
    jobApi
      .startJobs({ idList: this.multiSelectJobIds })
      .then(res => {
        this.$Notice.success({ title: "启动作业成功" });
      })
      .catch(res => {
        this.$Notice.error({ title: "启动作业失败", desc: res.msg });
      });
  }
  clickRestartJobs() {
    jobApi
      .restartJobs({ idList: this.multiSelectJobIds })
      .then(res => {
        this.$Notice.success({ title: "重启作业成功" });
      })
      .catch(res => {
        this.$Notice.error({ title: "重启作业失败", desc: res.msg });
      });
  }
  clickStopJobs() {
    jobApi
      .stopJobs({ idList: this.multiSelectJobIds })
      .then(res => {
        this.$Notice.success({ title: "停止作业成功" });
      })
      .catch(res => {
        this.$Notice.error({ title: "停止作业失败", desc: res.msg });
      });
  }
  clickDeleteJobs() {
    jobApi
      .deleteJobs({ idList: this.multiSelectJobIds })
      .then(res => {
        this.getJobList();
        this.handleSelectAllCancel(this.multiSelectJobIds);
        this.$Notice.success({ title: "删除作业成功" });
      })
      .catch(res => {
        this.$Notice.error({ title: "删除作业失败", desc: res.msg });
      });
  }
  getJobList() {
    jobApi
      .queryJobs(collection.mapDeleteBlankVK(this.jobListFilter))
      .then((res: any) => {
        this.jobList = res.list;
        this.jobListFilterPage.total = res.total;
      })
      .catch(err => {
        this.$Notice.error({ title: err.msg });
      });
  }
  getEnums() {
    enumApi.jobType().then((res: any) => {
      this.hintJobTypeEnum = res;
    });
    enumApi.jobInstanceStatus().then((res: any) => {
      this.hintJobInstanceStatusEnum = res;
    });
  }
  parseRouter() {
    this.jobListFilter = collection.mapLeftJoinCopy(
      this.jobListFilter,
      this.$route.query
    );
  }

  mounted() {
    this.parseRouter();
    this.getJobList();
    this.getEnums();
  }
}
</script>

<style scoped lang="scss"></style>
