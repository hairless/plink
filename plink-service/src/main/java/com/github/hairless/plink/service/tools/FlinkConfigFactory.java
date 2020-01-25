package com.github.hairless.plink.service.tools;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author aven danxieai@163.com
 * @version 0.1
 * @date 2020/1/24
 * 管理flink 配置
 */
@Component
@Slf4j
public class FlinkConfigFactory implements InitializingBean {


    @Value("${flink.conf-file}")
    private String confFilePath;

    @Getter
    private Map<String, Object> properties;

    public Object getProperty(String key) {
        return properties.get(key);
    }

    public String flinkClusterHost() {
        return (String) properties.getOrDefault(FlinkPropertiesEnum.REST_HOST.getKey(),
                FlinkPropertiesEnum.REST_HOST.getDefaultValue());
    }

    public Integer flinkClusterPort() {
        return (Integer) properties.getOrDefault(FlinkPropertiesEnum.REST_PORT.getKey(),
                FlinkPropertiesEnum.REST_PORT.getDefaultValue());
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        properties = new HashMap<>();
        // 从flink-conf.yaml 中加载配置
        // 1 设置文件读取路径 或使用默认classpath中的配置
        // 2 配置文件中被注释的默认值
        // rest.port rest.host
        Yaml yaml = new Yaml();
        InputStream inputStream;
        if ("classpath".equals(confFilePath)) {
            log.debug("load flink-conf.yaml from classpath");
            inputStream = this.getClass().getClassLoader().getResourceAsStream("flink-conf.yaml");
            // 从classpath 中加载
        } else {
            log.debug("load flink-conf.yaml from path: {}", confFilePath);
            try {
                inputStream = new FileInputStream("confFilePath");
            } catch (FileNotFoundException e) {
                log.error("cannot found flink-conf.yaml from {}", confFilePath);
                throw e;
            }
        }
        properties = yaml.load(inputStream);
    }
}
