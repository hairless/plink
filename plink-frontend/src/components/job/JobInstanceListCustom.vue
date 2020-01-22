<template>
  <div>
    <!-- JobInstance Query-->
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
        </Col>
      </Row>
    </div>
    <!-- JobInstance List -->
    <div>
      <Table
        stripe
        ref="selection"
        :columns="jobInstanceListColumns"
        :data="jobInstanceList"
      >
      </Table>
    </div>
    <!-- JobInstance List Tools -->
    <div style="margin-top: 10px; padding: 5px; background: #f8f8f9">
      <Row :gutter="10">
        <Col align="right">
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
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import { IJobInstance } from "@/model/jobInstanceModel";
import jobInstanceApi from "@/api/jobInstanceApi";

@Component
export default class JobList extends Vue {
  // hint
  hintJobTypeList: object[] = [{ value: 1, label: "自定义 / Jar" }];
  // Job Props
  jobInstanceListColumns: object[] = [
    {
      type: "expand",
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
      title: "创建时间",
      key: "createTime",
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
      key: "status",
      align: "center"
    }
  ];
  jobInstanceList: IJobInstance[] = [];
  // Job Query
  jobQueryCondition: any = {
    id: "",
    name: "",
    total: 100,
    pageNum: 1,
    pageSize: 10
  };
  clickQuery() {
    this.getJobInstanceList();
  }
  pageChange(num: number) {
    this.jobQueryCondition.pageNum = num;
    this.getJobInstanceList();
  }
  pageSizeChange(size: number) {
    this.jobQueryCondition.pageSize = size;
    this.getJobInstanceList();
  }

  getJobInstanceList() {
    jobInstanceApi
      .queryJobInstances({
        id: this.jobQueryCondition.id,
        name:
          this.jobQueryCondition.name === ""
            ? null
            : this.jobQueryCondition.name,
        pageNum: this.jobQueryCondition.pageNum,
        pageSize: this.jobQueryCondition.pageSize
      })
      .then((res: any) => {
        this.jobInstanceList = res.list;
        this.jobQueryCondition.total = res.total;
      })
      .catch(err => {
        this.$Notice.error({ title: err.msg });
      });
  }

  mounted() {
    this.getJobInstanceList();
  }
}
</script>

<style scoped lang="scss"></style>
