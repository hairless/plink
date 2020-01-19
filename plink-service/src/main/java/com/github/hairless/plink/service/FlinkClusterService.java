package com.github.hairless.plink.service;

import com.github.hairless.plink.model.enums.JobInstanceStatusEnum;
import com.github.hairless.plink.model.resp.JobResp;

/**
 * @author: silence
 * @date: 2020/1/19
 */
public interface FlinkClusterService {
    /**
     * 作业提交
     *
     * @param jobResp 作业信息
     * @return appId
     */
    String submitJob(JobResp jobResp) throws Exception;

    /**
     * 查询任务状态
     *
     * @param jobResp 作业信息
     * @return 状态枚举
     */
    JobInstanceStatusEnum jobStatus(JobResp jobResp) throws Exception;

    /**
     * 停止作业
     *
     * @param jobResp 作业信息
     * @return 是否成功
     */
    Boolean cancelJob(JobResp jobResp) throws Exception;
}
