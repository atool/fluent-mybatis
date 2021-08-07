package cn.org.atool.fluent.mybatis.metadata;

import cn.org.atool.fluent.mybatis.metadata.feature.DbFeature;
import cn.org.atool.fluent.mybatis.metadata.feature.EscapeExpress;
import cn.org.atool.fluent.mybatis.metadata.feature.PagedFormat;
import lombok.Getter;

import static cn.org.atool.fluent.mybatis.metadata.feature.EscapeExpress.*;
import static cn.org.atool.fluent.mybatis.metadata.feature.PagedFormat.*;

/**
 * DbType 数据库类型
 *
 * @author darui.wu Created by darui.wu on 2020/6/16.
 * @see EscapeExpress
 */
public enum DbType {
    /**
     * MYSQL
     */
    MYSQL("mysql", BACK_ESCAPE, MYSQL_LIMIT, "SELECT LAST_INSERT_ID() AS ID"),
    /**
     * MARIADB
     */
    MARIADB("mariadb", BACK_ESCAPE, MYSQL_LIMIT, "SELECT LAST_INSERT_ID() AS ID"),
    /**
     * H2 '
     */
    H2("h2", BACK_ESCAPE, MYSQL_LIMIT),
    /**
     * SQLITE
     * https://www.sqlite.org/lang_keywords.html
     */
    SQLITE("sqlite", D_QUOTATION_ESCAPE, MYSQL_LIMIT),
    /**
     * ORACLE
     */
    ORACLE("oracle", NONE_ESCAPE, ORACLE_LIMIT, "select SEQ_USER_ID.nextval as id from dual", true),
    /**
     * DB2
     */
    DB2("db2", DB2_LIMIT),
    /**
     * HSQL
     */
    HSQL("hsql", PG_LIMIT),
    /**
     * POSTGRE
     */
    POSTGRE_SQL("postgresql", D_QUOTATION_ESCAPE, PG_LIMIT),
    /**
     * informix
     */
    INFORMIX("informix", INFORMIX_LIMIT),
    /**
     * SQLSERVER2005
     */
    SQL_SERVER2005("sqlserver2005", SQUARE_BRACKETS_ESCAPE, UN_SUPPORT_LIMIT),
    /**
     * SQLSERVER
     */
    SQL_SERVER2012("sqlserver", SQUARE_BRACKETS_ESCAPE, SQLSERVER2012_LIMIT),
    /**
     * 其它数据库1, 按标准语法进行处理
     * 这里定义2个OTHER, 是为了尽可能满足一个应用使用到多数据源类型的场景
     */
    OTHER("other", MYSQL_LIMIT),
    /**
     * 其它数据库2, 按标准语法进行处理
     */
    OTHER_2("other2", MYSQL_LIMIT);

    @Getter
    public final DbFeature feature;

    DbType(String alias, PagedFormat paged) {
        this.feature = new DbFeature(alias, paged);
    }

    DbType(String alias, EscapeExpress escape, PagedFormat paged) {
        this.feature = new DbFeature(alias, escape, paged);
    }

    DbType(String alias, EscapeExpress escape, PagedFormat paged, String seq) {
        this.feature = new DbFeature(alias, escape, paged, seq);
    }

    DbType(String alias, EscapeExpress escape, PagedFormat paged, String seq, boolean before) {
        this.feature = new DbFeature(alias, escape, paged, seq).setBefore(before);
    }

    /**
     * 数据库字段用反义符包起来
     *
     * @param column 数据库字段
     * @return 反义符包裹后的数据库字段
     */
    public String wrap(String column) {
        return this.feature.getEscape().wrap(column);
    }

    /**
     * 去掉转义符
     *
     * @param column 可能带转义符的字段名称
     * @return 去掉转义符后的名称
     */
    public String unwrap(String column) {
        return this.feature.getEscape().unwrap(column);
    }

    /**
     * 根据数据库类型组装分页语句
     *
     * @param query          未分页的查询语句
     * @param pagedOffset    分页偏移开始变量
     * @param pagedSize      分页大小变量
     * @param pagedEndOffset 分页偏移结束变量
     * @return 分页查询语句
     */
    public String paged(String query, String pagedOffset, String pagedSize, String pagedEndOffset) {
        return this.feature.getPaged().build(query, pagedOffset, pagedSize, pagedEndOffset);
    }

    /**
     * 设置数据库字段的反义处理
     *
     * @param expression 反义处理函数, 比如 mysql: `?`, sqlserver: [?], 或 无反义处理: ?
     */
    public void setEscapeExpress(String expression) {
        this.feature.setEscape(new EscapeExpress(expression));
    }

    /**
     * 设置数据库分页处理语法规则
     *
     * @param pagedFormat {@link PagedFormat}
     */
    public void setPagedFormat(String pagedFormat) {
        this.feature.setPaged(new PagedFormat(pagedFormat));
    }
}