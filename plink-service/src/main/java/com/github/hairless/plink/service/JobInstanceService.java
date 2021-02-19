package com.github.hairless.plink.service;

import com.github.hairless.plink.model.dto.JobInstanceDTO;
import com.github.hairless.plink.model.pojo.JobInstance;
import com.github.hairless.plink.model.req.PageReq;
import com.github.pagehelper.PageInfo;

/**
 * @Author Trevor
 * @Create 2020/1/21 10:13
 */
public interface JobInstanceService {
    PageInfo<JobInstanceDTO> queryJobInstances(JobInstanceDTO jobInstanceDTO, PageReq pageReq);

    void updateJobAndInstanceStatus(JobInstance jobInstance);

    String startLog(Long jobInstanceId);

    String getClientLogFilePath(JobInstanceDTO jobInstanceDTO);
}
