package com.github.hairless.plink.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.github.hairless.plink.model.req.JobReq;
import com.github.hairless.plink.model.resp.JobResp;
import com.github.hairless.plink.model.resp.Result;
import com.github.hairless.plink.service.JobService;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import netscape.javascript.JSObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

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
    private JobService jobService;

    /**
     * 添加作业
     *
     * @param jobReq
     * @return
     */
    @RequestMapping("/addJob")
    public Result<JobResp> addJob(@RequestBody JobReq jobReq) {
        return jobService.addJob(jobReq);
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
     * @param jobId
     * @return
     */
    @RequestMapping(value = "/deleteJob/{jobId}", method = RequestMethod.POST)
    public Result deleteJob(@PathVariable(value = "jobId") @NotNull Long jobId) {
        return jobService.deleteJob(jobId);
    }

    /**
     * 编辑作业
     *
     * @param jobReq
     * @return
     */
    @RequestMapping("/updateJob")
    public Result updateJob(@RequestBody @Valid JobReq jobReq) {
        return jobService.updateJob(jobReq);
    }

    /**
     * 查询单个作业
     *
     * @param jobId
     * @return
     */
    @RequestMapping("/queryJob/{jobId}")
    public Result<JobResp> queryJob(@PathVariable(value = "jobId") @NotNull Long jobId) {
        return jobService.queryJob(jobId);
    }

    /**
     * 查询作业列表
     *
     * @return
     */
    @RequestMapping("/queryJobs")
    public Result<PageInfo<JobResp>> queryJobs(@RequestBody(required = false) JobReq jobReq) {
        return jobService.queryJobs(jobReq);
    }

    /**
     * 作业上传jar
     */
    @RequestMapping("{jobId}/uploadJar")
    public Result uploadJar(@PathVariable(value = "jobId") @NotNull Long jobId, @RequestParam("file") MultipartFile file) {
        return jobService.uploadJar(jobId, file);
    }

    /**
     * 作业获取jar列表
     */
    @RequestMapping("{jobId}/jarList")
    public Result jarList(@PathVariable(value = "jobId") @NotNull Long jobId) {
        return jobService.jarList(jobId);
    }

    /**
     *
     *
     * @param jsonObject
     * @return
     */
    @RequestMapping(value = "/startJob/standaloneMode",method = RequestMethod.POST)
    public Result startJobStandalone(@RequestBody JSONObject jsonObject) throws HttpProcessException {

        return jobService.startJob(jsonObject);
    }



}
