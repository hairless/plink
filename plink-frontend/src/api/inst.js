import request from "@/utils/request";

/************** 实例相关 ********************/
export function getInstPageList(filter) {
  return request({
    url: "/api/mng/jobInstance/queryJobInstances",
    method: "get",
    params: filter
  });
}

export function getInstLog(id) {
  return request({
    url: "/api/mng/jobInstance/startLog/" + id,
    method: "get"
  });
}
