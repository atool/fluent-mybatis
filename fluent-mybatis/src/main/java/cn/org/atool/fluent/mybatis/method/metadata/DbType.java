package cn.org.atool.fluent.mybatis.method.metadata;

import lombok.Getter;

import static cn.org.atool.fluent.mybatis.method.model.XmlConstant.*;

/**
 * DbType 数据库类型
 *
 * @author:darui.wu Created by darui.wu on 2020/6/16.
 */
public enum DbType {
    /**
     * MYSQL
     */
    MYSQL("mysql", true),
    /**
     * MARIADB
     */
    MARIADB("mariadb", true),
    /**
     * H2
     */
    H2("h2", true),
    /**
     * SQLITE
     */
    SQLITE("sqlite", true),
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
    POSTGRE_SQL("postgresql", true),
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
                    .append(String.format(" WHERE ROW_ID > %s AND ROW_ID <= %s ", Wrapper_Paged_Offset, Wrapper_Paged_End_Offset))
                    .toString();
            case DB2:
                return new StringBuilder(noPagedXml.length() + 200)
                    .append("SELECT * FROM (SELECT TMP_PAGE.*,ROWNUMBER() OVER() AS ROW_ID FROM ( ")
                    .append(noPagedXml)
                    .append(" ) AS TMP_PAGE) TMP_PAGE WHERE ROW_ID")
                    .append(String.format(" BETWEEN %s AND %s", Wrapper_Paged_Offset, Wrapper_Paged_Size))
                    .toString();
            case SQL_SERVER:
            case SQL_SERVER2005:
                throw new RuntimeException("not support");
            case HSQL:
                return noPagedXml + String.format(" LIMIT %s OFFSET %s", Wrapper_Paged_Size, Wrapper_Paged_Offset);
            case MYSQL:
            case MARIADB:
            case SQLITE:
            case POSTGRE_SQL:
            case H2:
            default:
                return noPagedXml + String.format(" LIMIT %s, %s ", Wrapper_Paged_Offset, Wrapper_Paged_Size);
        }
    }

    /**
     * 是否可以直接limit
     */
    @Getter
    private final boolean canDirectLimit;

    DbType(String alias) {
        this(alias, false);
    }

    DbType(String alias, boolean canDirectLimit) {
        this.alias = alias;
        this.canDirectLimit = canDirectLimit;
    }
}