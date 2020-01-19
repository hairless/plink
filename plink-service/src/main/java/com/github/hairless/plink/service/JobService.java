package com.github.hairless.plink.service;

import com.github.hairless.plink.model.req.JobReq;
import com.github.hairless.plink.model.resp.JobResp;
import com.github.hairless.plink.model.resp.Result;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author Trevor
 * @Create 2020/1/14 20:25
 */

public interface JobService {
    Result<JobResp> addJob(JobReq jobReq);

    Result deleteJob(Long jobId);

    Result deleteJobs(List<Long> idList);

    Result updateJob(JobReq jobReq);

    Result<JobResp> queryJob(Long jobId);

    Result<PageInfo<JobResp>> queryJobs(JobReq jobReq);

    Result uploadJar(Long jobId, MultipartFile file);

    Result<List<String>> jarList(Long jobId);

    Result startJob(Long jobId);

    Result stopJob(Long jobId);

    Result reStartJob(Long jobId);

}
