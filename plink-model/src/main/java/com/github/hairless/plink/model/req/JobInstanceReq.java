package com.github.hairless.plink.model.req;

import com.alibaba.fastjson.JSON;
import com.github.hairless.plink.model.common.FlinkConfig;
import com.github.hairless.plink.model.pojo.JobInstance;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Author Trevor
 * @Create 2020/1/21 10:13
 */
@Getter
@Setter
@NoArgsConstructor
public class JobInstanceReq extends JobInstance {
    private static final long serialVersionUID = 1L;

    private FlinkConfig config;

    private int pageNum = 1;
    private int pageSize = 10;

    public JobInstanceReq transform() {
        if (this.config != null) {
            this.setConfigJson(JSON.toJSONString(this.config));
        }
        return this;
    }

}
