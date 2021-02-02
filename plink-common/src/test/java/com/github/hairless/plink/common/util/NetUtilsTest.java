package com.github.hairless.plink.common.util;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class NetUtilsTest {

    @Test
    public void getHost() {
        String host = NetUtils.getHost();
        Assert.assertEquals("192.168.31.32",host);
    }
}