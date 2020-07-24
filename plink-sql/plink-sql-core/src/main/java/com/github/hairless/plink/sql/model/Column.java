package com.github.hairless.plink.sql.model;

import lombok.Data;

/**
 * @author: silence
 * @date: 2020/7/17
 */
@Data
public class Column {
    private String name;
    private String type;
    private String desc;
}
