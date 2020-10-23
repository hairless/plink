package com.github.hairless.plink.model.pojo;

import com.github.hairless.plink.model.enums.JobClientVersionEnum;
import com.github.hairless.plink.model.enums.JobInstanceStatusEnum;
import com.github.hairless.plink.model.enums.JobTypeEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Flink 作业
 *
 * @author silence
 * @date 2020/01/13
 */
@Getter
@Setter
@NoArgsConstructor
public class Job extends BaseModel {
    private static final long serialVersionUID = 1L;
    /**
     * Flink作业名称，全局唯一
     */
    @NotBlank(message = "job name must not be empty")
    private String name;
    /**
     * 作业描述
     */
    private String description;
    /**
     * 作业类型
     * {@link JobTypeEnum}
     */
    @NotNull(message = "job type must not be null")
    private Integer type;
    /**
     * Flink 客户端版本
     * {@link JobClientVersionEnum}
     */
    private String clientVersion;
    /**
     * 作业flink参数配置
     */
    private String flinkConfigJson;
    /**
     * 作业额外配置
     */
    private String extraConfigJson;
    /**
     * 最新实例ID
     */
    private Long lastInstanceId;
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
