package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.base.model.SqlOp;
import cn.org.atool.fluent.mybatis.base.IQuery;

import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.*;

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

    private HavingOperator operator = new HavingOperator();

    public class HavingOperator implements IOperator<H> {
        /**
         * 表达式
         */
        private String expression;

        @Override
        public H apply(SqlOp op, Object... args) {
            assertNotBlank("expression", expression);
            return segment.apply(expression, op, args);
        }

        /**
         * 设置聚合函数表达式
         *
         * @param aggregate 聚合函数
         * @return 操作器
         */
        HavingOperator expression(IAggregate aggregate) {
            this.expression = aggregate.expression(current.column);
            return this;
        }
    }

    public HavingApply(H having) {
        super(having);
    }

    /**
     * 字段之和 符合 分组条件
     *
     * @return 返回比较操作
     */
    public HavingOperator sum() {
        return this.operator.expression(IAggregate.SUM);
    }

    /**
     * 非空字段总数 符合 分组条件
     *
     * @return 返回比较操作
     */
    public HavingOperator count() {
        return this.operator.expression(IAggregate.COUNT);
    }

    /**
     * 字段最大值 符合 分组条件
     *
     * @return 返回比较操作
     */
    public HavingOperator max() {
        return this.operator.expression(IAggregate.MAX);
    }

    /**
     * 字段最小值 符合 分组条件
     *
     * @return 返回比较操作
     */
    public HavingOperator min() {
        return this.operator.expression(IAggregate.MIN);
    }

    /**
     * 字段平均值 符合 分组条件
     *
     * @return 返回字段选择器
     */
    public HavingOperator avg() {
        return this.operator.expression(IAggregate.AVG);
    }
}