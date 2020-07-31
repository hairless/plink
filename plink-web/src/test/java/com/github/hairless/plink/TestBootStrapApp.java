package com.github.hairless.plink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

@EnableTransactionManagement
@MapperScan("com.github.hairless.plink.dao.mapper")
@SpringBootApplication(scanBasePackages = {"com.github.hairless.plink"})
public class TestBootStrapApp {

    public static void main(String[] args) {
        SpringApplication.run(TestBootStrapApp.class, args);
    }
}
