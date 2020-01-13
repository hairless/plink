package com.github.hairless.plink.model.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import java.util.Date;

/**
 * Created by silence on 2020/01/10.
 */
@Getter
@Setter
public class BaseModel {
    /**
     * 主键自增id
     */
    @Id
    private Long id;
    /**
     * 记录创建时间
     */
    private Date createTime;
    /**
     * 记录修改时间
     */
    private Date updateTime;

}
