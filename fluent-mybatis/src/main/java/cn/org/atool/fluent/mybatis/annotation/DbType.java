package cn.org.atool.fluent.mybatis.annotation;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * FluentMybatis 数据库类型
 *
 * @author darui.wu
 */
@Getter
@AllArgsConstructor
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
     * ORACLE
     */
    ORACLE("oracle"),
    /**
     * DB2
     */
    DB2("db2"),
    /**
     * H2
     */
    H2("h2"),
    /**
     * HSQL
     */
    HSQL("hsql"),
    /**
     * SQLITE
     */
    SQLITE("sqlite"),
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
    SQL_SERVER("sqlserver"),
    /**
     * UNKONWN DB
     */
    OTHER("other");

    /**
     * 数据库名称
     */
    private final String db;

    /**
     * 获取数据库类型（默认 MySql）
     *
     * @param dbType 数据库类型字符串
     */
    public static DbType getDbType(String dbType) {
        DbType[] dts = DbType.values();
        for (DbType dt : dts) {
            if (dt.getDb().equalsIgnoreCase(dbType)) {
                return dt;
            }
        }
        return OTHER;
    }
}