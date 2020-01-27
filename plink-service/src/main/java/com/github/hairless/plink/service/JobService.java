package com.github.hairless.plink.service;

import com.github.hairless.plink.model.dto.JobDTO;
import com.github.hairless.plink.model.req.PageReq;
import com.github.hairless.plink.model.resp.Result;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author Trevor
 * @Create 2020/1/14 20:25
 */

public interface JobService {
    Result<JobDTO> addJob(JobDTO jobDTO);

    Result deleteJob(Long jobId);

    Result deleteJobs(List<Long> idList);

    Result updateJob(JobDTO jobDTO);

    Result<JobDTO> queryJob(Long jobId);

    Result<PageInfo<JobDTO>> queryJobs(JobDTO jobDTO, PageReq pageReq);

    Result uploadJar(Long jobId, MultipartFile file);

    Result<List<String>> jarList(Long jobId);

    Result startJob(Long jobId);

    Result startJobs(List<Long> idList);

    Result stopJob(Long jobId);

    Result stopJobs(List<Long> idList);

    Result reStartJob(Long jobId);

    Result reStartJobs(List<Long> idList);

}
