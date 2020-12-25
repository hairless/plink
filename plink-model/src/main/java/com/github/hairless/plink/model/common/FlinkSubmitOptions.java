package com.github.hairless.plink.model.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author: silence
 * @date: 2020/8/24
 */
@Getter
@Setter
@NoArgsConstructor
public class FlinkSubmitOptions {
    private String jobName;
    private String savePointPath;
    private List<String> localClasspath;
    private List<String> shapefiles;
    private String mainJarPath;
    private FlinkConfig flinkConfig;
}
