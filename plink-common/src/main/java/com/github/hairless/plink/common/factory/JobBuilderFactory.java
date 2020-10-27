package com.github.hairless.plink.common.factory;

import com.github.hairless.plink.common.builder.JarJobBuilder;
import com.github.hairless.plink.common.builder.JobBuilder;
import com.github.hairless.plink.common.builder.SqlJobBuilder;
import com.github.hairless.plink.model.enums.JobTypeEnum;
import com.github.hairless.plink.model.exception.PlinkMessageException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: silence
 * @date: 2020/10/13
 */
public class JobBuilderFactory {
    private static final Map<JobTypeEnum, JobBuilder> builderMap = new HashMap<>();

    static {
        builderMap.put(JobTypeEnum.FLINK_JAR, new JarJobBuilder());
        builderMap.put(JobTypeEnum.FLINK_SQL, new SqlJobBuilder());
    }

    public static JobBuilder create(JobTypeEnum jobTypeEnum) {
        if (builderMap.containsKey(jobTypeEnum)) {
            return builderMap.get(jobTypeEnum);
        }
        throw new PlinkMessageException(jobTypeEnum + " not support");
    }
}
