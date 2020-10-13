package com.github.hairless.plink.common.builder;

import com.github.hairless.plink.common.util.UploadUtil;
import com.github.hairless.plink.model.common.FlinkSubmitOptions;
import com.github.hairless.plink.model.dto.JobInstanceDTO;

/**
 * @author: silence
 * @date: 2020/10/13
 */
public class FlinkJarSubmitOptionsBuilder implements FlinkSubmitOptionsBuilder {
    @Override
    public FlinkSubmitOptions builder(JobInstanceDTO jobInstanceDTO) {
        FlinkSubmitOptions flinkSubmitOptions = new FlinkSubmitOptions();
        flinkSubmitOptions.setJobName("PLINK_JAR_" + jobInstanceDTO.getJob().getName());
        flinkSubmitOptions.setMainJarPath(UploadUtil.getJobJarsPath(jobInstanceDTO.getJobId(), jobInstanceDTO.getFlinkConfig().getJarName()));
        flinkSubmitOptions.setFlinkConfig(jobInstanceDTO.getFlinkConfig());
        return flinkSubmitOptions;
    }
}
