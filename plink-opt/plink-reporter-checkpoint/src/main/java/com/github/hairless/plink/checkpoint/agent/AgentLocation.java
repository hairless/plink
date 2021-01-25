package com.github.hairless.plink.checkpoint.agent;

import lombok.Getter;

/**
 * @author lipan
 * @date 2021-01-24
 */
@Getter
public enum AgentLocation {
    JOBMANAGER("jm");
    private String val;

    AgentLocation(String val) {
        this.val = val;
    }

    public static AgentLocation get(String val) {
        for (AgentLocation l : AgentLocation.values()) {
            if (l.getVal().equals(val))
                return l;
        }
        return null;
    }
}
