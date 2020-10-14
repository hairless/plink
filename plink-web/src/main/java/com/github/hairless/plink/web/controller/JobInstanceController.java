package com.github.hairless.plink.web.controller;

import com.github.hairless.plink.model.dto.JobInstanceDTO;
import com.github.hairless.plink.model.req.PageReq;
import com.github.hairless.plink.model.resp.Result;
import com.github.hairless.plink.model.resp.ResultCode;
import com.github.hairless.plink.service.JobInstanceService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

/**
 * JobInstance
 *
 * @Author Trevor
 * @Create 2020/1/21 9:50
 */
@RestController
@RequestMapping("/mng/jobInstance")
public class JobInstanceController {
    @Autowired
    private JobInstanceService jobInstanceService;

    /**
     * 查询作业列表
     */
    @RequestMapping("/queryJobInstances")
    public Result<PageInfo<JobInstanceDTO>> queryJobInstances(JobInstanceDTO jobInstanceDTO, PageReq pageReq) {
        PageInfo<JobInstanceDTO> jobInstanceDTOPageInfo = jobInstanceService.queryJobInstances(jobInstanceDTO, pageReq);
        return new Result<>(ResultCode.SUCCESS, jobInstanceDTOPageInfo);
    }

    /**
     * 查询实例启动日志
     *
     * @param jobInstanceId 作业实例id
     */
    @RequestMapping("/startLog/{jobInstanceId}")
    public Result<String> queryJob(@PathVariable(value = "jobInstanceId") @NotNull Long jobInstanceId) {
        String startLog = jobInstanceService.startLog(jobInstanceId);
        return new Result<>(ResultCode.SUCCESS, ResultCode.SUCCESS.getDesc(), startLog);
    }
}
