import moment from "moment";

function dateFormat(dateStr: string, pattern: string = "YYYY-MM-DD HH:mm:ss") {
  let formatStr = moment(dateStr).format(pattern);
  if ("Invalid date" === formatStr) {
    return "";
  }
  return formatStr;
}

export default {
  dateFormat
};
