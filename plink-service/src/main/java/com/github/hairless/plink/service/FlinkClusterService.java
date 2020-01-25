package com.github.hairless.plink.service;

import com.github.hairless.plink.model.common.FlinkConfig;
import com.github.hairless.plink.model.resp.JobDetail;
import com.github.hairless.plink.service.exception.FlinkClusterServiceException;

/**
 * @author: silence
 * @date: 2020/1/19
 * 封装 flink rest api，此处的内容应只和flink api本身相关
 */
public interface FlinkClusterService {

    /**
     * 作业提交
     * 上传jar包 并启动作业
     * @param filePath jar path
     * @return jobId
     */
    String uploadJar(String filePath) throws FlinkClusterServiceException;

    /**
     *
     * @param jarId
     * @param config flink 启动参数
     * @return
     * @throws FlinkClusterServiceException
     */
    String runJob(String jarId, FlinkConfig config)throws FlinkClusterServiceException;

    /**
     * 查询任务状态
     * @param  jobId 任务id 由#runJob返回
     * @return 详细信息及状态
     */
    JobDetail jobDetail(String jobId) throws FlinkClusterServiceException;

    /**
     * 停止作业
     *
     * @param jobId 任务id 由#runJob返回
     */
    void cancelJob(String jobId) throws FlinkClusterServiceException;
}
