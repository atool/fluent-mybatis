package cn.org.atool.fluent.mybatis.metadata;

import lombok.Getter;

import static cn.org.atool.fluent.mybatis.mapper.StrConstant.DOUBLE_QUOTATION;

/**
 * DbType 数据库类型
 *
 * @author:darui.wu Created by darui.wu on 2020/6/16.
 */
public enum DbType {
    /**
     * MYSQL
     */
    MYSQL("mysql", "SELECT LAST_INSERT_ID() AS ID") {
        @Override
        public String wrap(String column) {
            return '`' + column + '`';
        }
    },
    /**
     * MARIADB
     */
    MARIADB("mariadb", "SELECT LAST_INSERT_ID() AS ID") {
        @Override
        public String wrap(String column) {
            return '`' + column + '`';
        }
    },
    /**
     * H2
     */
    H2("h2"),
    /**
     * SQLITE
     * https://www.sqlite.org/lang_keywords.html
     */
    SQLITE("sqlite") {
        @Override
        public String wrap(String column) {
            return DOUBLE_QUOTATION + column + DOUBLE_QUOTATION;
        }
    },
    /**
     * ORACLE
     */
    ORACLE("oracle", "select SEQ_USER_ID.nextval as id from dual", true) {
        @Override
        public String wrap(String column) {
            return DOUBLE_QUOTATION + column + DOUBLE_QUOTATION;
        }
    },
    /**
     * DB2
     */
    DB2("db2"),
    /**
     * HSQL
     */
    HSQL("hsql") {
        @Override
        public String wrap(String column) {
            return DOUBLE_QUOTATION + column + DOUBLE_QUOTATION;
        }
    },
    /**
     * POSTGRE
     */
    POSTGRE_SQL("postgresql") {
        @Override
        public String wrap(String column) {
            return DOUBLE_QUOTATION + column + DOUBLE_QUOTATION;
        }
    },
    /**
     * SQLSERVER2005
     */
    SQL_SERVER2005("sqlserver2005") {
        @Override
        public String wrap(String column) {
            return "[" + column + "]";
        }
    },
    /**
     * SQLSERVER
     */
    SQL_SERVER("sqlserver") {
        @Override
        public String wrap(String column) {
            return "[" + column + "]";
        }
    },
    /**
     * 其它数据库, 按标准语法进行处理
     */
    OTHER("other");

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

    public String wrap(String column) {
        return column;
    }
}