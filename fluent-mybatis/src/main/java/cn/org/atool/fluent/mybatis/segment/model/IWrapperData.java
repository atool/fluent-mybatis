package cn.org.atool.fluent.mybatis.segment.model;

import static cn.org.atool.fluent.mybatis.If.isBlank;

/**
 * IWrapperData: 提供给xml文件调用的方法
 *
 * @author darui.wu
 */
public interface IWrapperData {
    /**
     * 是否distinct查询
     *
     * @return ignore
     */
    boolean isDistinct();

    /**
     * 查询条件 SQL 片段
     *
     * @return 查询字段列表
     */
    String getSqlSelect();

    /**
     * (update)
     * set
     * column1 = value1,
     * column2 = value2
     *
     * @return 更新语句
     */
    String getUpdateStr();

    /**
     * where + groupBy + having + orderBy + limit + last 语句部分
     *
     * @return where sql
     */
    default String getMergeSql() {
        String sql = mergeSegments().sql();
        return isBlank(sql) ? null : sql.trim();
    }

    /**
     * 返回where部分sql
     *
     * @return ignore
     */
    default String getWhereSql() {
        return this.mergeSegments().whereSql();
    }

    /**
     * 返回 groupBy + having + orderBy + last 组合起来的语句
     *
     * @return ignore
     */
    default String getGroupBy() {
        return this.mergeSegments().groupBy();
    }

    /**
     * 返回 groupBy + having + last 组合起来的语句
     *
     * @return ignore
     */
    default String getOrderBy() {
        return this.mergeSegments().orderBy();
    }

    /**
     * 根据数据库类型返回带分页的语法
     *
     * @return sql with page
     */
    String sqlWithPaged();

    /**
     * select ... from table where ...
     * 不包含分页部分
     *
     * @return select ... from table where ...
     */
    String sqlWithoutPaged();

    /**
     * 返回last sql部分
     *
     * @return ignore
     */
    default String getLastSql() {
        return this.mergeSegments().last();
    }

    /**
     * 附加sql,只允许执行一次
     *
     * @param lastSql 附加sql
     */
    default void last(String lastSql) {
        this.mergeSegments().setLastSql(lastSql);
    }

    MergeSegments mergeSegments();
}