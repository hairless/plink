<template>
  <div>
    <!-- Job Detail -->
    <Tabs>
      <TabPane label="作业信息" name="basic">
        <Divider>基础信息</Divider>
        <Row :gutter="10">
          <Col span="12">作业 ID : {{ job.id }}</Col>
          <Col span="12">作业名称 : {{ job.name }}</Col>
        </Row>
        <Row :gutter="10">
          <Col span="12">作业类型 : {{ job.typeDesc }}</Col>
          <Col span="12">作业描述 : {{ job.description }}</Col>
        </Row>
        <Row :gutter="10">
          <Col span="12">创建时间 : {{ job.createTime | dateFormat }}</Col>
          <Col span="12">更新时间 : {{ job.updateTime | dateFormat }}</Col>
        </Row>
        <Row :gutter="10">
          <Col span="12">开始时间 : {{ job.lastStartTime | dateFormat }}</Col>
          <Col span="12">结束时间 : {{ job.lastStopTime | dateFormat }}</Col>
        </Row>
        <Divider>作业配置</Divider>
        <Row :gutter="10">
          <Col span="12">客户端版本 : {{ job.clientVersion }}</Col>
          <Col span="12">执行文件 : {{ job.config.jarName }}</Col>
        </Row>
        <Row :gutter="10">
          <Col span="12">MainClass : {{ job.config.mainClass }}</Col>
          <Col span="12">程序参数 : {{ job.config.args }}</Col>
        </Row>
        <Divider>运行参数</Divider>
        <Row :gutter="10">
          <Col span="12">作业并行度 : {{ job.config.parallelism }}</Col>
          <Col span="12"></Col>
        </Row>
      </TabPane>
      <TabPane label="作业实例" name="job">
        <!-- Job List -->
        <div>
          <Table
            stripe
            ref="selection"
            :columns="jobInstanceListColumns"
            :data="jobInstanceList"
          >
            <template slot="name">
              {{ job.name }}
            </template>
            <template slot-scope="{ row }" slot="status">
              <ButtonJobStatus
                size="small"
                :status="row.status"
                :text="row.statusDesc"
              />
            </template>
            <!--<template slot="operator">
              <div>
                <Button type="info" size="small">
                  详情
                </Button>
              </div>
            </template>-->
          </Table>
        </div>
        <!-- Job List Tools -->
        <div style="margin-top: 10px; padding: 5px; background: #f8f8f9">
          <Row :gutter="10">
            <!--<Col span="8">
              <Button type="error" size="small" style="margin-left: 10px;" @click="clickDelete">删除</Button>
            </Col>-->
            <Col align="right">
              <Page
                :total="jobInstanceQueryCondition.total"
                :current="jobInstanceQueryCondition.pageNum"
                :page-size="jobInstanceQueryCondition.pageSize"
                :page-size-opts="[5, 10, 20, 50, 100]"
                @on-change="jobInstancePageChange"
                @on-page-size-change="jobInstancePageSizeChange"
                show-total
                show-sizer
                show-elevator
                size="small"
              />
            </Col>
          </Row>
        </div>
      </TabPane>
      <TabPane label="作业监控" name="monitor" disabled>
        <!-- ... -->
      </TabPane>
      <div
        slot="extra"
        style="margin-bottom: 10px; padding: 5px; background: #f8f8f9"
      >
        <Button
          type="success"
          size="small"
          style="margin-right: 10px"
          @click="clickStart"
          :disabled="job.authMap && !job.authMap.start"
          >启动</Button
        >
        <Button
          type="warning"
          size="small"
          style="margin-right: 10px"
          @click="clickRestart"
          :disabled="job.authMap && !job.authMap.restart"
          >重启</Button
        >
        <Button
          type="error"
          size="small"
          style="margin-right: 10px"
          @click="clickStop"
          :disabled="job.authMap && !job.authMap.stop"
          >停止</Button
        >
        <Button
          type="error"
          size="small"
          style="margin-right: 10px"
          @click="clickDelete"
          :disabled="job.authMap && !job.authMap.delete"
          >删除</Button
        >
        <Button
          type="info"
          size="small"
          style="margin-right: 10px"
          @click="clickEdit"
          :disabled="job.authMap && !job.authMap.edit"
          >编辑</Button
        >
      </div>
    </Tabs>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Watch } from "vue-property-decorator";
import jobApi from "@/api/jobApi";
import jobInstanceApi from "@/api/jobInstanceApi";
import { IJob } from "@/model/jobModel";
import { IJobInstance } from "@/model/jobInstanceModel";
import date from "@/utils/date";
import ButtonJobStatus from "@/components/job/ButtonJobStatus.vue";
@Component({
  components: { ButtonJobStatus }
})
export default class JobDetailCustom extends Vue {
  rt: any = {
    jobId: ""
  };
  job: IJob = {
    config: {}
  };
  // Job Instance List
  jobInstanceListColumns: object[] = [
    /*{
      type: "expand",
      key: "id",
      width: 30
    },*/
    {
      title: "ID",
      key: "id",
      align: "center",
      minWidth: 50
    },
    {
      title: "名称",
      key: "name",
      align: "center",
      minWidth: 200,
      slot: "name"
    },
    {
      title: "类型",
      align: "center",
      minWidth: 150,
      render: function(h: any, params: any) {
        return h("div", params.row.job.typeDesc);
      }
    },
    {
      title: "创建时间",
      key: "createTime",
      align: "center",
      minWidth: 166,
      render: function(h: any, params: any) {
        return h("div", date.dateFormat(params.row.createTime));
      }
    },
    {
      title: "开始时间",
      key: "startTime",
      align: "center",
      minWidth: 166,
      render: function(h: any, params: any) {
        return h("div", date.dateFormat(params.row.startTime));
      }
    },
    {
      title: "结束时间",
      key: "stopTime",
      align: "center",
      minWidth: 166,
      render: function(h: any, params: any) {
        return h("div", date.dateFormat(params.row.stopTime));
      }
    },
    {
      title: "Flink UI",
      key: "lastUiAddress",
      align: "center",
      minWidth: 250,
      ellipsis: true,
      render: (h: any, params: any) => {
        return h(
          "a",
          {
            on: {
              click: () => {
                this.handClickJobInstanceListColumnUiAddress(params.row);
              }
            }
          },
          params.row.appId
        );
      }
    },
    {
      title: "状态",
      key: "status",
      align: "center",
      minWidth: 100,
      slot: "status",
      fixed: "right"
    } /*,
    {
      title: "操作",
      fixed: "right",
      align: "center",
      slot: "operator",
      width: 100
    }*/
  ];
  jobInstanceList: IJobInstance[] = [];
  // job instance  query
  jobInstanceQueryCondition: any = {
    total: 100,
    pageNum: 1,
    pageSize: 10
  };
  jobInstancePageChange(num: number) {
    this.jobInstanceQueryCondition.pageNum = num;
    this.getJobInstanceList();
  }
  jobInstancePageSizeChange(size: number) {
    this.jobInstanceQueryCondition.pageSize = size;
    this.getJobInstanceList();
  }
  clickStart() {
    jobApi
      .startJob({ jobId: this.rt.jobId })
      .then(res => {
        this.$Notice.success({ title: "启动作业成功" });
        this.getJobInstanceList();
      })
      .catch(res => {
        this.$Notice.warning({
          title: "启动作业失败",
          desc: res.msg
        });
      })
      .finally(() => {
        this.jobTimer = this.getJobTimer();
      });
  }
  handClickJobInstanceListColumnUiAddress(row: any) {
    window.open(row.uiAddress, "_blank");
  }
  clickRestart() {
    jobApi
      .restartJob({ jobId: this.rt.jobId })
      .then(res => {
        this.$Notice.success({ title: "重启作业成功" });
      })
      .catch(res => {
        this.$Notice.warning({
          title: "重启作业失败",
          desc: res.msg
        });
      });
  }
  clickStop() {
    jobApi
      .stopJob({ jobId: this.rt.jobId })
      .then(res => {
        this.$Notice.success({ title: "停止作业成功" });
      })
      .catch(res => {
        this.$Notice.warning({
          title: "停止作业失败",
          desc: res.msg
        });
      });
  }
  clickDelete() {
    jobApi
      .deleteJob({ jobId: this.rt.jobId })
      .then(res => {
        this.$Notice.success({ title: "删除作业成功" });
        this.$router.push({
          path: "/page/job/list"
        });
      })
      .catch(res => {
        this.$Notice.warning({
          title: "删除作业失败",
          desc: res.msg
        });
      });
  }
  clickEdit() {
    this.$router.push({
      path: "/page/job/edit",
      query: {
        id: this.rt.jobId
      }
    });
  }
  getJob() {
    jobApi
      .queryJob({ jobId: this.rt.jobId })
      .then((res: any) => {
        this.job = res;
        // 最终状态时清除定时器
        if ([3, 4, 5, 6, undefined].includes(this.job.lastStatus)) {
          this.clearJobTimer();
        }
      })
      .catch(res => {
        this.$Notice.error({ title: res.msg });
      });
  }
  jobTimer: any = null;
  getJobTimer() {
    return setInterval(() => {
      this.getJob();
      this.getJobInstanceList();
    }, 1000);
  }
  clearJobTimer() {
    clearInterval(this.jobTimer);
    this.jobTimer = null;
  }
  getJobInstanceList() {
    jobInstanceApi
      .queryJobInstances({
        jobId: this.rt.jobId,
        pageNum: this.jobInstanceQueryCondition.pageNum,
        pageSize: this.jobInstanceQueryCondition.pageSize
      })
      .then((res: any) => {
        this.jobInstanceList = res.list;
        this.jobInstanceQueryCondition.total = res.total;
      })
      .catch(err => {
        this.$Notice.error({ title: err.msg });
      });
  }
  parseRouter() {
    this.rt.jobId = this.$route.query.id;
  }
  mounted() {
    this.parseRouter();
    this.getJob();
    this.getJobInstanceList();
  }
  beforeDestroy() {
    this.clearJobTimer();
  }
}
</script>

<style scoped lang="scss"></style>
