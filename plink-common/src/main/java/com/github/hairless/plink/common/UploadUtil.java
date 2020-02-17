package com.github.hairless.plink.common;

/**
 * @author: silence
 * @date: 2020/2/17
 */
public class UploadUtil {
    public static String getJobJarsPath() {
        return getUploadBasePath() + "jobJars/";
    }

    public static String getUploadBasePath() {
        String userDir = System.getProperty("user.dir");
        return userDir + "/upload/";
    }
}
