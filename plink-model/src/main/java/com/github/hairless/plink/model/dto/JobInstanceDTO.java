package com.github.hairless.plink.model.dto;

import com.alibaba.fastjson.JSONObject;
import com.github.hairless.plink.model.common.FlinkConfig;
import com.github.hairless.plink.model.pojo.JobInstance;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;

/**
 * @Author Trevor
 * @Create 2020/1/21 10:14
 */
@Getter
@Setter
@NoArgsConstructor
public class JobInstanceDTO extends JobInstance {
    private static final long serialVersionUID = 1L;

    private String statusDesc;

    private String uiAddress;

    private JobDTO job;

    @Valid
    private FlinkConfig flinkConfig;

    @Valid
    private JSONObject extraConfig;

}
