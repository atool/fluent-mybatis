package cn.org.atool.fluent.mybatis.method.metadata;

import lombok.Getter;

import static cn.org.atool.fluent.mybatis.utility.SqlProviderUtils.*;

/**
 * DbType 数据库类型
 *
 * @author:darui.wu Created by darui.wu on 2020/6/16.
 */
public enum DbType {
    /**
     * MYSQL
     */
    MYSQL("mysql"),
    /**
     * MARIADB
     */
    MARIADB("mariadb"),
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
    ORACLE("oracle"),
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

    DbType(String alias) {
        this.alias = alias;
    }
}