package com.github.hairless.plink.sql.connector.collection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: silence
 * @date: 2020/8/14
 */
public class CollectionDataWarehouse {
    private static final Map<String, Map<String, List<String>>> warehouse = new ConcurrentHashMap<>();
    private static final Map<String, Object> locks = new ConcurrentHashMap<>();

    public static void insertOutputData(String jobIdentifier, String tableName, String data) {
        synchronized (locks.get(jobIdentifier)) {
            Map<String, List<String>> output = warehouse.get(jobIdentifier);
            if (output == null) {
                output = new HashMap<>();
            }
            List<String> sinkOutput = output.get(tableName);
            if (sinkOutput == null) {
                sinkOutput = new ArrayList<>();
            }
            sinkOutput.add(data);
            output.put(tableName, sinkOutput);
            warehouse.put(jobIdentifier, output);
        }
    }

    public static Map<String, List<String>> getData(String jobIdentifier) {
        return warehouse.get(jobIdentifier);
    }

    public static void registerLock(String jobIdentifier) {
        locks.put(jobIdentifier, new Object());
    }

    public static void remove(String jobIdentifier) {
        warehouse.remove(jobIdentifier);
        locks.remove(jobIdentifier);
    }
}
