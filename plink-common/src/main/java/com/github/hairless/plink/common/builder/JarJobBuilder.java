package com.github.hairless.plink.common.builder;

import com.github.hairless.plink.common.util.UploadUtil;
import com.github.hairless.plink.common.util.ValidatorUtil;
import com.github.hairless.plink.model.common.FlinkConfig;
import com.github.hairless.plink.model.common.FlinkSubmitOptions;
import com.github.hairless.plink.model.dto.JobDTO;
import com.github.hairless.plink.model.dto.JobInstanceDTO;
import org.apache.flink.util.Preconditions;

/**
 * @author: silence
 * @date: 2020/10/13
 */
public class JarJobBuilder extends JobBuilder {

    @Override
    public void validate(JobDTO jobDTO) {
        Preconditions.checkNotNull(jobDTO, "jobDTO is null");
        FlinkConfig flinkConfig = jobDTO.getFlinkConfig();
        ValidatorUtil.validate(flinkConfig);
    }

    @Override
    public FlinkSubmitOptions buildFlinkSubmitOptionInternal(JobInstanceDTO jobInstanceDTO) {
        FlinkSubmitOptions flinkSubmitOptions = new FlinkSubmitOptions();
        flinkSubmitOptions.setJobName("PLINK_JAR_" + jobInstanceDTO.getJob().getName());
        flinkSubmitOptions.setMainJarPath(UploadUtil.getJobJarsPath(jobInstanceDTO.getJobId(), jobInstanceDTO.getFlinkConfig().getJarName()));
        FlinkConfig flinkConfig = jobInstanceDTO.getFlinkConfig();
        flinkConfig.setPrepArgs(flinkConfig.getArgs() == null ? new String[0] : flinkConfig.getArgs().split("\n"));
        flinkSubmitOptions.setFlinkConfig(flinkConfig);
        return flinkSubmitOptions;
    }
}
