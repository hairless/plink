package com.github.hairless.plink.common.factory;

import com.github.hairless.plink.common.builder.FlinkJarSubmitOptionsBuilder;
import com.github.hairless.plink.common.builder.FlinkSqlSubmitOptionsBuilder;
import com.github.hairless.plink.common.builder.FlinkSubmitOptionsBuilder;
import com.github.hairless.plink.model.enums.JobTypeEnum;
import com.github.hairless.plink.model.exception.PlinkMessageException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: silence
 * @date: 2020/10/13
 */
public class FlinkSubmitOptionsBuilderFactory {
    private static final Map<JobTypeEnum, FlinkSubmitOptionsBuilder> builderMap = new HashMap<>();

    static {
        builderMap.put(JobTypeEnum.FLINK_JAR, new FlinkJarSubmitOptionsBuilder());
        builderMap.put(JobTypeEnum.FLINK_SQL, new FlinkSqlSubmitOptionsBuilder());
    }

    public static FlinkSubmitOptionsBuilder create(JobTypeEnum jobTypeEnum) {
        if (builderMap.containsKey(jobTypeEnum)) {
            return builderMap.get(jobTypeEnum);
        }
        throw new PlinkMessageException(jobTypeEnum + " not support");
    }
}
