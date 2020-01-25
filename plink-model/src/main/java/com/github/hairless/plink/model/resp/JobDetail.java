package com.github.hairless.plink.model.resp;

import lombok.Data;

import java.io.Serializable;

/**
 * @author aven danxieai@163.com
 * @version 0.1
 * @date 2020/1/25
 * describe flink job detail e.g. jobState, plan
 */
@Data
public class JobDetail implements Serializable {

    private static final long serialVersionUID = -8930691459877752756L;
    private String jid;

    private String name;

    private Boolean isStoppable;

    private String state;

    private Integer startTime;

    private Integer endTime;

    private Integer duration;

    private Integer now;

    private String plan;
}
