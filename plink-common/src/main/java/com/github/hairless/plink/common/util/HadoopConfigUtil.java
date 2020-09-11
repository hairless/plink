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
    private static final String CONF_SUFFIX = "/etc/hadoop";
    private static final Map<String, Configuration> configurationMap = new ConcurrentHashMap<>();


    public static String getHadoopHome() throws PlinkException {
        String hadoopHome = System.getenv("HADOOP_HOME");
        if (StringUtils.isBlank(hadoopHome)) {
            throw new PlinkException("HADOOP_HOME is not set!");
        }
        return hadoopHome;
    }

    private static synchronized void loadConfiguration(String hadoopHome) {
        if (!configurationMap.containsKey(hadoopHome)) {
            Preconditions.checkArgument(StringUtils.isNotBlank(hadoopHome), "hadoopHome is empty");
            Collection<File> files = FileUtils.listFiles(new File(hadoopHome, CONF_SUFFIX), new String[]{"xml"}, false);
            Configuration conf = new Configuration();
            if (CollectionUtils.isNotEmpty(files)) {
                for (File file : files) {
                    conf.addResource(new Path(file.getAbsolutePath()));
                }
            }
            configurationMap.put(hadoopHome, conf);
        }
    }

    public static synchronized Configuration getConfiguration() throws PlinkException {
        return getConfiguration(getHadoopHome());
    }

    public static synchronized Configuration getConfiguration(String hadoopHome) throws PlinkException {
        if (!configurationMap.containsKey(hadoopHome)) {
            loadConfiguration(hadoopHome);
        }
        return configurationMap.get(hadoopHome);
    }

}
