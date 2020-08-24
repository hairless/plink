package com.github.hairless.plink.sql;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: silence
 * @date: 2020/8/14
 */
@Slf4j
public class SqlJobDriver {

    public static void main(String[] args) throws Exception {
        SqlJob sqlJob = SqlJobFactory.create(args);
        sqlJob.start();
    }
}
