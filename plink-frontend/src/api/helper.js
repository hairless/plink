import request from "@/utils/request";

/************** Helper 相关 ********************/
export function getInstStatusList() {
  return request({
    url: "/mng/enum/jobInstanceStatus",
    method: "get"
  });
}

export function getJobTypeList() {
  return request({
    url: "/mng/enum/jobType",
    method: "get"
  });
}

export function getJobClientVersionList() {
  return request({
    url: "/mng/enum/jobClientVersion",
    method: "get"
  });
}

export function getJobStateInfoType() {
  return request({
    url: "/mng/enum/jobStateInfoType",
    method: "get"
  });
}

export function getDefaultFlinkConfs() {
  return request({
    url: "/mng/util/defaultFlinkConfs",
    method: "get"
  });
}
