package com.github.hairless.plink.model.pojo;

import com.github.hairless.plink.model.enums.JobInstanceStatusEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Flink 作业实例
 *
 * @author silence
 * @date 2020/01/13
 */
@Getter
@Setter
@Builder
public class JobInstance extends BaseModel {
    private static final long serialVersionUID = 1L;
    /**
     * 作业id
     * {@link Job#id}
     */
    private Long jobId;
    /**
     * 实例启动时的镜像作业配置
     * {@link Job#config}
     */
    private String config;
    /**
     * 状态
     * {@link JobInstanceStatusEnum}
     */
    private Integer status;
    /**
     * 提交到集群返回的任务id
     */
    private String appId;
    /**
     * 启动时间
     */
    private Date startTime;
    /**
     * 停止时间
     */
    private Date stopTime;
}
