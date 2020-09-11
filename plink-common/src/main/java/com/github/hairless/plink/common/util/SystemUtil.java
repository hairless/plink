package com.github.hairless.plink.common.util;

/**
 * @author hy
 * @date 2019/4/9
 * @desc
 */
public class SystemUtil {
    public static final int WINDOWS = 1;
    public static final int UNIX = 2;
    public static final int MACOSX = 3;
    private static int osType;

    public static boolean isWindows() {
        return WINDOWS == getOSType();
    }

    public static boolean isUnix() {
        return UNIX == getOSType() || MACOSX == getOSType();
    }

    public static int getOSType() {
        if (osType == 0) {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.startsWith("windows")) {
                osType = 1;
            } else if (os.contains("os x")) {
                osType = 3;
            } else {
                osType = 2;
            }
        }
        return osType;
    }

}
