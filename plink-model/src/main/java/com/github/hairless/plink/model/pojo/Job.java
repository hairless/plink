package com.github.hairless.plink.model.pojo;

import com.github.hairless.plink.model.enums.JobInstanceStatusEnum;
import com.github.hairless.plink.model.enums.JobTypeEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Flink 作业
 *
 * @author silence
 * @date 2020/01/13
 */
@Getter
@Setter
@Builder
public class Job extends BaseModel {
    /**
     * Flink作业名称，全局唯一
     */
    private String name;
    /**
     * 作业描述
     */
    private String description;
    /**
     * 作业类型
     * {@link JobTypeEnum}
     */
    private Integer type;
    /**
     * Flink 客户端版本
     */
    private String clientVersion;
    /**
     * 作业配置
     */
    private String config;
    /**
     * 最新实例状态
     * {@link JobInstance#status}
     * {@link JobInstanceStatusEnum}
     */
    private Integer lastStatus;
    /**
     * 最新实例对应的集群任务id
     * {@link JobInstance#appId}
     */
    private String lastAppId;
    /**
     * 最新实例启动时间
     * {@link JobInstance#startTime}
     */
    private Date lastStartTime;
    /**
     * 最新实例停止时间
     * {@link JobInstance#stopTime}
     */
    private Date lastStopTime;
}
