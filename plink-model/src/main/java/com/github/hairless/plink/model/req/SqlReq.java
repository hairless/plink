package com.github.hairless.plink.model.req;

import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Sql Req
 */
@Getter
@Setter
@NoArgsConstructor
public class SqlReq implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * SQL
     */
    private String sql;

}
