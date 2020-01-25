package com.github.hairless.plink.service.impl;

/**
 * @author aven danxieai@163.com
 * @version 0.1
 * @date 2020/1/24
 * 定义flink rest api 常量
 */
public interface FlinkApiConstants {

    String version = "/v1";

    String cluster = version + "/cluster";

    String config = version + "/config";

    String jars = version + "/jars";

    String jars_upload = jars + "/upload";

    // 使用 %s 替换 jarId
    String jars_delete = jars + "/%s";

    String jars_plan = jars + "/%s/plan";

    String jars_run = jars + "/%s/run";

    String jobs = version + "/jobs";

    String jobs_state = jobs + "/%s";

}
