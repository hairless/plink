<template>
  <div>
    <!-- Job Query-->
    <div style="margin-bottom: 10px; padding: 5px; background: #f8f8f9">
      <Row :gutter="10">
        <Col span="20">
          <span> ID : </span>
          <Input v-model="jobQueryCondition.id" placeholder=""  style="width: 80px" size="small" />
          <span> 名称 : </span>
          <Input v-model="jobQueryCondition.name" placeholder="" style="width: 200px" size="small" />
        </Col>
        <Col span="4" align="right">
          <Button type="primary" size="small" style="margin-right: 10px;">查询</Button>
          <Button type="success" size="small" @click="jobCreateModal = true">新建</Button>
        </Col>
      </Row>
    </div>
    <!-- Job List -->
    <div>
      <Table stripe ref="selection" :columns="jobListColumns" :data="jobList">
        <template slot="operator">
          <div>
            <a href="http://www.baidu.com">
              <Button type="info" size="small">
                编辑
              </Button>
            </a>
          </div>
        </template>
      </Table>
    </div>
    <div style="margin-top: 10px; padding: 5px; background: #f8f8f9">
      <Row :gutter="10">
        <Col span="8">
          <Button type="success" size="small">启动</Button>
          <Button type="error" size="small" style="margin-left: 10px;">停止</Button>
          <Button type="warning" size="small" style="margin-left: 10px;">重启</Button>
        </Col>
        <Col span="16" align="right">
          <Page :total="100" show-total show-sizer show-elevator size="small" />
        </Col>
      </Row>
    </div>
    <!-- Job Create -->
    <div>
      <Modal
        v-model="jobCreateModal"
        title="新建作业"
        @on-ok="jobCreateOk"
        @on-cancel="jobCreateCancel"
        draggable
        ok-text="新建"
      >
        <Form :model="jobCreateItems" :label-width="80">
          <FormItem label="作业名称 :">
            <Input v-model="jobCreateItems.name" placeholder="" />
          </FormItem>
          <FormItem label="作业类型 : ">
            <Select v-model="jobCreateItems.type">
              <Option value="ud-jar">自定义 Jar</Option>
              <Option value="tl-jar">模板 Jar</Option>
            </Select>
          </FormItem>
          <FormItem label="作业描述 :">
            <Input v-model="jobCreateItems.description" placeholder="" />
          </FormItem>
        </Form>
      </Modal>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";

@Component
export default class JobList extends Vue {
  // Job List
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
  jobList: object[] = [
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
  // Job Query
  jobQueryCondition: object = {
    id: "",
    name: ""
  };
  // Job Create
  jobCreateModal: boolean = false;
  jobCreateItems: object = {
    name: "name",
    description: "desc"
  };
  jobCreateOk() {
    this.$Message.success("新建作业成功");
  }
  jobCreateCancel() {
    this.$Message.warning("取消新建作业");
  }
}
</script>

<style scoped lang="scss"></style>
