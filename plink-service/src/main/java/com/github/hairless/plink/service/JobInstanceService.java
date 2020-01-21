package com.github.hairless.plink.service;

import com.github.hairless.plink.model.req.JobInstanceReq;
import com.github.hairless.plink.model.resp.JobInstanceResp;
import com.github.hairless.plink.model.resp.Result;
import com.github.pagehelper.PageInfo;

/**
 * @Author Trevor
 * @Create 2020/1/21 10:13
 */
public interface JobInstanceService {
    Result<PageInfo<JobInstanceResp>> queryJobInstances(JobInstanceReq JobInstanceReq);
}
