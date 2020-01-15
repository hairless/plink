<template>
  <div>
    <!-- Job Detail -->
    <Tabs>
      <TabPane label="作业信息" name="basic">
        <Divider>基础信息</Divider>
        <Row :gutter="10">
          <Col span="12">作业 ID : 1001</Col>
          <Col span="12">作业名称 : WordCount</Col>
        </Row>
        <Row :gutter="10">
          <Col span="12">作业类型 : 自定义 / Jar</Col>
          <Col span="12">作业描述 : 单词统计测试</Col>
        </Row>
        <Row :gutter="10">
          <Col span="12">创建时间 : 2020-01-15 19:01:05</Col>
          <Col span="12">更新时间 : 2020-01-15 19:01:05</Col>
        </Row>
        <Row :gutter="10">
          <Col span="12">启动时间 : 2020-01-15 19:01:05</Col>
          <Col span="12">结束时间 : 2020-01-15 19:01:05</Col>
        </Row>
        <Divider>作业配置</Divider>
        <Row :gutter="10">
          <Col span="12">客户端版本 : 1.10</Col>
          <Col span="12">执行文件 : wordcount.jar</Col>
        </Row>
        <Row :gutter="10">
          <Col span="12">MainClass : org.apache.flink.examples.WordCount</Col>
          <Col span="12">程序参数 : --input xxx.txt</Col>
        </Row>
        <!--<Divider>运行参数</Divider>-->
      </TabPane>
      <TabPane label="作业实例" name="job">
        <!-- Job List -->
        <div>
          <Table stripe ref="selection" :columns="jobInstanceListColumns" :data="jobInstanceList">
            <template slot="operator">
              <div>
                <Button type="info" size="small" @click="clickDetail">
                  详情
                </Button>
              </div>
            </template>
          </Table>
        </div>
        <!-- Job List Tools -->
        <div style="margin-top: 10px; padding: 5px; background: #f8f8f9">
          <Row :gutter="10">
            <Col span="8">
              <Button type="error" size="small" style="margin-left: 10px;" @click="clickDelete">删除</Button>
            </Col>
            <Col span="16" align="right">
              <Page :total="100" show-total show-sizer show-elevator size="small" />
            </Col>
          </Row>
        </div>
      </TabPane>
      <TabPane label="运行参数" name="runtime" disabled>
        <!-- ... -->
      </TabPane>
      <div slot="extra" style="margin-bottom: 10px; padding: 5px; background: #f8f8f9">
        <Button type="success" size="small" style="margin-right: 10px" @click="clickStart">启动</Button>
        <Button type="warning" size="small" style="margin-right: 10px" @click="clickRestart">重启</Button>
        <Button type="error" size="small" style="margin-right: 10px" @click="clickStop">停止</Button>
        <Button type="error" size="small" style="margin-right: 10px" @click="clickDelete">删除</Button>
        <Button type="info" size="small" style="margin-right: 10px" @click="clickEdit">编辑</Button>
      </div>
    </Tabs>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";

@Component
export default class JobDetailCustom extends Vue {
  job: any = {
    name: "",
    type: "",
    description: "",
    client_version: "",
    config: {
      app: {
        exec_file: "",
        main_class: "",
        params: ""
      },
      runtime: {
        // ...
      }
    }
  };
  // Job Instance List
  jobInstanceListColumns: object[] = [
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
      key: "start_time",
      align: "center"
    },
    {
      title: "结束时间",
      key: "stop_time",
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
      width: 100
    }
  ];
  jobInstanceList: object[] = [
    {
      id: 10001,
      name: "作业1",
      type: "自定义 / Jar",
      description: "单词统计",
      start_time: "2020-01-13 12:14:00",
      stop_time: "2020-01-13 14:45:21",
      last_status: "已停止",
      _checked: true
    },
    {
      id: 10002,
      name: "作业2",
      type: "自定义 / Jar",
      description: "文件测试",
      start_time: "2020-01-13 12:16:00",
      stop_time: "2020-01-13 12:45:21",
      last_status: "已停止"
    }
  ];
  clickStart() {
    this.$Message.success("启动");
  }
  clickRestart() {
    this.$Message.success("重启");
  }
  clickStop() {
    this.$Message.success("停止");
  }
  clickDelete() {
    this.$Message.success("删除");
  }
  clickEdit() {
    this.$Message.success("编辑");
  }
}
</script>

<style scoped lang="scss"></style>
