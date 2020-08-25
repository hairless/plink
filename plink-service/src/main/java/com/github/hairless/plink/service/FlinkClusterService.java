package com.github.hairless.plink.service;

import com.github.hairless.plink.model.dto.JobInstanceDTO;
import com.github.hairless.plink.model.enums.JobInstanceStatusEnum;

/**
 * @author: silence
 * @date: 2020/1/19
 */
public interface FlinkClusterService {
    /**
     * 作业实例提交flink job
     *
     * @param jobInstanceDTO 作业实例信息
     * @return appId
     */
    String submitJob(JobInstanceDTO jobInstanceDTO, String logFile) throws Exception;

    /**
     * 查询flink job的状态对应的作业实例状态
     *
     * @param jobInstanceDTO 作业实例信息
     * @return 状态枚举
     */
    JobInstanceStatusEnum jobStatus(JobInstanceDTO jobInstanceDTO) throws Exception;

    /**
     * 停止作业实例对应的flink job
     *
     * @param jobInstanceDTO 作业实例信息
     * @return 是否成功
     */
    void stopJob(JobInstanceDTO jobInstanceDTO) throws Exception;

    /**
     * 获取任务的ui地址
     *
     * @param appId 任务提交返回的id
     * @return 是否成功
     */
    String getJobUiAddress(String appId) throws Exception;
}
