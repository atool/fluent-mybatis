package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.base.model.FieldMeta;
import cn.org.atool.fluent.mybatis.base.model.SqlOp;
import cn.org.atool.fluent.mybatis.base.IQuery;
import cn.org.atool.fluent.mybatis.functions.IAggregate;

import static cn.org.atool.fluent.mybatis.segment.model.KeyWordSegment.HAVING;
import static cn.org.atool.fluent.mybatis.segment.model.StrConstant.EMPTY;

/**
 * BaseHaving: having设置
 *
 * @author darui.wu
 * @create 2020/6/21 6:22 下午
 */
public abstract class HavingBase<
    H extends HavingBase<H, Q>,
    Q extends IQuery<?, Q>
    >
    extends BaseSegment<HavingApply<H, Q>, Q> {

    private final HavingOperator<H> operator = new HavingOperator<>((H) this);

    private final HavingApply<H, Q> apply = new HavingApply<>((H) this);

    protected HavingBase(Q query) {
        super(query);
    }

    /**
     * 设置having条件
     *
     * @param aggregate having 聚合函数
     * @param op        比较操作
     * @param args      参数列表
     * @return Having设置器
     */
    H aggregate(String aggregate, SqlOp op, Object... args) {
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
    HavingOperator<H> apply(String column, IAggregate aggregate) {
        return this.operator.aggregate(column, aggregate);
    }

    /**
     * 执行聚合操作
     *
     * @param aggregate 聚合操作, 比如 sum(column)
     * @return Having条件判断
     */
    public HavingOperator<H> apply(String aggregate) {
        return this.operator.aggregate(null, (c) -> aggregate);
    }

    @Override
    public HavingApply<H, Q> set(FieldMeta field) {
        return this.apply.setCurrentField(field);
    }
}