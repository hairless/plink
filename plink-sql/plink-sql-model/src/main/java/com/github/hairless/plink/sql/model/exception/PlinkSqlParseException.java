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
        super(message + " at line " + lineNumber + ", column " + columnNumber + " to line " + endLineNumber + ", column " + endColumnNumber);
        this.pos = new SqlParsePos(lineNumber, columnNumber, endLineNumber, endColumnNumber);
    }

    public SqlParsePos getPos() {
        return pos;
    }
}

