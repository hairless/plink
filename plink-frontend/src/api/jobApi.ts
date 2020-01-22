import { get, post } from "@/utils/http.ts";

const QUERY_JOB_URL = "/mng/job/queryJob/";
const QUERY_JOBS_URL = "/mng/job/queryJobs";
const ADD_JOB_URL = "/mng/job/addJob";
const UPDATE_JOB_URL = "/mng/job/updateJob";
const DELETE_JOB_URL = "/mng/job//deleteJob/{jobId}";
const DELETE_JOBS_URL = "/mng/job/deleteJobs";
const JAR_LIST_URL = "/mng/job/{jobId}/jarList";
const UPLOAD_JAR_URL = "/mng/job/{jobId}/uploadJar";
const START_JOB_URL = "/mng/job/startJob/{jobId}";
const START_JOBS_URL = "/mng/job/startJobs";
const RESTART_JOB_URL = "/mng/job/reStartJob/{jobId}";
const RESTART_JOBS_URL = "/mng/job/reStartJobs";
const STOP_JOB_URL = "/mng/job/startJob/{jobId}";
const STOP_JOBS_URL = "/mng/job/stopJobs";

function queryJob(params: any) {
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
  return post(DELETE_JOB_URL.replace("{jobId}", params.jobId), params);
}

function deleteJobs(params: any) {
  return post(DELETE_JOBS_URL, params.idList);
}

function jarList(params: any) {
  return get(JAR_LIST_URL.replace("{jobId}", params.jobId));
}

function startJob(params: any) {
  return get(START_JOB_URL.replace("{jobId}", params.jobId));
}

function startJobs(params: any) {
  return post(START_JOBS_URL, params.idList);
}

function restartJob(params: any) {
  return get(RESTART_JOB_URL.replace("{jobId}", params.jobId));
}

function restartJobs(params: any) {
  return post(RESTART_JOBS_URL, params.idList);
}

function stopJob(params: any) {
  return get(STOP_JOB_URL.replace("{jobId}", params.jobId));
}

function stopJobs(params: any) {
  return post(STOP_JOBS_URL, params.idList);
}

export default {
  UPLOAD_JAR_URL,
  queryJob,
  queryJobs,
  addJob,
  updateJob,
  deleteJob,
  deleteJobs,
  jarList,
  startJob,
  startJobs,
  restartJob,
  restartJobs,
  stopJob,
  stopJobs
};
