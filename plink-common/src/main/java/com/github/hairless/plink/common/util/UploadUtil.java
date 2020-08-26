package com.github.hairless.plink.common.util;

/**
 * @author: silence
 * @date: 2020/2/17
 */
public class UploadUtil {
    public static String getUploadBasePath() {
        String userDir = System.getProperty("user.dir");
        return userDir + "/upload/";
    }

    public static String getJobJarsPathBase() {
        return getUploadBasePath() + "jobJars/";
    }

    public static String getJobJarsPath(Long jobId) {
        return getJobJarsPathBase() + jobId + "/";
    }

    public static String getJobJarsPath(Long jobId, String jarName) {
        return getJobJarsPath(jobId) + jarName;
    }

}
