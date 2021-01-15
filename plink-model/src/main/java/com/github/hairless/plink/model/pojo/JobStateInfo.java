package com.github.hairless.plink.model.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author chaixiaoxue
 * @date 2021/1/14 17:11
 */
@Data
public class JobStateInfo extends BaseModel {
    private static final long serialVersionUID = 1L;

    /**
     * 作业id
     * {@link Job#id}
     */
    private Long jobId;

    /**
     * 实例id
     * {@link JobInstance#id}
     */
    private Long instanceId;

    /**
     * 类型 0:checkpoint;1:savepoint
     */
    private Integer type;

    /**
     * 状态保存耗时，单位毫秒
     */
    private Long duration;

    /**
     * 状态大小，单位字节
     */
    private Long size;

    /**
     * 状态持久化路径
     */
    private String externalPath;

    /**
     * 上报时间戳
     */
    private Long reportTimestamp;



}
