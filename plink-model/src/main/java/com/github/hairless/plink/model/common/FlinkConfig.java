package com.github.hairless.plink.model.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * @author: silence
 * @date: 2020/1/17
 */
@Getter
@Setter
@NoArgsConstructor
public class FlinkConfig implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * jar包名称
     */
    @NotBlank(message = "jarName must not be empty")
    private String jarName;
    /**
     * 主类全路径
     */
    @NotBlank(message = "entryClass must not be empty")
    private String entryClass;
    /**
     * main方法参数
     */
    private String programArgs;

    private String[] programArgsList;
    /**
     * jobManager 内存
     * 目前通过api 提交任务不支持这个参数
     */
    private String jobManagerMemory;
    /**
     * taskManager 内存
     * 目前通过api 提交任务不支持这个参数
     */
    private String taskManagerMemory;
    /**
     * taskManager slot数量
     * 目前通过api 提交任务不支持这个参数
     */
    private Integer taskManagerSlots;
    /**
     * 作业并行度 parallelism
     */
    private Integer parallelism;
    /**
     * 其他flink配置
     */
    private List<String> configs;

    private String savepointPath;
}
