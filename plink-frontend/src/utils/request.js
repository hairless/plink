import axios from "axios";
import store from "@/store";
import { getToken } from "@/utils/auth";
import { Notice } from "view-design";

// axios.defaults.headers.post["Content-Type"] = "application/json;charset=UTF-8";

// create an axios instance
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API, // url = base url + request url
  // withCredentials: true, // send cookies when cross-domain requests
  timeout: 20000 // request timeout
});

// request interceptor
service.interceptors.request.use(
  config => {
    // do something before request is sent

    if (store.getters.token) {
      // let each request carry token
      // ['x-auth-token'] is a custom headers key
      // please modify it according to the actual situation
      config.headers["x-auth-token"] = getToken();
    }
    return config;
  },
  error => {
    // do something with request error
    console.log(error); // for debug
    return Promise.reject(error);
  }
);

// response interceptor
service.interceptors.response.use(
  response => {
    const resp = response.data;

    // if the custom code is not 10000, it is judged as an error.
    switch (resp.code) {
      case 10000:
        return resp;
      case 40001:
        // 登录失败
        return Promise.reject(resp);
      case 40003:
        Notice.warning({
          title: "Token 已失效，请重新登录",
          duration: 3
        });
        store.dispatch("user/resetToken").then(() => {
          location.reload();
        });
        return resp;
      case 40005:
        return Promise.reject(resp);
      default:
        Notice.error({
          title: "ERROR !",
          desc: resp.msg || "Error",
          duration: 5
        });
        return Promise.reject(new Error(resp.msg || "Error"));
    }
  },
  error => {
    // console.log('err' + error); // for debug
    Notice.error({
      title: error.message
    });
    return Promise.reject(error);
  }
);

export default service;
