package com.github.hairless.plink.model.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private String jarName;
    /**
     * 主类全路径
     */
    private String mainClass;
    /**
     * main方法参数
     */
    private String args;
    /**
     * jobManager 内存
     */
    private String jobManagerMemory;
    /**
     * taskManager 内存
     */
    private String taskManagerMemory;
    /**
     * taskManager slot数量
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
}
