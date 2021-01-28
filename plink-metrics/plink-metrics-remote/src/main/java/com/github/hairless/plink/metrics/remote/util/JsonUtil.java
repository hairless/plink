package com.github.hairless.plink.metrics.remote.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @description: Json Utils
 * @author: thorntree
 * @create: 2021-01-26 15:46
 */
public class JsonUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(MapperFeature.PROPAGATE_TRANSIENT_MARKER, true);
    }

    public static String toJSONString(Object value) {
        try {
            return mapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("json parse error", e);
        }
    }
}
