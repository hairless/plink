package com.github.hairless.plink.service.impl;

import com.github.hairless.plink.common.conf.FlinkAutoConfig;
import com.github.hairless.plink.service.UtilService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author: silence
 * @date: 2020/10/27
 */
@Service
public class UtilServiceImpl implements UtilService {

    @Override
    public Map<String, String> defaultFlinkConfs() {
        return FlinkAutoConfig.defaultConfs;
    }
}
