package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.base.IQuery;
import cn.org.atool.fluent.mybatis.functions.IAggregate;

import static cn.org.atool.fluent.mybatis.segment.model.Aggregate.*;

/**
 * HavingBy 设置
 *
 * @param <H>
 * @param <Q>
 * @author darui.wu
 * @create 2020/6/21 6:18 下午
 */
public class HavingApply<
    H extends HavingBase<H, Q>,
    Q extends IQuery<?, Q>
    > extends BaseApply<H, Q> {

    public HavingApply(H having) {
        super(having);
    }

    /**
     * 进行聚合操作
     *
     * @param aggregate 具体聚合操作
     * @return 返回比较操作
     */
    public HavingOperator<H> apply(IAggregate aggregate) {
        return this.segment.apply(current.column, aggregate);
    }

    /**
     * 字段之和 符合 分组条件
     *
     * @return 返回比较操作
     */
    public HavingOperator<H> sum() {
        return this.apply(SUM);
    }

    /**
     * 非空字段总数 符合 分组条件
     *
     * @return 返回比较操作
     */
    public HavingOperator<H> count() {
        return this.apply(COUNT);
    }

    /**
     * 字段最大值 符合 分组条件
     *
     * @return 返回比较操作
     */
    public HavingOperator<H> max() {
        return this.apply(MAX);
    }

    /**
     * 字段最小值 符合 分组条件
     *
     * @return 返回比较操作
     */
    public HavingOperator<H> min() {
        return this.apply(MIN);
    }

    /**
     * 字段平均值 符合 分组条件
     *
     * @return 返回字段选择器
     */
    public HavingOperator<H> avg() {
        return this.apply(AVG);
    }
}