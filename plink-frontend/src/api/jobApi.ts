import { get, post } from "@/utils/http.ts";

const QUERY_JOB_URL = "/mng/job/queryJob/";
const QUERY_JOBS_URL = "/mng/job/queryJobs";
const ADD_JOB_URL = "/mng/job/addJob";
const UPDATE_JOB_URL = "/mng/job/updateJob";
const DELETE_JOB_URL = "/mng/job/deleteJob";
const JAR_LIST_URL = "/mng/job/{jobId}/jarList";
const UPLOAD_JAR__URL = "/mng/job/{jobId}/uploadJar";

function queryJob(params: any) {
  console.log(params);
  return get(QUERY_JOB_URL + params.jobId);
}

function queryJobs(params: any) {
  return post(QUERY_JOBS_URL, params);
}

function addJob(params: any) {
  return post(ADD_JOB_URL, params);
}

function updateJob(params: any) {
  return post(UPDATE_JOB_URL, params);
}

function deleteJob(params: any) {
  return get(DELETE_JOB_URL, params);
}

function jarList(params: any) {
  return get(JAR_LIST_URL.replace("{jobId}", params.jobId), params);
}

function uploadJar(params: any) {
  return get(UPLOAD_JAR__URL.replace("{jobId}", params.jobId), params);
}

export default {
  queryJob,
  queryJobs,
  addJob,
  updateJob,
  deleteJob,
  jarList,
  uploadJar
};
