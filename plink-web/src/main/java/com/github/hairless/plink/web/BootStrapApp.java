package com.github.hairless.plink.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

@EnableScheduling
@EnableTransactionManagement
@MapperScan("com.github.hairless.plink.dao.mapper")
@ConfigurationPropertiesScan("com.github.hairless.plink.*.conf")
@SpringBootApplication(scanBasePackages = {"com.github.hairless.plink"})
public class BootStrapApp {

    public static void main(String[] args) {
        SpringApplication.run(BootStrapApp.class, args);
    }
}
