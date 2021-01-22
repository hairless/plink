package com.github.hairless.plink.common.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: silence
 * @date: 2021/1/22
 */
public class MapUtils {
    public static <K, V> Map<K, V> mergeMap(Map<K, V> map, Map<K, V> map2) {
        final Map<K, V> mergeMap = map == null ? new HashMap<>() : map;
        if (map2 == null) {
            map2 = new HashMap<>();
        }
        map2.forEach((k, v) -> {
            if (!mergeMap.containsKey(k)) {
                mergeMap.put(k, v);
            }
        });
        return mergeMap;
    }
}
