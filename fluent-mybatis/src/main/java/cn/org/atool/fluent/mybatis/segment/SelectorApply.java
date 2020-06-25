package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.base.IQuery;

import static cn.org.atool.fluent.mybatis.segment.model.Aggregate.*;

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
     * 返回参数字段之和
     *
     * @return 返回字段选择器
     */
    public S sum() {
        return this.sum(null);
    }

    /**
     * 返回参数字段之和
     *
     * @param alias as 别名
     * @return 返回字段选择器
     */
    public S sum(String alias) {
        return this.segment.apply(SUM.aggregate(this.current.column, alias));
    }

    /**
     * 返回参数字段的数量，不统计为NULL的记录
     *
     * @return 返回字段选择器
     */
    public S count() {
        return this.count(null);
    }

    /**
     * 返回参数字段的数量，不统计为NULL的记录
     *
     * @param alias as 别名
     * @return 返回字段选择器
     */
    public S count(String alias) {
        return this.segment.apply(COUNT.aggregate(this.current.column, alias));
    }


    /**
     * 返回参数字段的最大值
     *
     * @return 返回字段选择器
     */
    public S max() {
        return this.max(null);
    }

    /**
     * 返回参数字段的最大值
     *
     * @param alias as 别名
     * @return 返回字段选择器
     */
    public S max(String alias) {
        return this.segment.apply(MAX.aggregate(this.current.column, alias));
    }

    /**
     * 返回参数字段的最小值
     *
     * @return 返回字段选择器
     */
    public S min() {
        return this.min(null);
    }

    /**
     * 返回参数字段的最小值
     *
     * @param alias as 别名
     * @return 返回字段选择器
     */
    public S min(String alias) {
        return this.segment.apply(MIN.aggregate(this.current.column, alias));
    }

    /**
     * 返回参数字段的平均值
     *
     * @return 返回字段选择器
     */
    public S avg() {
        return this.avg(null);
    }

    /**
     * 返回参数字段的平均值
     *
     * @param alias as 别名
     * @return 返回字段选择器
     */
    public S avg(String alias) {
        return this.segment.apply(AVG.aggregate(this.current.column, alias));
    }

    /**
     * 返回符合条件的参数字段值的连接字符串
     *
     * @return 返回字段选择器
     */
    public S group_concat() {
        return this.group_concat(null);
    }

    /**
     * 返回符合条件的参数字段值的连接字符串
     *
     * @param alias as 别名
     * @return 返回字段选择器
     */
    public S group_concat(String alias) {
        return this.segment.apply(GROUP_CONCAT.aggregate(this.current.column, alias));
    }
}