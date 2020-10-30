package com.github.hairless.plink.service;

import com.github.hairless.plink.model.dto.JobDTO;
import com.github.hairless.plink.model.req.PageReq;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author Trevor
 * @Create 2020/1/14 20:25
 */

public interface JobService {
    JobDTO addJob(JobDTO jobDTO);

    void deleteJob(Long jobId);

    void deleteJobs(List<Long> idList);

    void updateJob(JobDTO jobDTO);

    JobDTO queryJob(Long jobId);

    PageInfo<JobDTO> queryJobs(JobDTO jobDTO, PageReq pageReq);

    void uploadJar(Long jobId, MultipartFile file);

    List<String> jarList(Long jobId);

    void startJob(Long jobId);

    void startJob(JobDTO jobDTO);

    void startJobs(List<Long> idList);

    void stopJob(Long jobId);

    void stopJobs(List<Long> idList);

    void reStartJob(Long jobId);

    void reStartJobs(List<Long> idList);

}
