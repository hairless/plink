package com.github.hairless.plink.sql.model.sqlparse;

import lombok.Data;

import java.util.List;

/**
 * @author: silence
 * @date: 2020/7/17
 */
@Data
public class SqlParseInfo {

    private List<SqlParseNode> nodeList;

    private List<SqlParseLink> linkList;

}

