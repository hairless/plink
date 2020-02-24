import { get, post } from "@/utils/http.ts";

const JOB_INSTANCE_STATUS_URL = "/mng/enum/jobInstanceStatus";
const JOB_TYPE_URL = "/mng/enum/jobType";
const JOB_CLIENT_VERSION_URL = "/mng/enum/jobClientVersion";

function jobInstanceStatus() {
  return get(JOB_INSTANCE_STATUS_URL);
}

function jobType() {
  return get(JOB_TYPE_URL, {});
}

function jobClientVersion() {
  return get(JOB_CLIENT_VERSION_URL);
}

export default {
  jobInstanceStatus,
  jobType,
  jobClientVersion
};
