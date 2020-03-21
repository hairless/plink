interface IJob {
  id?: number;
  name?: string;
  description?: string;
  type?: number;
  clientVersion?: string;
  lastStatus?: number;
  lastAppId?: string;
  lastStartTime?: string;
  lastStopTime?: string;
  createTime?: string;
  updateTime?: string;
  lastUiAddress?: string;
  config?: {
    jarName?: string;
    mainClass?: string;
    args?: string;
    parallelism?: number;
  };
  authMap?: {
    edit?: boolean;
    delete?: boolean;
    start?: boolean;
    stop?: boolean;
    restart?: boolean;
  };
}

export { IJob };
