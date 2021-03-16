package com.github.hairless.plink.model.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Map;

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
    @NotBlank(message = "mainClass must not be empty")
    private String mainClass;
    /**
     * main方法参数
     */
    private String args;
    /**
     * 处理后的main方法参数
     */
    private transient String[] prepArgs;
    /**
     * 其他flink配置
     */
    private Map<String, String> configs;
    /**
     * 任务提交队列
     */
    private String queue;
}
