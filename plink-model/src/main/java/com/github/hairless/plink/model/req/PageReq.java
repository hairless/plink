package com.github.hairless.plink.model.req;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author: silence
 * @date: 2020/1/27
 */
@Getter
@Setter
@NoArgsConstructor
public class PageReq implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 第几页
     */
    private int pageNum = 1;
    /**
     * 每一页的大小
     */
    private int pageSize = 10;

}
