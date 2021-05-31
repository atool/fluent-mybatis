package cn.org.atool.fluent.mybatis.segment.model;

/**
 * IWrapperData: 提供给xml文件调用的方法
 *
 * @author darui.wu
 */
public interface IWrapperData {
    /**
     * 是否distinct查询
     *
     * @return
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
    String getMergeSql();

    /**
     * 返回where部分sql
     *
     * @return
     */
    String getWhereSql();

    /**
     * 返回 groupBy + having + orderBy + last 组合起来的语句
     *
     * @return
     */
    String getGroupBy();

    /**
     * 返回 groupBy + having + last 组合起来的语句
     *
     * @return
     */
    String getOrderBy();

    /**
     * select ... from table where ...
     * 不包含分页部分
     *
     * @return select ... from table where ...
     */
    String getQuerySql();

    /**
     * 返回last sql部分
     *
     * @return
     */
    String getLastSql();
}