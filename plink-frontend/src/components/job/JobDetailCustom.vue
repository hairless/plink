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
          <Col span="12">作业类型 : {{ job.type }}</Col>
          <Col span="12">作业描述 : {{ job.description }}</Col>
        </Row>
        <Row :gutter="10">
          <Col span="12">创建时间 : {{ job.createTime }}</Col>
          <Col span="12">更新时间 : {{ job.updateTime }}</Col>
        </Row>
        <Row :gutter="10">
          <Col span="12">启动时间 : {{ job.lastStartTime }}</Col>
          <Col span="12">结束时间 : {{ job.lastStopTime }}</Col>
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
              <span>{{ job.name }}</span>
            </template>
            <template slot="type">
            <span>{{ job.type }}</span>
          </template>
            <template slot="operator">
              <div>
                <Button type="info" size="small">
                  详情
                </Button>
              </div>
            </template>
          </Table>
        </div>
        <!-- Job List Tools -->
        <!--<div style="margin-top: 10px; padding: 5px; background: #f8f8f9">
          <Row :gutter="10">
            <Col span="8">
              <Button type="error" size="small" style="margin-left: 10px;" @click="clickDelete">删除</Button>
            </Col>
            <Col span="16" align="right">
              <Page :total="100" show-total show-sizer show-elevator size="small" />
            </Col>
          </Row>
        </div>-->
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
          >启动</Button
        >
        <Button
          type="warning"
          size="small"
          style="margin-right: 10px"
          @click="clickRestart"
          >重启</Button
        >
        <Button
          type="error"
          size="small"
          style="margin-right: 10px"
          @click="clickStop"
          >停止</Button
        >
        <Button
          type="error"
          size="small"
          style="margin-right: 10px"
          @click="clickDelete"
          >删除</Button
        >
        <Button
          type="info"
          size="small"
          style="margin-right: 10px"
          @click="clickEdit"
          >编辑</Button
        >
      </div>
    </Tabs>
  </div>
</template>

<script lang="ts">
  import {Component, Vue} from "vue-property-decorator";
  import jobApi from "@/api/jobApi";
  import jobInstanceApi from "@/api/jobInstanceApi";
  import {IJob} from "@/model/jobModel";
  import {IJobInstance} from "@/model/jobInstanceModel";

  @Component
export default class JobDetailCustom extends Vue {
  rt: any = {
    jobId: ""
  };
  job: IJob = {
    config: {}
  };
  // Job Instance List
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
      align: "center",
      slot: "name"
    },
    {
      title: "类型",
      key: "type",
      align: "center",
      slot: "type"
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
    },
    {
      title: "操作",
      fixed: "right",
      align: "center",
      slot: "operator",
      width: 100
    }
  ];
  jobInstanceList: IJobInstance[] = [];
  clickStart() {
    jobApi
      .startJob({ jobId: this.rt.jobId })
      .then(res => {
        this.$Notice.success({ title: "启动作业成功" });
      })
      .catch(res => {
        this.$Notice.warning({
          title: "启动作业失败",
          desc: res.msg
        });
      });
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
  // get
  getJob() {
    jobApi
      .queryJob({ jobId: this.rt.jobId })
      .then((res: any) => {
        this.job = res;
      })
      .catch(res => {
        this.$Notice.error({ title: res.msg });
      });
  }

  getJobInstanceList() {
    jobInstanceApi
      .queryJobInstances({ jobId: this.rt.jobId })
      .then((res: any) => {
        this.jobInstanceList = res.list;
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
}
</script>

<style scoped lang="scss"></style>
