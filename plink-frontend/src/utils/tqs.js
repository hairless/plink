let qs = require("qs");
import collection from "@/utils/collection";

function stringify(data) {
  return qs.stringify(data);
}

function stringifyExcludedBlank(data) {
  return qs.stringify(collection.mapDeleteBlankVK(data));
}

function parse(data) {
  return qs.parse(data);
}

function getQueryString(obj) {
  return obj
    ? "?" +
        Object.keys(obj)
          .filter(function(key) {
            return obj[key];
          })
          .map(function(key) {
            let val = obj[key];
            if (Array.isArray(val)) {
              return val
                .map(function(val2) {
                  return (
                    encodeURIComponent(key) + "=" + encodeURIComponent(val2)
                  );
                })
                .join("&");
            }
            return encodeURIComponent(key) + "=" + encodeURIComponent(val);
          })
          .join("&")
    : "";
}

export default {
  stringify,
  stringifyExcludedBlank,
  parse,
  getQueryString
};
