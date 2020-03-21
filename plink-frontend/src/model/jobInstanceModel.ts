import { IJob } from "@/model/jobModel";

interface IJobInstance {
  id?: number;
  jobId?: number;
  status?: string;
  appId?: string;
  startTime?: string;
  stopTime?: string;
  createTime?: string;
  updateTime?: string;
  uiAddress?: string;
  config?: {
    jarName?: string;
    mainClass?: string;
    args?: string;
    parallelism?: number;
  };
  job?: IJob;
}

export { IJobInstance };
