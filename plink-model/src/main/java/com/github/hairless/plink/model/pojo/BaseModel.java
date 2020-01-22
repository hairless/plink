package com.github.hairless.plink.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by silence on 2020/01/10.
 */
@Getter
@Setter
@NoArgsConstructor
public class BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键自增id
     */
    @Id
    @NotNull(message = "Id must be not null")
    @Min(value = 1, message = "Id must be a positive integer")
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
