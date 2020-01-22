package com.github.hairless.plink.model.resp;

import com.alibaba.fastjson.JSON;
import com.github.hairless.plink.model.common.FlinkConfig;
import com.github.hairless.plink.model.common.Transform;
import com.github.hairless.plink.model.pojo.Job;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

/**
 * @author: silence
 * @date: 2020/1/17
 */
@Getter
@Setter
@NoArgsConstructor
public class JobResp extends Job implements Transform<JobResp, Job> {
    private static final long serialVersionUID = 1L;

    private FlinkConfig config;

    public JobResp transform(Job job) {
        BeanUtils.copyProperties(job, this);
        if (this.getConfigJson() != null) {
            this.setConfig(JSON.parseObject(this.getConfigJson(), FlinkConfig.class));
        } else {
            this.setConfig(new FlinkConfig());
        }
        return this;
    }
}
