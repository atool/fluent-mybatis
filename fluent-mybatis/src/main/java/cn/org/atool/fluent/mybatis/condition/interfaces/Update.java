package cn.org.atool.fluent.mybatis.condition.interfaces;

import java.io.Serializable;

/**
 * @author darui.wu
 */
public interface Update<Children, R> extends Serializable {

    /**
     * 设置更新
     *
     * @param column
     * @param val
     * @return ignore
     */
    default Children set(R column, Object val) {
        return set(true, column, val);
    }

    /**
     * 设置 更新 SQL 的 SET 片段
     *
     * @param condition 是否加入 set
     * @param column    字段
     * @param val       值
     * @return children
     */
    Children set(boolean condition, R column, Object val);

    /**
     * 设置更新（自定义SQL）
     *
     * @param sql
     * @return
     */
    default Children setSql(String sql) {
        return setSql(true, sql);
    }

    /**
     * 设置 更新 SQL 的 SET 片段
     *
     * @param condition
     * @param sql       set sql
     * @return children
     */
    Children setSql(boolean condition, String sql);

    /**
     * 获取 更新 SQL 的 SET 片段
     *
     * @return ignore
     */
    String getSqlSet();
}