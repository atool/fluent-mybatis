package cn.org.atool.fluent.mybatis.metadata;

import lombok.Getter;

/**
 * DbType 数据库类型
 *
 * @author:darui.wu Created by darui.wu on 2020/6/16.
 */
public enum DbType {
    /**
     * MYSQL
     */
    MYSQL("mysql", "SELECT LAST_INSERT_ID() AS ID"),
    /**
     * MARIADB
     */
    MARIADB("mariadb", "SELECT LAST_INSERT_ID() AS ID"),
    /**
     * H2
     */
    H2("h2"),
    /**
     * SQLITE
     */
    SQLITE("sqlite"),
    /**
     * ORACLE
     */
    ORACLE("oracle", "select SEQ_USER_ID.nextval as id from dual", true),
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
    POSTGRE_SQL("postgresql"),
    /**
     * SQLSERVER2005
     */
    SQL_SERVER2005("sqlserver2005"),
    /**
     * SQLSERVER
     */
    SQL_SERVER("sqlserver");

    @Getter
    private String alias;

    @Getter
    private String seq;

    @Getter
    private boolean before = false;

    DbType(String alias) {
        this.alias = alias;
    }

    DbType(String alias, String seq) {
        this.alias = alias;
        this.seq = seq;
    }

    DbType(String alias, String seq, boolean before) {
        this.alias = alias;
        this.seq = seq;
        this.before = before;
    }
}