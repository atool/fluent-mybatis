package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.base.crud.IBaseQuery;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.base.model.ISqlOp;
import cn.org.atool.fluent.mybatis.functions.IAggregate;

import static cn.org.atool.fluent.mybatis.segment.model.KeyWordSegment.HAVING;

/**
 * BaseHaving: having设置
 *
 * @author darui.wu
 * @create 2020/6/21 6:22 下午
 */
public abstract class HavingBase<
    H extends HavingBase<H, Q>,
    Q extends IBaseQuery<?, Q>
    >
    extends AggregateSegment<H, Q, HavingOperator<H>> {

    protected final HavingOperator<H> operator = new HavingOperator<>(super.getOrigin());

    protected HavingBase(Q query) {
        super(query);
    }

    protected HavingBase(H selector, IAggregate aggregate) {
        super(selector, aggregate);
    }

    /**
     * 设置having条件
     *
     * @param aggregate having 聚合函数
     * @param op        比较操作
     * @param args      参数列表
     * @return Having设置器
     */
    H aggregate(String aggregate, ISqlOp op, Object... args) {
        this.wrapper.getWrapperData().apply(HAVING, aggregate, op, args);
        return (H) this;
    }

    /**
     * 执行聚合操作
     *
     * @param column    聚合字段
     * @param aggregate 聚合函数
     * @return Having条件判断
     */
    protected HavingOperator<H> apply(FieldMapping column, IAggregate aggregate) {
        return this.operator.aggregate(this.columnWithAlias(column), aggregate);
    }

    /**
     * count(*)
     *
     * @return Having条件判断
     */
    public HavingOperator<H> count() {
        return this.operator.aggregate(null, (c) -> "count(*)");
    }

    /**
     * 执行聚合操作
     *
     * @param aggregate 聚合操作, 比如 sum(column) 或者 select中聚合操作的别名
     * @return Having条件判断
     */
    public HavingOperator<H> apply(String aggregate) {
        if (this.aggregate == null) {
            return this.operator.aggregate(null, (c) -> aggregate);
        } else {
            this.operator.aggregate(this.columnWithAlias(aggregate), this.aggregate);
            return this.operator;
        }
    }

    @Override
    protected HavingOperator<H> apply() {
        this.apply(this.current, this.aggregate);
        return this.operator;
    }
}