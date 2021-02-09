package com.github.hairless.plink.common.builder;

import com.github.hairless.plink.common.conf.FlinkAutoConfig;
import com.github.hairless.plink.common.util.MapUtils;
import com.github.hairless.plink.common.util.NetUtils;
import com.github.hairless.plink.model.common.FlinkConfig;
import com.github.hairless.plink.model.common.FlinkSubmitOptions;
import com.github.hairless.plink.model.dto.JobDTO;
import com.github.hairless.plink.model.dto.JobInstanceDTO;
import org.apache.commons.text.StringSubstitutor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: silence
 * @date: 2020/10/13
 */
public abstract class JobBuilder {

    public abstract void validate(JobDTO jobDTO);

    protected abstract FlinkSubmitOptions buildFlinkSubmitOptionInternal(JobInstanceDTO jobInstanceDTO);

    public FlinkSubmitOptions buildFlinkSubmitOption(JobInstanceDTO jobInstanceDTO) {
        FlinkConfig flinkConfig = jobInstanceDTO.getFlinkConfig();
        Map<String, String> mergeConfig = mergeConfig(flinkConfig.getConfigs(), jobInstanceDTO);
        flinkConfig.setConfigs(mergeConfig);
        return buildFlinkSubmitOptionInternal(jobInstanceDTO);
    }

    private Map<String, String> mergeConfig(Map<String, String> flinkConfig, JobInstanceDTO jobInstanceDTO) {
        //合并平台默认配置
        Map<String, String> mergeConfig = MapUtils.mergeMap(flinkConfig, FlinkAutoConfig.defaultConfs);

        //替换占位符
        Map<String, Object> valueMap = new HashMap<>();
        valueMap.put("jobId", jobInstanceDTO.getJob().getId());
        valueMap.put("instanceId", jobInstanceDTO.getId());
        valueMap.put("host", NetUtils.getHost());
        StringSubstitutor substitutor = new StringSubstitutor(valueMap);
        mergeConfig.replaceAll((k, v) -> substitutor.replace(v));

        return mergeConfig;
    }

}
