import { get, post } from "@/utils/http.ts";

const QUERY_JOB_INSTANCES_URL = "/mng/jobInstance/queryJobInstances/";

function queryJobInstances(params: any) {
  return get(QUERY_JOB_INSTANCES_URL, params);
}

export default {
  queryJobInstances
};
