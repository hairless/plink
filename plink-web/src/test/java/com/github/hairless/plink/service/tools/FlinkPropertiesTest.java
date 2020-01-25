package com.github.hairless.plink.service.tools;

import com.github.hairless.plink.BaseTest;
import com.github.hairless.plink.service.tools.FlinkConfigFactory;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author aven danxieai@163.com
 * @version 0.1
 * @date 2020/1/24
 */
public class FlinkPropertiesTest extends BaseTest {

    @Autowired
    private FlinkConfigFactory flinkConfigFactory;

    @Test
    public void testConfFilePort() {
        Assert.assertEquals(java.util.Optional.of(8081).get(), flinkConfigFactory.flinkClusterPort());
    }

    @Test
    public void testConfFileHost() {
        Assert.assertEquals(java.util.Optional.of("localhost").get(), flinkConfigFactory.flinkClusterHost());
    }
}
