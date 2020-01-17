package com.github.hairless.plink.model.req;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.hairless.plink.model.pojo.Job;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author: silence
 * @date: 2020/1/15
 */
@Getter
@Setter
@NoArgsConstructor
public class JobReq extends Job {
    private static final long serialVersionUID = 1L;

    //TODO 属性确定后用FlinkConfig替代
    private JSONObject config;

    private int pageNum = 1;
    private int pageSize = 10;

    public JobReq transform() {
        if (this.config != null) {
            this.setConfigJson(JSON.toJSONString(this.config));
        }
        return this;
    }
}
