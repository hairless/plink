package com.github.hairless.plink.rpc;

import lombok.Getter;
import lombok.Setter;

/**
 * @author: silence
 * @date: 2020/1/27
 */
public interface FlinkRestRpcService {

    String uploadJar(String localJarPath);

    String runJar(String jarId, RunConfig runConfig);

    String queryJobStatus(String jobId);

    Boolean stopJob(String jobId);

    @Getter
    @Setter
    public class RunConfig {
        private String entryClassName;
        private String programArguments;
        private Integer parallelism;
        private Boolean allowNonRestoredState;
        private String savepointPath;
    }
}
