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
    private static final Map<String, Configuration> configurationMap = new ConcurrentHashMap<>();

    public static String getHadoopHome() throws PlinkException {
        String hadoopHome = System.getenv("HADOOP_HOME");
        if (StringUtils.isBlank(hadoopHome)) {
            throw new PlinkException("HADOOP_HOME is not set!");
        }
        return hadoopHome;
    }

    public static String getHadoopConfDir() throws PlinkException {
        String hadoopHome = System.getenv("HADOOP_CONF_DIR");
        if (StringUtils.isBlank(hadoopHome)) {
            throw new PlinkException("HADOOP_HOME is not set!");
        }
        return hadoopHome;
    }

    private static synchronized void loadConfiguration(String hadoopConfDir) {
        if (!configurationMap.containsKey(hadoopConfDir)) {
            Preconditions.checkArgument(StringUtils.isNotBlank(hadoopConfDir), "hadoopConfDir is empty");
            Collection<File> files = FileUtils.listFiles(new File(hadoopConfDir), new String[]{"xml"}, false);
            Configuration conf = new Configuration();
            if (CollectionUtils.isNotEmpty(files)) {
                for (File file : files) {
                    conf.addResource(new Path(file.getAbsolutePath()));
                }
            }
            configurationMap.put(hadoopConfDir, conf);
        }
    }

    public static synchronized Configuration getConfiguration() throws PlinkException {
        return getConfiguration(getHadoopConfDir());
    }

    public static synchronized Configuration getConfiguration(String hadoopConfDir) throws PlinkException {
        if (!configurationMap.containsKey(hadoopConfDir)) {
            loadConfiguration(hadoopConfDir);
        }
        return configurationMap.get(hadoopConfDir);
    }

}
