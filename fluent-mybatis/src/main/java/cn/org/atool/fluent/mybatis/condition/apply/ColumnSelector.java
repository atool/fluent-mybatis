package cn.org.atool.fluent.mybatis.condition.apply;

import cn.org.atool.fluent.mybatis.condition.base.BaseSelector;

/**
 * ColumnSelector: 字段查询
 *
 * @author darui.wu
 * @create 2020/6/21 3:26 下午
 */
public class ColumnSelector<S extends BaseSelector<S>> {
    private S selector;

    private String column;

    public ColumnSelector(S selector, String column) {
        this.selector = selector;
        this.column = column;
    }

    /**
     * 查询字段
     *
     * @return 返回字段选择器
     */
    public S select() {
        return this.selector.apply(column);
    }

    /**
     * 查询字段
     *
     * @param alias as 别名
     * @return 返回字段选择器
     */
    public S select(String alias) {
        return this.selector.apply(String.format("%s AS %s", column, alias));
    }

    /**
     * 返回参数字段之和
     *
     * @return 返回字段选择器
     */
    public S sum() {
        return this.selector.apply(String.format("SUM(%s)", column));
    }

    /**
     * 返回参数字段之和
     *
     * @param alias as 别名
     * @return 返回字段选择器
     */
    public S sum(String alias) {
        return this.selector.apply(String.format("SUM(%s) AS %s", column, alias));
    }

    /**
     * 返回参数字段的数量，不统计为NULL的记录
     *
     * @return 返回字段选择器
     */
    public S count() {
        return this.selector.apply(String.format("COUNT(%s)", column));
    }

    /**
     * 返回参数字段的数量，不统计为NULL的记录
     *
     * @param alias as 别名
     * @return 返回字段选择器
     */
    public S count(String alias) {
        return this.selector.apply(String.format("COUNT(%s) AS %s", column, alias));
    }


    /**
     * 返回参数字段的最大值
     *
     * @return 返回字段选择器
     */
    public S max() {
        return this.selector.apply(String.format("MAX(%s)", column));
    }

    /**
     * 返回参数字段的最大值
     *
     * @param alias as 别名
     * @return 返回字段选择器
     */
    public S max(String alias) {
        return this.selector.apply(String.format("MAX(%s) AS %s", column, alias));
    }

    /**
     * 返回参数字段的最小值
     *
     * @return 返回字段选择器
     */
    public S min() {
        return this.selector.apply(String.format("MIN(%s)", column));
    }

    /**
     * 返回参数字段的最小值
     *
     * @param alias as 别名
     * @return 返回字段选择器
     */
    public S min(String alias) {
        return this.selector.apply(String.format("MIN(%s) AS %s", column, alias));
    }

    /**
     * 返回参数字段的平均值
     *
     * @return 返回字段选择器
     */
    public S avg() {
        return this.selector.apply(String.format("AVG(%s)", column));
    }

    /**
     * 返回参数字段的平均值
     *
     * @param alias as 别名
     * @return 返回字段选择器
     */
    public S avg(String alias) {
        return this.selector.apply(String.format("AVG(%s) AS %s", column, alias));
    }

    /**
     * 返回符合条件的参数字段值的连接字符串
     *
     * @return 返回字段选择器
     */
    public S group_concat() {
        return this.selector.apply(String.format("GROUP_CONCAT(%s)", column));
    }

    /**
     * 返回符合条件的参数字段值的连接字符串
     *
     * @param alias as 别名
     * @return 返回字段选择器
     */
    public S group_concat(String alias) {
        return this.selector.apply(String.format("GROUP_CONCAT(%s) AS %s", column, alias));
    }
}