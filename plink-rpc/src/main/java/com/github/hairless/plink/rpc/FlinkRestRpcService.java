package com.github.hairless.plink.rpc;

import lombok.Getter;
import lombok.Setter;

/**
 * @author: silence
 * @date: 2020/1/27
 */
public interface FlinkRestRpcService {

    String uploadJar(String localJarPath);

    void deleteJar(String jarId);

    String runJar(String jarId, RunConfig runConfig);

    String queryJobStatus(String jobId);

    void stopJob(String jobId);

    String getJobUiAddress(String jobId);

    @Getter
    @Setter
    public class RunConfig {
        private String entryClass;
        private String programArgs;
        private Integer parallelism;
        private Boolean allowNonRestoredState;
        private String savepointPath;
    }
}
