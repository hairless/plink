package com.github.hairless.plink.service;

import com.github.hairless.plink.model.pojo.Job;
import com.github.hairless.plink.model.req.JobReq;
import com.github.hairless.plink.model.resp.Result;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author Trevor
 * @Create 2020/1/14 20:25
 */

public interface JobService {
    Result<Job> addJob(Job job);

    Result deleteJob(Long jobId);

    Result deleteJobs(List<Long> idList);

    Result updateJob(Job job);

    Result<Job> queryJob(Long jobId);

    Result<PageInfo<Job>> queryJobs(JobReq jobReq);

    Result uploadJar(Long jobId, MultipartFile file);
}
