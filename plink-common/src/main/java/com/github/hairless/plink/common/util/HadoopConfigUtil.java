package com.github.hairless.plink.common.util;

import com.github.hairless.plink.model.exception.PlinkException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.util.Preconditions;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author: silence
 * @date: 2020/9/04
 */
public class HadoopConfigUtil {
    public static final String HADOOP_HOME = "HADOOP_HOME";
    public static final String HADOOP_CONF_DIR = "HADOOP_CONF_DIR";
    public static final String CONF_SUFFIX = "/etc/hadoop";
    private static final Map<String, Configuration> configurationMap = new ConcurrentHashMap<>();


    public static String getHadoopHome() throws PlinkException {
        return FileUtil.getPathFromEnv(HADOOP_HOME);
    }

    public static String getHadoopConfDir(String hadoopHome) throws PlinkException {
        return FileUtil.resolvePath(hadoopHome, CONF_SUFFIX);
    }

    public static String getHadoopConfDir() throws PlinkException {
        try {
            return FileUtil.getPathFromEnv(HADOOP_CONF_DIR);
        } catch (PlinkException e) {
            return getHadoopConfDir(getHadoopHome());
        }
    }

    private static synchronized void loadConfiguration(String hadoopConfDir) throws PlinkException {
        if (!configurationMap.containsKey(hadoopConfDir)) {
            Preconditions.checkArgument(StringUtils.isNotBlank(hadoopConfDir), "hadoopConfDir is empty");
            File hadoopConfDirFile = new File(hadoopConfDir);
            if (!hadoopConfDirFile.exists()) {
                throw new PlinkException(hadoopConfDir + " is not exist!");
            }
            Collection<File> files = FileUtils.listFiles(hadoopConfDirFile, new String[]{"xml"}, false);
            Configuration conf = new Configuration();
            if (CollectionUtils.isNotEmpty(files)) {
                for (File file : files) {
                    conf.addResource(new Path(file.getAbsolutePath()));
                }
            }
            configurationMap.put(hadoopConfDir, conf);
        }
    }

    public static synchronized Configuration getConfigurationFromEnv() throws PlinkException {
        return getConfigurationFromHadoopConfDir(getHadoopConfDir());
    }

    public static synchronized Configuration getConfigurationFromHadoopHome(String hadoopHome) throws PlinkException {
        return getConfigurationFromHadoopConfDir(getHadoopConfDir(hadoopHome));
    }

    public static synchronized Configuration getConfigurationFromHadoopConfDir(String hadoopConfDir) throws PlinkException {
        if (!configurationMap.containsKey(hadoopConfDir)) {
            loadConfiguration(hadoopConfDir);
        }
        return configurationMap.get(hadoopConfDir);
    }

}
