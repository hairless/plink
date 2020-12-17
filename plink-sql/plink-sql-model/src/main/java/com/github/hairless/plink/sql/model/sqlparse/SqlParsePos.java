package com.github.hairless.plink.sql.model.sqlparse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: silence
 * @date: 2020/12/17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SqlParsePos {
    private int lineNumber;
    private int columnNumber;
    private int endLineNumber;
    private int endColumnNumber;
}
