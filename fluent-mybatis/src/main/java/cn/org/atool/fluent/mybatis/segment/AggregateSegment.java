package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.base.crud.IBaseQuery;
import cn.org.atool.fluent.mybatis.functions.IAggregate;

import static cn.org.atool.fluent.mybatis.segment.model.Aggregate.*;

/**
 * 聚合函数
 *
 * @author wudarui
 */
@SuppressWarnings({ "unchecked" })
public abstract class AggregateSegment<S extends AggregateSegment<S, Q, R>, Q extends IBaseQuery<?, Q>, R>
        extends BaseSegment<R, Q> {
    /**
     * 当前聚合片段
     */
    public final S and = (S) this;
    /**
     * 聚合对象max,min,sum...等实例的原始实例来源(aggregate=null)
     */
    protected S origin;

    /**
     * 聚合函数
     */
    protected final IAggregate aggregate;

    /**
     * max
     */
    public S max;

    /**
     * min
     */
    public S min;

    /**
     * sum
     */
    public S sum;

    /**
     * avg
     */
    public S avg;

    /**
     * count
     */
    public S count;

    /**
     * group_concat
     */
    public S group_concat;

    /**
     * 构造函数
     *
     * @param query 查询对象
     */
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

    /**
     * 构造函数
     *
     * @param origin    源片段
     * @param aggregate 聚合函数
     */
    protected AggregateSegment(S origin, IAggregate aggregate) {
        super((Q) origin.wrapper);
        this.aggregate = aggregate;
        this.origin = origin;
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
     * @param aggregate 聚合函数接口
     * @return S
     */
    protected abstract S aggregateSegment(IAggregate aggregate);

    /**
     * 获取源片段
     *
     * @return S
     */
    protected S getOrigin() {
        return this.aggregate == null || this.origin == null ? (S) this : this.origin;
    }
}