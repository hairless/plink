import request from "@/utils/request";

// 上传 Jar 的 URL
export const UPLOAD_JAR_URL = "/api/mng/job/{jobId}/uploadJar";

/************** 作业相关 ********************/
export function getJobPageList(filter) {
  return request({
    url: "/api/mng/job/queryJobs",
    method: "get",
    params: filter
  });
}

/*export function getJobList(filter) {
  return request({
    url: "/api/mng/job/getJobList",
    method: "get",
    params: filter
  });
}*/

export function addJob(data) {
  return request({
    url: "/api/mng/job/addJob",
    method: "post",
    data: data
  });
}

export function getJob(jobId) {
  return request({
    url: "/api/mng/job/queryJob/" + jobId,
    method: "get"
  });
}

export function updateJob(data) {
  return request({
    url: "/api/mng/job/updateJob",
    method: "post",
    data: data
  });
}

export function deleteJob(jobId) {
  return request({
    url: "/api/mng/job/deleteJob/" + jobId,
    method: "post"
  });
}

export function deleteJobList(idList) {
  return request({
    url: "/api/mng/job/deleteJobs",
    method: "post",
    data: idList
  });
}

export function getJobJarList(jobId) {
  return request({
    url: "/api/mng/job/{jobId}/jarList".replace("{jobId}", jobId),
    method: "get"
  });
}

export function startJob(jobId) {
  return request({
    url: "/api/mng/job/startJob/{jobId}".replace("{jobId}", jobId),
    method: "get"
  });
}

export function startJobList(idList) {
  return request({
    url: "/api/mng/job/startJobs",
    method: "get",
    params: idList
  });
}

export function restartJob(jobId) {
  return request({
    url: "/api/mng/job/reStartJob/{jobId}".replace("{jobId}", jobId),
    method: "get"
  });
}

export function restartJobList(idList) {
  return request({
    url: "/api/mng/job/reStartJobs",
    method: "get",
    params: idList
  });
}

export function stopJob(jobId) {
  return request({
    url: "/api/mng/job/stopJob/{jobId}".replace("{jobId}", jobId),
    method: "get"
  });
}

export function stopJobList(idList) {
  return request({
    url: "/api/mng/job/stopJobs",
    method: "get",
    params: idList
  });
}
