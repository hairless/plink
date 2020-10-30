import request from "@/utils/request";

/************** Helper 相关 ********************/
export function getInstStatusList() {
  return request({
    url: "/api/mng/enum/jobInstanceStatus",
    method: "get"
  });
}

export function getJobTypeList() {
  return request({
    url: "/api/mng/enum/jobType",
    method: "get"
  });
}

export function getJobClientVersionList() {
  return request({
    url: "/api/mng/enum/jobClientVersion",
    method: "get"
  });
}

export function getDefaultFlinkConfs() {
  return request({
    url: "/api/mng/util/defaultFlinkConfs",
    method: "get"
  });
}
