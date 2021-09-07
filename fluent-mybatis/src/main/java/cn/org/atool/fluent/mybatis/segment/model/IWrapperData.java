package cn.org.atool.fluent.mybatis.segment.model;

import cn.org.atool.fluent.mybatis.metadata.DbType;

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
    String getMergeSql();

    /**
     * 返回where部分sql
     *
     * @return ignore
     */
    String getWhereSql();

    /**
     * 返回 groupBy + having + orderBy + last 组合起来的语句
     *
     * @return ignore
     */
    String getGroupBy();

    /**
     * 返回 groupBy + having + last 组合起来的语句
     *
     * @return ignore
     */
    String getOrderBy();

    /**
     * 根据数据库类型返回带分页的语法
     *
     * @param dbType DbType
     * @return sql with page
     */
    String sqlWithPaged(DbType dbType, String allColumn);

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
    String getLastSql();
}