package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.model.ISqlOp;
import cn.org.atool.fluent.mybatis.functions.IAggregate;
import cn.org.atool.fluent.mybatis.segment.fragment.BracketFrag;
import cn.org.atool.fluent.mybatis.segment.fragment.CachedFrag;
import cn.org.atool.fluent.mybatis.segment.fragment.IFragment;
import cn.org.atool.fluent.mybatis.segment.model.IOperator;

import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.assertNotNull;

/**
 * HavingOperator: having聚合操作
 *
 * @param <H> Having操作器
 * @author wudarui
 */
@SuppressWarnings({"rawtypes"})
public class HavingOperator<H extends HavingBase<H, ?>>
    implements IOperator<H> {
    /**
     * 表达式
     */
    private IFragment expression;

    private final H having;

    HavingOperator(H having) {
        this.having = having;
    }

    @Override
    public H apply(ISqlOp op, Object... args) {
        assertNotNull("expression", expression);
        return this.having.aggregate(expression, op, args);
    }

    /**
     * having aggregate(column) op (select ...)
     *
     * @param op    比较符
     * @param query 子查询
     * @return H
     */
    public H apply(ISqlOp op, IQuery query) {
        assertNotNull("query", query);
        query.data().sharedParameter(this.having.data());
        return this.having.apply(expression, op, BracketFrag.set(query));
    }

    /**
     * having aggregate(column) op func(args)
     *
     * @param op   比较符
     * @param func 函数
     * @param args 参数
     * @return H
     */
    public H applyFunc(ISqlOp op, String func, Object... args) {
        assertNotNull("func", func);
        return this.having.apply(expression, op, CachedFrag.set(func), args);
    }

    /**
     * 设置聚合函数表达式
     *
     * @param column    聚合字段
     * @param aggregate 聚合函数
     * @return 操作器
     */
    HavingOperator<H> aggregate(IFragment column, IAggregate aggregate) {
        this.expression = aggregate.aggregate(column);
        return this;
    }
}