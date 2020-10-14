package com.github.hairless.plink.sql;

import com.github.hairless.plink.sql.model.SqlConfig;
import com.github.hairless.plink.sql.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.stream.Collectors;

/**
 * @author: silence
 * @date: 2020/8/24
 */
@Slf4j
public class SqlJobFactory {
    public static final Options options = new Options();

    static {
        options.addOption("f", "file", true, "sql config file");
        options.addOption("c", "config", true, "sql config");
        //options.addOption("n", "name", true, "job name");
    }

    public static SqlJob create(String[] args) throws Exception {
        String configJson;
        CommandLine commandLine = new DefaultParser().parse(options, args);
        if (commandLine.hasOption("f")) {
            String file = commandLine.getOptionValue("file");
            log.info("load config from file {}", file);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            configJson = bufferedReader.lines().collect(Collectors.joining());
        } else if (commandLine.hasOption("c")) {
            configJson = commandLine.getOptionValue("config");
        } else {
            throw new RuntimeException("please set -f or -c for sqlConfig");
        }
        SqlConfig sqlConfig = JsonUtil.parseObject(configJson, SqlConfig.class);
        return new SqlJob(sqlConfig);
    }
}
