package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.base.IQuery;

/**
 * ColumnSelector: 字段查询
 *
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
        return this.segment.apply(String.format("SUM(%s)", this.current.column));
    }

    /**
     * 返回参数字段之和
     *
     * @param alias as 别名
     * @return 返回字段选择器
     */
    public S sum(String alias) {
        return this.segment.apply(String.format("SUM(%s) AS %s", this.current.column, alias));
    }

    /**
     * 返回参数字段的数量，不统计为NULL的记录
     *
     * @return 返回字段选择器
     */
    public S count() {
        return this.segment.apply(String.format("COUNT(%s)", this.current.column));
    }

    /**
     * 返回参数字段的数量，不统计为NULL的记录
     *
     * @param alias as 别名
     * @return 返回字段选择器
     */
    public S count(String alias) {
        return this.segment.apply(String.format("COUNT(%s) AS %s", this.current.column, alias));
    }


    /**
     * 返回参数字段的最大值
     *
     * @return 返回字段选择器
     */
    public S max() {
        return this.segment.apply(String.format("MAX(%s)", this.current.column));
    }

    /**
     * 返回参数字段的最大值
     *
     * @param alias as 别名
     * @return 返回字段选择器
     */
    public S max(String alias) {
        return this.segment.apply(String.format("MAX(%s) AS %s", this.current.column, alias));
    }

    /**
     * 返回参数字段的最小值
     *
     * @return 返回字段选择器
     */
    public S min() {
        return this.segment.apply(String.format("MIN(%s)", this.current.column));
    }

    /**
     * 返回参数字段的最小值
     *
     * @param alias as 别名
     * @return 返回字段选择器
     */
    public S min(String alias) {
        return this.segment.apply(String.format("MIN(%s) AS %s", this.current.column, alias));
    }

    /**
     * 返回参数字段的平均值
     *
     * @return 返回字段选择器
     */
    public S avg() {
        return this.segment.apply(String.format("AVG(%s)", this.current.column));
    }

    /**
     * 返回参数字段的平均值
     *
     * @param alias as 别名
     * @return 返回字段选择器
     */
    public S avg(String alias) {
        return this.segment.apply(String.format("AVG(%s) AS %s", this.current.column, alias));
    }

    /**
     * 返回符合条件的参数字段值的连接字符串
     *
     * @return 返回字段选择器
     */
    public S group_concat() {
        return this.segment.apply(String.format("GROUP_CONCAT(%s)", this.current.column));
    }

    /**
     * 返回符合条件的参数字段值的连接字符串
     *
     * @param alias as 别名
     * @return 返回字段选择器
     */
    public S group_concat(String alias) {
        return this.segment.apply(String.format("GROUP_CONCAT(%s) AS %s", this.current.column, alias));
    }
}