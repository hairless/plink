interface IJobInstance {
  id?: number;
  jobId?: number;
  name?: string;
  description?: string;
  type?: number;
  clientVersion?: string;
  config?: {
    jarName?: string;
    mainClass?: string;
    args?: string;
    parallelism?: number;
  };
  status?: string;
  appId?: string;
  startTime?: string;
  stopTime?: string;
  createTime?: string;
  updateTime?: string;
}

export { IJobInstance };
