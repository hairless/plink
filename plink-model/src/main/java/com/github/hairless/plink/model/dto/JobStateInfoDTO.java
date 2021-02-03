package com.github.hairless.plink.model.dto;

import com.github.hairless.plink.model.pojo.JobStateInfo;
import lombok.Data;

/**
 * @author chaixiaoxue
 * @date 2021/1/14 17:27
 */
@Data
public class JobStateInfoDTO extends JobStateInfo {
    private String typeDesc;
}
