package com.github.hairless.plink.web.controller;

import com.github.hairless.plink.model.enums.JobClientVersionEnum;
import com.github.hairless.plink.model.enums.JobInstanceStatusEnum;
import com.github.hairless.plink.model.enums.JobStateInfoTypeEnum;
import com.github.hairless.plink.model.enums.JobTypeEnum;
import com.github.hairless.plink.model.resp.Result;
import com.github.hairless.plink.model.resp.ResultCode;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: silence
 * @date: 2020/2/3
 */
@RestController
@RequestMapping("/mng/enum")
public class EnumController {
    @RequestMapping("/jobInstanceStatus")
    public Result jobInstanceStatus() {
        return base(JobInstanceStatusEnum.class);
    }

    @RequestMapping("/jobType")
    public Result jobType() {
        return base(JobTypeEnum.class);
    }

    @RequestMapping("/jobClientVersion")
    public Result jobClientVersion() {
        return base(JobClientVersionEnum.class);
    }

    @RequestMapping("/jobStateInfoType")
    public Result jobStateInfoType() {
        return base(JobStateInfoTypeEnum.class);
    }

    private <T extends Class> Result base(T enumClass) {
        return new Result<>(ResultCode.SUCCESS, enumClass.getEnumConstants());
    }
}
