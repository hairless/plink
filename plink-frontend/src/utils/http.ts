import axios from "axios";
import { config } from "@vue/test-utils";

// baseURL
axios.defaults.baseURL = "/";

// timeout
axios.defaults.timeout = 10000;

// Content-Type
axios.defaults.headers.post["Content-Type"] = "application/json;charset=UTF-8";

// interceptor request
axios.interceptors.request.use(
  config => {
    // ...
    return config;
  },
  error => {
    // return Promise.error(error);
    return Promise.reject(error);
  }
);

// interceptor response
const resultCode: any = {
  success: "10001",
  failure: "10002",
  exception: "10003"
};
axios.interceptors.response.use(
  response => {
    if (response.status === 200) {
      if (response.data.code === resultCode.success) {
        // success
        return Promise.resolve(response.data.data);
      } else if (response.data.code === resultCode.failure) {
        // failure
        return Promise.reject(response);
      } else if (response.data.code === resultCode.exception) {
        // exception
        return Promise.reject(response);
      } else {
        // else
        return Promise.reject(response);
      }
    } else {
      return Promise.reject(response);
    }
  },
  error => {
    return Promise.reject(error);
  }
);

// get
function get(url: string, params?: object) {
  return new Promise((resolve, reject) => {
    axios
      .get(url, {
        params: params
      })
      .then(res => {
        resolve(res);
      })
      .catch(err => {
        reject(err);
      });
  });
}

// post
function post(url: string, params: object) {
  return new Promise((resolve, reject) => {
    axios
      .post(url, params)
      .then((res: any) => {
        resolve(res);
      })
      .catch((err: any) => {
        reject(err);
      });
  });
}

// export
export { get, post };
