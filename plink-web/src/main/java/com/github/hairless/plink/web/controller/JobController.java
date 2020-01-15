package com.github.hairless.plink.web.controller;

import com.github.hairless.plink.model.pojo.Job;
import com.github.hairless.plink.model.req.JobReq;
import com.github.hairless.plink.model.resp.Result;
import com.github.hairless.plink.service.impl.JobServiceImpl;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * job
 *
 * @Author Trevor
 * @Create 2020/1/14 14:19
 */

@RestController
@Slf4j
@RequestMapping("/mng/job")
public class JobController {
    @Autowired
    private JobServiceImpl jobService;

    /**
     * 添加作业
     *
     * @param job
     * @return
     */
    @RequestMapping("/addJob")
    public Result<Job> addJob(@RequestBody Job job) {
        return jobService.addJob(job);
    }

    /**
     * 批量删除作业
     *
     * @param idList
     * @return
     */
    @RequestMapping(value = "/deleteJobs", method = RequestMethod.POST)
    public Result deleteJobs(@RequestBody List<Long> idList) {
        return jobService.deleteJobs(idList);
    }

    /**
     * 删除作业
     *
     * @param idList
     * @return
     */
    @RequestMapping(value = "/deleteJob/{jobId}", method = RequestMethod.POST)
    public Result deleteJob(@PathVariable(value = "jobId") Long jobId) {
        return jobService.deleteJob(jobId);
    }

    /**
     * 编辑作业
     *
     * @param job
     * @return
     */
    @RequestMapping("/updateJob")
    public Result updateJob(@RequestBody @Valid Job job) {
        return jobService.updateJob(job);
    }

    /**
     * 查询单个作业
     *
     * @param jobId
     * @return
     */
    @RequestMapping("/queryJob/{jobId}")
    public Result<Job> queryJob(@PathVariable(value = "jobId") Long jobId) {
        return jobService.queryJob(jobId);
    }

    /**
     * 查询作业列表
     *
     * @return
     */
    @RequestMapping("/queryJobs")
    public Result<PageInfo<Job>> queryJobs(@RequestBody JobReq jobReq) {
        return jobService.queryJobs(jobReq);
    }

}
