package com.github.hairless.plink.model.req;

import com.github.hairless.plink.model.pojo.Job;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author: silence
 * @date: 2020/1/15
 */
@Getter
@Setter
@NoArgsConstructor
public class JobReq extends Job {
    private static final long serialVersionUID = 1L;
    private int pageSize = 1;
    private int pageNum = 10;
}
