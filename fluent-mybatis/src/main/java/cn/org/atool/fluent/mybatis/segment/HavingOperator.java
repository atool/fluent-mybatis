package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.base.model.SqlOp;
import cn.org.atool.fluent.mybatis.functions.IAggregate;
import cn.org.atool.fluent.mybatis.segment.model.IOperator;

import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.assertNotBlank;

/**
 * HavingOperator: having聚合操作
 *
 * @param <H> Having操作器
 * @author wudarui
 */
public class HavingOperator<H extends HavingBase<H, ?>>
    implements IOperator<H> {
    /**
     * 表达式
     */
    private String expression;

    private final H having;

    HavingOperator(H having) {
        this.having = having;
    }

    @Override
    public H apply(SqlOp op, Object... args) {
        assertNotBlank("expression", expression);
        return this.having.aggregate(expression, op, args);
    }

    /**
     * 设置聚合函数表达式
     *
     * @param column    聚合字段
     * @param aggregate 聚合函数
     * @return 操作器
     */
    HavingOperator<H> aggregate(String column, IAggregate aggregate) {
        this.expression = aggregate.aggregate(column);
        return this;
    }
}
