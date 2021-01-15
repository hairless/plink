package com.github.hairless.plink.web.controller;

import com.github.hairless.plink.model.dto.JobStateInfoDTO;
import com.github.hairless.plink.model.req.PageReq;
import com.github.hairless.plink.model.resp.Result;
import com.github.hairless.plink.model.resp.ResultCode;
import com.github.hairless.plink.service.JobStateInfoService;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chaixiaoxue
 * @date 2021/1/14 18:21
 */
@Slf4j
@RestController
@RequestMapping("/mng/jobStateInfo")
public class JobStateInfoController {

    @Autowired
    private JobStateInfoService jobStateInfoService;

    /**
     * 查询状态列表
     */
    @RequestMapping("/queryJobStateInfos")
    public Result<PageInfo<JobStateInfoDTO>> queryJobStateInfos(JobStateInfoDTO jobStateInfoDTO, PageReq pageReq) {
        log.info("the method is queryJobStateInfos, params is {}",jobStateInfoDTO.toString());
        PageInfo<JobStateInfoDTO> jobStateInfoDTOPageInfo = jobStateInfoService.queryJobStateInfos(jobStateInfoDTO, pageReq);
        return new Result<>(ResultCode.SUCCESS, jobStateInfoDTOPageInfo);
    }

    /**
     * 查询状态详情
     */
    @RequestMapping("/queryJobStateInfo")
    public Result<JobStateInfoDTO> queryJobStateInfo(Long jobStateInfoId) {
        log.info("the method is queryJobStateInfo, params is {}",jobStateInfoId);
        JobStateInfoDTO jobStateInfoDTO = jobStateInfoService.queryJobStateInfo(jobStateInfoId);
        return new Result<>(ResultCode.SUCCESS, jobStateInfoDTO);
    }

    /**
     * 新增状态
     */
    @RequestMapping(value = "/addJobStateInfo",method = RequestMethod.POST)
    public Result<JobStateInfoDTO> addJobStateInfo(@RequestBody JobStateInfoDTO jobStateInfoDTO) {
        log.info("the method is addJobStateInfo, params is {}",jobStateInfoDTO.toString());
        JobStateInfoDTO jobStateInfo = jobStateInfoService.addJobStateInfo(jobStateInfoDTO);
        return new Result<>(ResultCode.SUCCESS, jobStateInfo);
    }

    /**
     * 删除状态
     */
    @RequestMapping("/deleteJobStateInfo")
    public Result deleteJobStateInfo(Long jobStateInfoId) {
        log.info("the method is queryJobStateInfo, params is {}",jobStateInfoId);
        jobStateInfoService.deleteJobStateInfo(jobStateInfoId);
        return new Result<>(ResultCode.SUCCESS);
    }
}
