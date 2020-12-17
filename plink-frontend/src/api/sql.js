import request from "@/utils/request";

/************** 作业相关 ********************/
export function sqlParse(sql) {
  return request({
    url: "/mng/sql/parse",
    method: "post",
    params: {"sql": sql}
  });
}