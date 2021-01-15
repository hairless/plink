package com.github.hairless.plink.web.controller;

import com.github.hairless.plink.model.dto.JobStateInfoDTO;
import com.github.hairless.plink.model.req.PageReq;
import com.github.hairless.plink.model.resp.Result;
import com.github.hairless.plink.model.resp.ResultCode;
import com.github.hairless.plink.service.JobStateInfoService;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
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
        JobStateInfoDTO jobStateInfoDTOPageInfo = jobStateInfoService.queryJobStateInfo(jobStateInfoId);
        return new Result<>(ResultCode.SUCCESS, jobStateInfoDTOPageInfo);
    }
}
