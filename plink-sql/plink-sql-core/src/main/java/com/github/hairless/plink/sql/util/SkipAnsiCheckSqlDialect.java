package com.github.hairless.plink.sql.util;

import org.apache.calcite.sql.SqlDialect;

/**
 * @author: silence
 * @date: 2020/7/14
 */
public class SkipAnsiCheckSqlDialect extends SqlDialect {
    public static final SqlDialect.Context DEFAULT_CONTEXT;
    public static final SqlDialect DEFAULT;

    public SkipAnsiCheckSqlDialect(SqlDialect.Context context) {
        super(context);
    }

    static {
        DEFAULT_CONTEXT = SqlDialect.EMPTY_CONTEXT.withDatabaseProduct(SqlDialect.DatabaseProduct.UNKNOWN).withIdentifierQuoteString("`");
        DEFAULT = new SkipAnsiCheckSqlDialect(DEFAULT_CONTEXT);
    }

    /**
     * 重写该方法以支持中文
     *
     * @param buf
     * @param charsetName
     * @param val
     */
    public void quoteStringLiteral(StringBuilder buf, String charsetName, String val) {
        buf.append(this.literalQuoteString);
        buf.append(val.replace(this.literalEndQuoteString, this.literalEscapedQuote));
        buf.append(this.literalEndQuoteString);
    }
}
