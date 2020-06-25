package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.base.IQuery;
import cn.org.atool.fluent.mybatis.functions.IAggregate;

import static cn.org.atool.fluent.mybatis.segment.model.Aggregate.*;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.isNotEmpty;

/**
 * ColumnSelector: 字段查询
 *
 * @param <S> 字段选择器
 * @param <Q> 查询器
 * @author darui.wu
 * @create 2020/6/21 3:26 下午
 */
public class SelectorApply<
    S extends SelectorBase<S, Q>,
    Q extends IQuery<?, Q>
    > extends BaseApply<S, Q> {

    public SelectorApply(S selector) {
        super(selector);
    }

    /**
     * 查询字段
     *
     * @return 返回字段选择器
     */
    public S get() {
        return this.segment.apply(this.current.column);
    }

    /**
     * 查询字段
     *
     * @param alias as 别名
     * @return 返回字段选择器
     */
    public S alias(String alias) {
        return this.segment.apply(String.format("%s AS %s", this.current.column, alias));
    }

    /**
     * 执行聚合函数
     *
     * @param aggregate 聚合函数
     * @return 返回字段选择器
     */
    public S apply(IAggregate aggregate) {
        return this.apply(aggregate, null);
    }

    /**
     * 执行聚合函数
     *
     * @param aggregate 聚合函数
     * @param alias     as别名
     * @return 返回字段选择器
     */
    public S apply(IAggregate aggregate, String alias) {
        String expression = aggregate.aggregate(this.current.column);
        if (isNotEmpty(alias)) {
            expression = expression + " AS " + alias;
        }
        return this.segment.apply(expression);
    }

    /**
     * 返回参数字段之和
     *
     * @return 返回字段选择器
     */
    public S sum() {
        return this.apply(SUM);
    }

    /**
     * 返回参数字段之和
     *
     * @param alias as 别名
     * @return 返回字段选择器
     */
    public S sum(String alias) {
        return this.apply(SUM, alias);
    }

    /**
     * 返回参数字段的数量，不统计为NULL的记录
     *
     * @return 返回字段选择器
     */
    public S count() {
        return this.apply(COUNT);
    }

    /**
     * 返回参数字段的数量，不统计为NULL的记录
     *
     * @param alias as 别名
     * @return 返回字段选择器
     */
    public S count(String alias) {
        return this.apply(COUNT, alias);
    }

    /**
     * 返回参数字段的最大值
     *
     * @return 返回字段选择器
     */
    public S max() {
        return this.apply(MAX);
    }

    /**
     * 返回参数字段的最大值
     *
     * @param alias as 别名
     * @return 返回字段选择器
     */
    public S max(String alias) {
        return this.apply(MAX, alias);
    }

    /**
     * 返回参数字段的最小值
     *
     * @return 返回字段选择器
     */
    public S min() {
        return this.apply(MIN);
    }

    /**
     * 返回参数字段的最小值
     *
     * @param alias as 别名
     * @return 返回字段选择器
     */
    public S min(String alias) {
        return this.apply(MIN, alias);
    }

    /**
     * 返回参数字段的平均值
     *
     * @return 返回字段选择器
     */
    public S avg() {
        return this.apply(AVG);
    }

    /**
     * 返回参数字段的平均值
     *
     * @param alias as 别名
     * @return 返回字段选择器
     */
    public S avg(String alias) {
        return this.apply(AVG, alias);
    }

    /**
     * 返回符合条件的参数字段值的连接字符串
     *
     * @return 返回字段选择器
     */
    public S group_concat() {
        return this.apply(GROUP_CONCAT);
    }

    /**
     * 返回符合条件的参数字段值的连接字符串
     *
     * @param alias as 别名
     * @return 返回字段选择器
     */
    public S group_concat(String alias) {
        return this.apply(GROUP_CONCAT, alias);
    }
}