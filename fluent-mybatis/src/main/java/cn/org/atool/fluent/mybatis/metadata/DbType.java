package cn.org.atool.fluent.mybatis.metadata;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

import static cn.org.atool.fluent.mybatis.metadata.DbEscape.*;

/**
 * DbType 数据库类型
 *
 * @author darui.wu Created by darui.wu on 2020/6/16.
 */
@SuppressWarnings("unused")
public enum DbType {
    /**
     * MYSQL
     */
    MYSQL("mysql", BACK_ESCAPE, "SELECT LAST_INSERT_ID() AS ID"),
    /**
     * MARIADB
     */
    MARIADB("mariadb", BACK_ESCAPE, "SELECT LAST_INSERT_ID() AS ID"),
    /**
     * H2 '
     */
    H2("h2", BACK_ESCAPE),
    /**
     * SQLITE
     * https://www.sqlite.org/lang_keywords.html
     */
    SQLITE("sqlite", D_QUOTATION_ESCAPE),
    /**
     * ORACLE
     */
    ORACLE("oracle", NONE_ESCAPE, "select SEQ_USER_ID.nextval as id from dual", true),
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
    POSTGRE_SQL("postgresql", D_QUOTATION_ESCAPE),
    /**
     * SQLSERVER2005
     */
    SQL_SERVER2005("sqlserver2005", SQUARE_BRACKETS_ESCAPE),
    /**
     * SQLSERVER
     */
    SQL_SERVER("sqlserver", SQUARE_BRACKETS_ESCAPE),
    /**
     * 其它数据库, 按标准语法进行处理
     */
    OTHER("other");

    @Getter
    private final String alias;

    private final DbEscape escape;

    @Getter
    private String seq;

    @Getter
    private boolean before = false;

    DbType(String alias) {
        this.alias = alias;
        this.escape = NONE_ESCAPE;
    }

    DbType(String alias, DbEscape escape) {
        this.alias = alias;
        this.escape = escape;
    }

    DbType(String alias, DbEscape escape, String seq) {
        this.alias = alias;
        this.seq = seq;
        this.escape = escape;
    }

    DbType(String alias, DbEscape escape, String seq, boolean before) {
        this.alias = alias;
        this.seq = seq;
        this.before = before;
        this.escape = escape;
    }

    public String wrap(String column) {
        return DB_ESCAPE.getOrDefault(this, this.escape).wrap(column);
    }

    /**
     * 去掉转义符
     *
     * @param column 可能带转义符的字段名称
     * @return 去掉转义符后的名称
     */
    public String unwrap(String column) {
        return DB_ESCAPE.getOrDefault(this, this.escape).unwrap(column);
    }

    /**
     * 用户指定数据库的反义处理
     */
    private static final Map<DbType, DbEscape> DB_ESCAPE = new HashMap<>();

    /**
     * 设置数据库字段的反义处理
     *
     * @param dbType  数据库类型
     * @param wrapper 反义处理函数, 比如 mysql: `?`, sqlserver: [?], 或 无反义处理: ?
     */
    public static void setEscape(DbType dbType, String wrapper) {
        DB_ESCAPE.put(dbType, new DbEscape(wrapper));
    }
}