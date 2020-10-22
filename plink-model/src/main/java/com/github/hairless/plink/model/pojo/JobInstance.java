package com.github.hairless.plink.model.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.hairless.plink.model.enums.JobInstanceStatusEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
public class JobInstance extends BaseModel {
    private static final long serialVersionUID = 1L;
    /**
     * 作业id
     * {@link Job#id}
     */
    private Long jobId;
    /**
     * 实例启动时的镜像作业配置
     * {@link Job#flinkConfigJson}
     */
    private String flinkConfigJson;
    /**
     * 实例启动时的镜像作业配置
     * {@link Job#extraConfigJson}
     */
    private String extraConfigJson;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;
    /**
     * 停止时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date stopTime;
}
