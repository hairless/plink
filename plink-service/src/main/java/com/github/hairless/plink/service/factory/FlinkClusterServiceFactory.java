package com.github.hairless.plink.service.factory;

import com.github.hairless.plink.service.FlinkClusterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: silence
 * @date: 2020/1/19
 */
@Service
public class FlinkClusterServiceFactory {

    public static String DEFAULT_CLUSTER_MODE = "standalone";
    public static String FLINK_CLUSTER_SERVICE_SUFFIX = "FlinkClusterServiceImpl";

    @Autowired
    Map<String, FlinkClusterService> flinkClusterServiceMap = new ConcurrentHashMap<>();

    public FlinkClusterService getDefaultFlinkClusterService() {
        return getFlinkClusterService(DEFAULT_CLUSTER_MODE);
    }

    public FlinkClusterService getFlinkClusterService(String mode) {
        FlinkClusterService flinkClusterService = flinkClusterServiceMap.get(mode + FLINK_CLUSTER_SERVICE_SUFFIX);
        if (flinkClusterService == null) {
            throw new RuntimeException("no flinkClusterService defined");
        }
        return flinkClusterService;
    }
}
