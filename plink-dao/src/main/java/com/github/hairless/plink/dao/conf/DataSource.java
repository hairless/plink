package com.github.hairless.plink.dao.conf;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * Created by silence on 2020/01/10
 */
@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
@Primary
@Component
public class DataSource extends DruidDataSource {

}
