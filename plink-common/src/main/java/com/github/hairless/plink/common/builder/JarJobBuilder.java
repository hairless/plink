package com.github.hairless.plink.common.builder;

import com.github.hairless.plink.common.conf.FlinkAutoConfig;
import com.github.hairless.plink.common.util.UploadUtil;
import com.github.hairless.plink.common.util.ValidatorUtil;
import com.github.hairless.plink.model.common.FlinkConfig;
import com.github.hairless.plink.model.common.FlinkSubmitOptions;
import com.github.hairless.plink.model.dto.JobDTO;
import com.github.hairless.plink.model.dto.JobInstanceDTO;
import org.apache.flink.util.Preconditions;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: silence
 * @date: 2020/10/13
 */
public class JarJobBuilder implements JobBuilder {

    @Override
    public void validate(JobDTO jobDTO) {
        Preconditions.checkNotNull(jobDTO, "jobDTO is null");
        FlinkConfig flinkConfig = jobDTO.getFlinkConfig();
        ValidatorUtil.validate(flinkConfig);
    }

    @Override
    public FlinkSubmitOptions buildFlinkSubmitOption(JobInstanceDTO jobInstanceDTO) {
        FlinkSubmitOptions flinkSubmitOptions = new FlinkSubmitOptions();
        flinkSubmitOptions.setJobName("PLINK_JAR_" + jobInstanceDTO.getJob().getName());
        flinkSubmitOptions.setMainJarPath(UploadUtil.getJobJarsPath(jobInstanceDTO.getJobId(), jobInstanceDTO.getFlinkConfig().getJarName()));
        FlinkConfig flinkConfig = jobInstanceDTO.getFlinkConfig();
        Map<String, String> defaultConfs = FlinkAutoConfig.defaultConfs;
        Map<String, String> configs = flinkConfig.getConfigs() == null ? new HashMap<>() : flinkConfig.getConfigs();
        defaultConfs.forEach((k, v) -> {
            if (!configs.containsKey(k)) {
                configs.put(k, v);
            }
        });
        flinkSubmitOptions.setFlinkConfig(flinkConfig);
        return flinkSubmitOptions;
    }
}
