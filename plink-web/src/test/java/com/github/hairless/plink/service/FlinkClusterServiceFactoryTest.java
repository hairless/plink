package com.github.hairless.plink.service;

import com.github.hairless.plink.TestBootStrapApp;
import com.github.hairless.plink.service.factory.FlinkClusterServiceFactory;
import com.github.hairless.plink.service.impl.StandaloneFlinkClusterServiceImpl;
import com.github.hairless.plink.service.impl.YarnFlinkClusterServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author: silence
 * @date: 2020/9/11
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestBootStrapApp.class})
public class FlinkClusterServiceFactoryTest {

    @Autowired
    private FlinkClusterServiceFactory flinkClusterServiceFactory;

    @Test
    public void getDefaultFlinkClusterService() {
        FlinkClusterService defaultFlinkClusterService = flinkClusterServiceFactory.getDefaultFlinkClusterService();
        assert defaultFlinkClusterService != null;
    }

    @Test
    public void getFlinkClusterService() {
        FlinkClusterService standAloneFlinkClusterService = flinkClusterServiceFactory.getFlinkClusterService("standalone");
        assert standAloneFlinkClusterService instanceof StandaloneFlinkClusterServiceImpl;

        FlinkClusterService yarnFlinkClusterService = flinkClusterServiceFactory.getFlinkClusterService("yarn");
        assert yarnFlinkClusterService instanceof YarnFlinkClusterServiceImpl;
    }
}