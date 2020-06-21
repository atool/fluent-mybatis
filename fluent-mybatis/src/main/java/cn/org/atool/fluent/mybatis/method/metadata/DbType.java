package cn.org.atool.fluent.mybatis.method.metadata;

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
    SQL_SERVER2005("sqlserver2005") {
        @Override
        public String selectByPaged(String noPagedXml) {
            return null;
        }
    },
    /**
     * SQLSERVER
     */
    SQL_SERVER("sqlserver") {
        @Override
        public String selectByPaged(String noPagedXml) {
            return null;
        }
    };

    @Getter
    private String alias;

    /**
     * 分页查询语句
     */
    public String selectByPaged(String noPagedXml) {
        switch (this) {
            case ORACLE:
                return new StringBuilder(noPagedXml.length() + 200)
                    .append("SELECT * FROM ( ")
                    .append(" SELECT TMP_PAGE.*, ROWNUM ROW_ID FROM ( ")
                    .append(noPagedXml)
                    .append(" ) TMP_PAGE)")
                    .append(" WHERE ROW_ID > #{ew.paged.offset} AND ROW_ID <= #{ew.paged.endOffset} ")
                    .toString();
            case DB2:
                return new StringBuilder(noPagedXml.length() + 200)
                    .append("SELECT * FROM (SELECT TMP_PAGE.*,ROWNUMBER() OVER() AS ROW_ID FROM ( ")
                    .append(noPagedXml)
                    .append(" ) AS TMP_PAGE) TMP_PAGE WHERE ROW_ID BETWEEN #{ew.paged.offset} AND #{ew.paged.pageSize}")
                    .toString();
            case SQL_SERVER:
            case SQL_SERVER2005:
                throw new RuntimeException("not support");
            case HSQL:
                return noPagedXml + " LIMIT #{ew.paged.pageSize} OFFSET #{ew.paged.offset}";
            case MYSQL:
            case MARIADB:
            case SQLITE:
            case POSTGRE_SQL:
            case H2:
            default:
                return noPagedXml + " LIMIT #{ew.paged.offset}, #{ew.paged.pageSize} ";
        }
    }

    DbType(String alias) {
        this.alias = alias;
    }
}