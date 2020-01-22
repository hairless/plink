<template>
  <div>
    <!-- Job Query-->
    <div style="margin-bottom: 10px; padding: 5px; background: #f8f8f9">
      <Row :gutter="10">
        <Col span="20">
          <span> ID : </span>
          <Input
            v-model="jobQueryCondition.id"
            placeholder=""
            style="width: 80px"
            size="small"
            clearable
          />
          <span> 名称 : </span>
          <Input
            v-model="jobQueryCondition.name"
            placeholder=""
            style="width: 200px"
            size="small"
            clearable
          />
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
            :total="jobQueryCondition.total"
            :current="jobQueryCondition.pageNum"
            :page-size="jobQueryCondition.pageSize"
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
                v-for="item in hintJobTypeList"
                :value="item.value"
                :key="item.value"
                >{{ item.label }}</Option
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
import { IJob } from "@/model/jobModel";

@Component
export default class JobList extends Vue {
  // hint
  hintJobTypeList: object[] = [{ value: 1, label: "自定义 / Jar" }];
  // Job Props
  jobListColumns: object[] = [
    {
      type: "expand",
      key: "id",
      width: 50
    },
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
      key: "type",
      align: "center"
    },
    {
      title: "描述",
      key: "description",
      align: "center"
    },
    {
      title: "开始时间",
      key: "startTime",
      align: "center"
    },
    {
      title: "结束时间",
      key: "stopTime",
      align: "center"
    },
    {
      title: "状态",
      key: "last_status",
      align: "center"
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
  jobQueryCondition: any = {
    id: "",
    name: "",
    total: 100,
    pageNum: 1,
    pageSize: 10
  };
  clickQuery() {
    this.getJobList();
  }
  pageChange(num: number) {
    this.jobQueryCondition.pageNum = num;
    this.getJobList();
  }
  pageSizeChange(size: number) {
    this.jobQueryCondition.pageSize = size;
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
      .queryJobs({
        id: this.jobQueryCondition.id,
        name:
          this.jobQueryCondition.name === ""
            ? null
            : this.jobQueryCondition.name,
        pageNum: this.jobQueryCondition.pageNum,
        pageSize: this.jobQueryCondition.pageSize
      })
      .then((res: any) => {
        this.jobList = res.list;
        this.jobQueryCondition.total = res.total;
      })
      .catch(err => {
        this.$Notice.error({ title: err.msg });
      });
  }

  mounted() {
    this.getJobList();
  }
}
</script>

<style scoped lang="scss"></style>
