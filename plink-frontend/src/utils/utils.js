export function mapLeftJoinCopy(left, right, skipRightNull = true) {
  let t = Object.create(null);
  Object.keys(left).forEach(k => {
    let v = right[k] === undefined ? null : right[k];
    if (v === null) {
      if (!skipRightNull) {
        if (isNumber(v)) {
          t[k] = parseFloat(v);
        } else {
          t[k] = v;
        }
      } else {
        if (isNumber(left[k])) {
          t[k] = parseFloat(left[k]);
        } else {
          t[k] = left[k];
        }
      }
    } else {
      if (isNumber(v)) {
        t[k] = parseFloat(v);
      } else {
        t[k] = v;
      }
    }
  });
  return t;
}

/**
 * delete blank V and K
 *
 * @param map
 */
export function objectDeleteBlankVK(map) {
  let t = Object.create(null);
  let keys = Object.keys(map);
  for (let index in keys) {
    let k = keys[index];
    let v = map[k];
    if (v === undefined || v === null || v.toString().length === 0) {
      continue;
    }
    t[k] = v;
  }
  return t;
}

export function isNumber(val) {
  return parseFloat(val).toString() !== "NaN";
}

export function deepCopy(jsonObject) {
  return JSON.parse(JSON.stringify(jsonObject));
}
