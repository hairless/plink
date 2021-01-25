package com.github.hairless.plink.checkpoint;

import com.github.hairless.plink.checkpoint.agent.AgentLocation;
import com.github.hairless.plink.checkpoint.container.ConfigurationHolder;
import com.github.hairless.plink.checkpoint.container.ReporterSetupTransformer;

import java.lang.instrument.Instrumentation;

/**
 * @author lipan
 * @date 2021-01-24
 */
public class Main {
    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("start plink monitor agent...");

        if (agentArgs.equals(AgentLocation.JOBMANAGER.getVal())) {
            ConfigurationHolder.setLocation(AgentLocation.get(agentArgs));
            inst.addTransformer(new ReporterSetupTransformer());
        }
    }
}
