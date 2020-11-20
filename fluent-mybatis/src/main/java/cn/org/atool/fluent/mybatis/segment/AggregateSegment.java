package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.base.crud.IBaseQuery;
import cn.org.atool.fluent.mybatis.functions.IAggregate;

import static cn.org.atool.fluent.mybatis.segment.model.Aggregate.*;

public abstract class AggregateSegment<
    S extends AggregateSegment<S, Q, R>,
    Q extends IBaseQuery<?, Q>,
    R
    >
    extends BaseSegment<R, Q> {
    public final S and = (S) this;

    protected final IAggregate aggregate;

    public S max;

    public S min;

    public S sum;

    public S avg;

    public S count;

    public S group_concat;

    protected AggregateSegment(Q query) {
        super(query);
        this.aggregate = null;
        this.max = this.aggregateSegment(MAX);
        this.min = this.aggregateSegment(MIN);
        this.sum = this.aggregateSegment(SUM);
        this.avg = this.aggregateSegment(AVG);
        this.count = this.aggregateSegment(COUNT);
        this.group_concat = this.aggregateSegment(GROUP_CONCAT);
        this.init(max)
            .init(min)
            .init(sum)
            .init(avg)
            .init(count)
            .init(group_concat);
    }

    protected AggregateSegment(S segment, IAggregate aggregate) {
        super((Q) segment.wrapper);
        this.aggregate = aggregate;
    }

    S init(S selector) {
        selector.max = this.max;
        selector.min = this.min;
        selector.sum = this.sum;
        selector.avg = this.avg;
        selector.count = this.count;
        selector.group_concat = this.group_concat;
        return (S) this;
    }

    /**
     * 构造聚合选择器
     *
     * @param aggregate
     * @return
     */
    protected abstract S aggregateSegment(IAggregate aggregate);
}