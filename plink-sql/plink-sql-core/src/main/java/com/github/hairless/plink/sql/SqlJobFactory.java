package com.github.hairless.plink.sql;

import com.alibaba.fastjson.JSON;
import com.github.hairless.plink.sql.model.SqlConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
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

    public static SqlJob create(String[] args) throws ParseException, FileNotFoundException {
        SqlConfig sqlConfig;
        CommandLine commandLine = new DefaultParser().parse(options, args);
        if (commandLine.hasOption("f")) {
            String file = commandLine.getOptionValue("file");
            log.info("load config from file {}", file);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String configJson = bufferedReader.lines().collect(Collectors.joining());
            sqlConfig = JSON.parseObject(configJson, SqlConfig.class);
        } else if (commandLine.hasOption("c")) {
            String configJson = commandLine.getOptionValue("config");
            sqlConfig = JSON.parseObject(configJson, SqlConfig.class);
        } else {
            throw new RuntimeException("please set -f or -c for sqlConfig");
        }
        return new SqlJob(sqlConfig);
    }
}
