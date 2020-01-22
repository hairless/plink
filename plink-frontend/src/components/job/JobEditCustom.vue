<template>
  <div>
    <!-- Job Edit -->
    <Tabs>
      <TabPane label="基本配置" name="basic">
        <Form :model="jobEdit" :label-width="100">
          <FormItem label="作业名称 :">
            <Input v-model="jobEdit.name" placeholder="" />
          </FormItem>
          <FormItem label="作业类型 : ">
            <Select v-model="jobEdit.type">
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
              v-model="jobEdit.description"
              placeholder=""
              :rows="4"
            />
          </FormItem>
        </Form>
      </TabPane>
      <TabPane label="作业配置" name="job">
        <Form :model="jobEdit" :label-width="100" style="width:80%;">
          <FormItem label="客户端版本 :">
            <Select
              v-model="jobEdit.clientVersion"
              placeholder="请选择客户端版本"
            >
              <Option value="1.9.0">1.9.0</Option>
            </Select>
          </FormItem>
          <FormItem label="执行文件 : ">
            <Select v-model="jobEdit.config.jarName" placeholder="可上传文件">
              <Option
                v-for="item in hintExecFileList"
                :value="item.value"
                :key="item.value"
                >{{ item.label }}</Option
              >
            </Select>
            <Upload :action="uploadJarActionUrl" :on-success="uploadOnSuccess">
              <Button icon="ios-cloud-upload-outline">上传文件</Button>
            </Upload>
          </FormItem>
          <FormItem label="MainClass :">
            <Input v-model="jobEdit.config.mainClass" placeholder="" />
          </FormItem>
          <FormItem label="程序参数 :">
            <Input v-model="jobEdit.config.args" type="textarea" :rows="4" />
          </FormItem>
        </Form>
      </TabPane>
      <TabPane label="运行参数" name="runtime">
        <Form :model="jobEdit" :label-width="100" style="width:80%;">
          <FormItem label="作业并行度 :">
            <Slider v-model="jobEdit.config.parallelism" show-input></Slider>
          </FormItem>
        </Form>
      </TabPane>
      <div
        slot="extra"
        style="margin-bottom: 10px; padding: 5px; background: #f8f8f9"
      >
        <Button
          type="info"
          size="small"
          style="margin-right: 10px"
          @click="clickGoBack"
          >返回</Button
        >
        <Button type="success" size="small" @click="clickSave">保存</Button>
      </div>
    </Tabs>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import jobApi from "@/api/jobApi";
import { IJob } from "@/model/jobModel";

@Component
export default class JobEditCustom extends Vue {
  // hint
  hintJobTypeList: object[] = [{ value: 1, label: "自定义 / Jar" }];
  hintExecFileList: object[] = [];

  rt: any = {
    jobId: ""
  };
  jobEdit: IJob = {
    name: "",
    config: {}
  };

  clickGoBack() {
    this.$router.go(-1);
  }
  clickSave() {
    jobApi
      .updateJob(this.jobEdit)
      .then(res => {
        this.$Message.success("保存配置成功");
      })
      .catch(err => {
        this.$Notice.error({
          title: "保存配置失败",
          desc: err.msg
        });
      });
  }

  // upload
  uploadJarActionUrl: string = jobApi.UPLOAD_JAR_URL;

  uploadOnSuccess(res: any, file: any) {
    this.getJobJarList();
    this.$Notice.success({
      title: "上传文件成功"
    });
  }

  // get
  getJob() {
    jobApi
      .queryJob({ jobId: this.rt.jobId })
      .then((res: any) => {
        this.jobEdit.id = res.id;
        this.jobEdit.name = res.name;
        this.jobEdit.type = res.type;
        this.jobEdit.description = res.description;
        this.jobEdit.clientVersion = res.clientVersion;
        this.jobEdit.config = res.config;
      })
      .catch(res => {
        this.$Notice.error({ title: res.msg });
      });
  }

  getJobJarList() {
    let a = {
      a: 1,
      b: 2
    };
    jobApi.jarList({ jobId: this.rt.jobId }).then((res: any) => {
      let t: object[] = res;
      this.hintExecFileList = t.map(x => {
        return { value: x.toString(), label: x.toString() };
      });
    });
  }

  parseRouter() {
    let id = this.$route.query.id;
    this.rt.jobId = id;
    this.uploadJarActionUrl = this.uploadJarActionUrl.replace(
      "{jobId}",
      String(id)
    );
  }

  mounted() {
    this.parseRouter();
    this.getJob();
    this.getJobJarList();
  }
}
</script>

<style scoped lang="scss"></style>
