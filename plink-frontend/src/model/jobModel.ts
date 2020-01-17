interface JobModel {
  id?: number;
  name?: string;
  description?: string;
  type?: number;
  clientVersion?: string;
  config?: {
    execFile?: string;
    mainClass?: string;
    params?: string;
  };
  lastStatus?: string;
  lastAppId?: string;
  lastStartTime?: string;
  lastStopTime?: string;
  createTime?: string;
  updateTime?: string;
}

export { JobModel };
