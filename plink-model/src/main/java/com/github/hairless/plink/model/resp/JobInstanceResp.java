package com.github.hairless.plink.model.resp;

import com.alibaba.fastjson.JSON;
import com.github.hairless.plink.model.common.FlinkConfig;
import com.github.hairless.plink.model.common.Transform;
import com.github.hairless.plink.model.pojo.Job;
import com.github.hairless.plink.model.pojo.JobInstance;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import javax.validation.Valid;

/**
 * @Author Trevor
 * @Create 2020/1/21 10:14
 */
@Getter
@Setter
@NoArgsConstructor
public class JobInstanceResp extends JobInstance implements Transform<JobInstanceResp, JobInstance> {
    private static final long serialVersionUID = 1L;

    @Valid
    private FlinkConfig config;

    public JobInstanceResp transform(JobInstance jobInstance) {
        BeanUtils.copyProperties(jobInstance, this);
        if (this.getConfigJson() != null) {
            this.setConfig(JSON.parseObject(this.getConfigJson(), FlinkConfig.class));
        } else {
            this.setConfig(new FlinkConfig());
        }
        return this;
    }

}
