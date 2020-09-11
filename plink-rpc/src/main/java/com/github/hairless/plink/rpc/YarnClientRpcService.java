package com.github.hairless.plink.rpc;

import com.github.hairless.plink.model.exception.PlinkException;
import org.apache.hadoop.yarn.api.records.YarnApplicationState;

/**
 * @author: silence
 * @date: 2020/9/4
 */
public interface YarnClientRpcService {
    void killApplication(String appId) throws PlinkException;

    YarnApplicationState getYarnApplicationState(String appId) throws PlinkException;
}
