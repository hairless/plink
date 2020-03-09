package com.github.hairless.plink.common;

import com.github.hairless.plink.model.exception.PlinkMessageException;
import com.github.hairless.plink.model.exception.PlinkRuntimeException;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.GlobalConfiguration;
import org.apache.flink.configuration.RestOptions;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * @author: silence
 * @date: 2020/2/17
 */
public class FlinkConfigUtil {

    private static final String CONF_SUFFIX = "/conf";
    private static final String LIB_SUFFIX = "/lib";

    private static final String VERSION_CLASS = "org.apache.flink.runtime.util.EnvironmentInformation";
    private static final String VERSION_METHODS = "getVersion";

    private static volatile Configuration configuration;

    public static String getFlinkHome() throws PlinkMessageException {
        String flinkHome = System.getenv("FLINK_HOME");
        if (StringUtils.isBlank(flinkHome)) {
            throw new PlinkMessageException("FLINK_HOME is not set!");
        }
        return flinkHome;
    }

    private static synchronized void loadConfiguration() throws PlinkMessageException {
        if (configuration == null) {
            configuration = GlobalConfiguration.loadConfiguration(getFlinkHome() + CONF_SUFFIX);
        }
    }

    public static Configuration getConfiguration() throws PlinkMessageException {
        if (configuration == null) {
            loadConfiguration();
        }
        return configuration;
    }

    public static String getRestAddress() throws PlinkMessageException {
        return "http://" + getConfiguration().getValue(RestOptions.ADDRESS) + ":" + getConfiguration().getValue(RestOptions.PORT);
    }

    public static String getFlinkVersion() throws PlinkMessageException {
        try {
            File libDir = new File(getFlinkHome() + LIB_SUFFIX);
            File[] libFiles = libDir.listFiles();
            if (libFiles != null) {
                URL[] jarFileURLs = Arrays.stream(libFiles).flatMap(f -> {
                    try {
                        if (f.getName().contains("flink") && f.getName().endsWith("jar")) {
                            return Stream.of(f.toURI().toURL());
                        }
                    } catch (MalformedURLException ignored) {
                    }
                    return Stream.empty();
                }).toArray(URL[]::new);
                URLClassLoader urlClassLoader = new URLClassLoader(jarFileURLs, null);
                Class<?> versionClass = urlClassLoader.loadClass(VERSION_CLASS);
                Object flinkVersion = versionClass.getMethod(VERSION_METHODS).invoke(null);
                if (flinkVersion != null) {
                    return flinkVersion.toString();
                }
            }
        } catch (PlinkMessageException e) {
            throw e;
        } catch (Exception e) {
            throw new PlinkRuntimeException("get flink version error", e);
        }
        return null;
    }

}
