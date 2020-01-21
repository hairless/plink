package com.github.hairless.plink.web.controller;

import com.github.hairless.plink.model.pojo.JobInstance;
import com.github.hairless.plink.model.req.JobInstanceReq;
import com.github.hairless.plink.model.resp.JobInstanceResp;
import com.github.hairless.plink.model.resp.JobResp;
import com.github.hairless.plink.model.resp.Result;
import com.github.hairless.plink.service.JobInstanceService;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * JobInstance
 *
 * @Author Trevor
 * @Create 2020/1/21 9:50
 */
@RestController
@Slf4j
@RequestMapping("/mng/jobInstance")
public class JobInstanceController {
    @Autowired
    private JobInstanceService jobInstanceService;

    /**
     * 查询作业列表
     */
    @RequestMapping("/queryJobInstances")
    public Result<PageInfo<JobInstanceResp>> queryJobInstances(@RequestBody(required = false) JobInstanceReq jobInstanceReq) {
        return jobInstanceService.queryJobInstances(jobInstanceReq);
    }
}
