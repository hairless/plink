package com.github.hairless.plink.model.pojo;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Id;
import javax.persistence.OrderBy;
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
    @NotNull(message = "id must not be null")
    @Min(value = 1, message = "id must be a positive integer")
    @OrderBy("desc")
    private Long id;
    /**
     * 记录创建时间
     */
    private Date createTime;
    /**
     * 记录修改时间
     */
    private Date updateTime;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
