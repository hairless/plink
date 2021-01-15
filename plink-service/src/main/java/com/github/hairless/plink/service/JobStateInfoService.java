package com.github.hairless.plink.service;

import com.github.hairless.plink.model.dto.JobStateInfoDTO;
import com.github.hairless.plink.model.req.PageReq;
import com.github.pagehelper.PageInfo;

/**
 * @author chaixiaoxue
 * @date 2021/1/14 18:25
 */
public interface JobStateInfoService {

    PageInfo<JobStateInfoDTO> queryJobStateInfos(JobStateInfoDTO jobStateInfoDTO, PageReq pageReq);

    JobStateInfoDTO queryJobStateInfo(Long jobStateInfoId);
}
