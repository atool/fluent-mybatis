package cn.org.atool.fluent.mybatis.metadata;

import lombok.Getter;

import static cn.org.atool.fluent.mybatis.mapper.StrConstant.*;

/**
 * DbType 数据库类型
 *
 * @author darui.wu Created by darui.wu on 2020/6/16.
 */
public enum DbType {
    /**
     * MYSQL
     */
    MYSQL("mysql", '`', '`', "SELECT LAST_INSERT_ID() AS ID"),
    /**
     * MARIADB
     */
    MARIADB("mariadb", '`', '`', "SELECT LAST_INSERT_ID() AS ID"),
    /**
     * H2 '
     */
    H2("h2", SINGLE_QUOTATION, SINGLE_QUOTATION),
    /**
     * SQLITE
     * https://www.sqlite.org/lang_keywords.html
     */
    SQLITE("sqlite", DOUBLE_QUOTATION, DOUBLE_QUOTATION),
    /**
     * ORACLE
     */
    ORACLE("oracle", DOUBLE_QUOTATION, DOUBLE_QUOTATION, "select SEQ_USER_ID.nextval as id from dual", true),
    /**
     * DB2
     */
    DB2("db2"),
    /**
     * HSQL
     */
    HSQL("hsql"),
    /**
     * POSTGRE
     */
    POSTGRE_SQL("postgresql", DOUBLE_QUOTATION, DOUBLE_QUOTATION),
    /**
     * SQLSERVER2005
     */
    SQL_SERVER2005("sqlserver2005", '[', ']'),
    /**
     * SQLSERVER
     */
    SQL_SERVER("sqlserver", '[', ']'),
    /**
     * 其它数据库, 按标准语法进行处理
     */
    OTHER("other");

    @Getter
    private final String alias;

    private char startWrapper;

    private char endWrapper;

    @Getter
    private String seq;

    @Getter
    private boolean before = false;

    DbType(String alias) {
        this.alias = alias;
        this.startWrapper = SPACE_CHAR;
        this.endWrapper = SPACE_CHAR;
    }

    DbType(String alias, char startWrapper, char endWrapper) {
        this.alias = alias;
        this.startWrapper = startWrapper;
        this.endWrapper = endWrapper;
    }

    DbType(String alias, char startWrapper, char endWrapper, String seq) {
        this.alias = alias;
        this.seq = seq;
        this.startWrapper = startWrapper;
        this.endWrapper = endWrapper;
    }

    DbType(String alias, char startWrapper, char endWrapper, String seq, boolean before) {
        this.alias = alias;
        this.seq = seq;
        this.before = before;
        this.startWrapper = startWrapper;
        this.endWrapper = endWrapper;
    }

    public String wrap(String column) {
        if (startWrapper != SPACE_CHAR) {
            return startWrapper + column + endWrapper;
        } else {
            return column;
        }
    }

    /**
     * 去掉转义符
     *
     * @param column 可能带转义符的字段名称
     * @return 去掉转义符后的名称
     */
    public String unwrap(String column) {
        int len = column.length();
        if (column.charAt(0) == startWrapper && column.charAt(len - 1) == endWrapper) {
            return column.substring(1, len - 1);
        } else {
            return column;
        }
    }
}