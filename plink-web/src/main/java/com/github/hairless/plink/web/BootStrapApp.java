package com.github.hairless.plink.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;


@MapperScan("com.github.hairless.plink.dao.mapper")
@SpringBootApplication(scanBasePackages = {"com.github.hairless.plink"})
public class BootStrapApp {

    public static void main(String[] args) {
        SpringApplication.run(BootStrapApp.class, args);
    }
}
