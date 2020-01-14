<template>
  <div>
    <!-- Job Edit -->
    <Tabs>
      <TabPane label="基本配置" name="basic">
        <Form :model="job" :label-width="100" style="width:80%;">
          <FormItem label="作业名称 :">
            <Input v-model="job.name" placeholder="" />
          </FormItem>
          <FormItem label="作业类型 : ">
            <Select v-model="job.type">
              <Option value="1">自定义 Jar</Option>
              <Option value="2">模板 Jar</Option>
            </Select>
          </FormItem>
          <FormItem label="作业描述 :">
            <Input type="textarea" v-model="job.description" placeholder="" :rows="4" />
          </FormItem>
        </Form>
      </TabPane>
      <TabPane label="作业配置" name="job">
        <Form :model="job" :label-width="100" style="width:80%;">
          <FormItem label="客户端版本 :">
            <Select v-model="job.client_version" placeholder="请选择客户端版本">
              <Option value="1.9.0">1.9.0</Option>
            </Select>
          </FormItem>
          <FormItem label="执行文件 : ">
            <Select v-model="job.config.app.exec_file" placeholder="可上传文件">
              <Option value="1">wordcount.jar</Option>
              <Option value="2">user.jar</Option>
            </Select>
            <Upload action="//jsonplaceholder.typicode.com/posts/">
              <Button icon="ios-cloud-upload-outline">上传文件</Button>
            </Upload>
          </FormItem>
          <FormItem label="MainClass :">
            <Input v-model="job.config.app.main_class" placeholder="" />
          </FormItem>
          <FormItem label="程序参数 :">
            <Input v-model="job.config.app.params" type="textarea" :rows="4" />
          </FormItem>
        </Form>
      </TabPane>
      <TabPane label="运行参数" name="runtime" disabled>
        <!-- ... -->
      </TabPane>
      <div slot="extra" style="margin-bottom: 10px; padding: 5px; background: #f8f8f9">
        <Button type="info" size="small" style="margin-right: 10px" @click="clickGoBack">返回</Button>
        <Button type="warning" size="small" style="margin-right: 10px">取消</Button>
        <Button type="success" size="small" @click="clickSave">保存</Button>
      </div>
    </Tabs>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";

@Component
export default class JobEditCustom extends Vue {
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

  clickGoBack() {
    this.$Message.success("返回成功");
  }
  clickSave() {
    this.$Message.success("保存配置成功");
  }
}
</script>

<style scoped lang="scss"></style>
