package com.github.hairless.plink.sql.model.sqlparse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author: silence
 * @date: 2020/7/17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SqlParseInfo {

    private List<SqlParseNode> nodeList;

    private List<SqlParseLink> linkList;

}

