package com.github.hairless.plink.sql.model.exception;

import com.github.hairless.plink.sql.model.sqlparse.SqlParsePos;

/**
 * @author: silence
 * @date: 2020/12/17
 */
public class PlinkSqlParseException extends Exception {
    private static final long serialVersionUID = -1;

    private SqlParsePos pos;

    public PlinkSqlParseException(String message, int lineNumber, int columnNumber, int endLineNumber, int endColumnNumber) {
        super(message);
        this.pos = new SqlParsePos(lineNumber, columnNumber, endLineNumber, endColumnNumber);
    }

    public SqlParsePos getPos() {
        return pos;
    }
}

