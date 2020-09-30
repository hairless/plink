package com.github.hairless.plink.common.util;

/**
 * @author: silence
 * @date: 2020/9/29
 */
public class PlinkUtil {

    public static String getPlinkHome() {
        String plink_home = System.getenv("PLINK_HOME");
        if (plink_home == null) {
            plink_home = System.getProperty("user.dir");
        }
        return plink_home;
    }

}
