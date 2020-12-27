package com.github.hairless.plink.rpc.impl;

import com.github.hairless.plink.common.util.HadoopConfigUtil;
import com.github.hairless.plink.model.exception.PlinkException;
import com.github.hairless.plink.rpc.YarnClientRpcService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.util.Preconditions;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.hadoop.service.Service.STATE;
import org.apache.hadoop.yarn.api.records.ApplicationReport;
import org.apache.hadoop.yarn.api.records.YarnApplicationState;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.util.ConverterUtils;
import org.apache.hadoop.yarn.webapp.util.WebAppUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author: silence
 * @date: 2020/9/4
 */
@Slf4j
@Service
public class YarnClientRpcServiceImpl implements YarnClientRpcService {
    @Value("${kerberos.keytab}")
    private String kerberosKeytab;
    @Value("${kerberos.principal}")
    private String kerberosPrincipal;

    private YarnClient reusableYarnClient;

    private String resourceManagerAddressCache;

    @Override
    public void killApplication(String appId) throws PlinkException {
        try {
            YarnClient yarnClient = getReusableYarnClient();
            yarnClient.killApplication(ConverterUtils.toApplicationId(appId));
        } catch (Exception e) {
            throw new PlinkException(e);
        }
    }

    @Override
    public YarnApplicationState getYarnApplicationState(String appId) throws PlinkException {
        return getYarnApplicationState(HadoopConfigUtil.getHadoopConfDir(), appId);
    }

    @Override
    public String getResourceManagerAddress() throws PlinkException {
        if (resourceManagerAddressCache == null) {
            resourceManagerAddressCache = WebAppUtils.getResolvedRemoteRMWebAppURLWithScheme(HadoopConfigUtil.getConfiguration());
        }
        return resourceManagerAddressCache;
    }

    public YarnApplicationState getYarnApplicationState(String hadoopHome, String appId) {
        Preconditions.checkArgument(StringUtils.isNotBlank(hadoopHome), "hadoopHome is empty");
        Preconditions.checkArgument(StringUtils.isNotBlank(appId), "appId is empty");
        try {
            YarnClient yarnClient = getReusableYarnClient();
            ApplicationReport report = yarnClient.getApplicationReport(ConverterUtils.toApplicationId(appId));
            Preconditions.checkNotNull(report, "getYarnApplicationReport is null");
            return report.getYarnApplicationState();
        } catch (Exception e) {
            log.error("getYarnApplicationState fail...hadoopHome={},appId={}", hadoopHome, appId, e);
        }
        return null;
    }

    private YarnClient getYarnClient() throws PlinkException, IOException {
        YarnClient yarnClient = YarnClient.createYarnClient();
        Configuration config = HadoopConfigUtil.getConfiguration();
        if (StringUtils.isNotBlank(kerberosKeytab) && StringUtils.isNotBlank(kerberosPrincipal)) {
            UserGroupInformation.setConfiguration(config);
            UserGroupInformation.loginUserFromKeytab(kerberosPrincipal, kerberosKeytab);
        }
        yarnClient.init(config);
        yarnClient.start();
        return yarnClient;
    }

    private synchronized YarnClient getReusableYarnClient() throws PlinkException, IOException {
        if (reusableYarnClient == null || !reusableYarnClient.isInState(STATE.STARTED)) {
            reusableYarnClient = getYarnClient();
        }
        return reusableYarnClient;
    }

}
