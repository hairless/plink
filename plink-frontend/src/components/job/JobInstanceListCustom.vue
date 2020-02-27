<template>
  <div>
    <!-- JobInstance Query-->
    <div style="margin-bottom: 10px; padding: 5px; background: #f8f8f9">
      <Row :gutter="10">
        <Col span="22">
          <span> ID : </span>
          <Input
            v-model="jobInstanceListFilter.id"
            placeholder=""
            style="width: 80px"
            size="small"
            clearable
          />
          <!--<span> 名称 : </span>
          <Input
            v-model="jobInstanceListFilter.name"
            placeholder=""
            style="width: 200px"
            size="small"
            clearable
          />
          <span> 类型 : </span>
          <Select
            v-model="jobInstanceListFilter.type"
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
          </Select>-->
          <span> 状态 : </span>
          <Select
            v-model="jobInstanceListFilter.status"
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
        <Col span="2" align="right">
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
        <template slot-scope="{ row }" slot="status">
          <ButtonJobStatus :status="row.status" :text="row.statusDesc" />
        </template>
      </Table>
    </div>
    <!-- JobInstance List Tools -->
    <div style="margin-top: 10px; padding: 5px; background: #f8f8f9">
      <Row :gutter="10">
        <Col align="right">
          <Page
            :total="jobInstanceListFilterPage.total"
            :current="jobInstanceListFilter.pageNum"
            :page-size="jobInstanceListFilter.pageSize"
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
import enumApi from "@/api/enumApi";
import date from "@/utils/date";
import collection from "@/utils/collection";
import ButtonJobStatus from "@/components/job/ButtonJobStatus.vue";
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
  jobInstanceListColumns: object[] = [
    /*{
      type: "expand",
      key: "id",
      width: 50
    },*/
    {
      title: "ID",
      key: "id",
      align: "center",
      width: 100
    },
    {
      title: "名称",
      align: "center",
      render: function(h: any, params: any) {
        return h("div", params.row.job.name);
      }
    },
    {
      title: "类型",
      align: "center",
      render: function(h: any, params: any) {
        return h("div", params.row.job.typeDesc);
      }
    },
    {
      title: "创建时间",
      key: "createTime",
      align: "center",
      render: function(h: any, params: any) {
        return h("div", date.dateFormat(params.row.createTime));
      }
    },
    {
      title: "开始时间",
      key: "startTime",
      align: "center",
      render: function(h: any, params: any) {
        return h("div", date.dateFormat(params.row.startTime));
      }
    },
    {
      title: "结束时间",
      key: "stopTime",
      align: "center",
      render: function(h: any, params: any) {
        return h("div", date.dateFormat(params.row.stopTime));
      }
    },
    {
      title: "状态",
      key: "status",
      align: "center",
      slot: "status"
    }
  ];
  jobInstanceList: IJobInstance[] = [];
  jobInstanceListFilter: any = {
    id: null,
    /*name: null,
    type: null,*/
    status: null,
    pageNum: 1,
    pageSize: 10
  };
  jobInstanceListFilterPage: any = {
    total: null
  };
  clickQuery() {
    history.pushState(
      {},
      "",
      window.location.pathname +
        "?" +
        tqs.stringifyExcludedBlank(this.jobInstanceListFilter)
    );
    this.getJobInstanceList();
  }
  pageChange(num: number) {
    this.jobInstanceListFilter.pageNum = num;
    this.getJobInstanceList();
  }
  pageSizeChange(size: number) {
    this.jobInstanceListFilter.pageSize = size;
    this.getJobInstanceList();
  }

  getJobInstanceList() {
    console.log(collection.mapDeleteBlankVK(this.jobInstanceListFilter));
    jobInstanceApi
      .queryJobInstances(
        collection.mapDeleteBlankVK(this.jobInstanceListFilter)
      )
      .then((res: any) => {
        this.jobInstanceList = res.list;
        this.jobInstanceListFilterPage.total = res.total;
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
    this.jobInstanceListFilter = collection.mapLeftJoinCopy(
      this.jobInstanceListFilter,
      this.$route.query
    );
    console.log(JSON.stringify(this.jobInstanceListFilter));
  }
  mounted() {
    this.parseRouter();
    this.getEnums();
    this.getJobInstanceList();
  }
}
</script>

<style scoped lang="scss"></style>
