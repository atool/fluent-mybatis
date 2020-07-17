package cn.org.atool.fluent.mybatis.segment.model;

import cn.org.atool.fluent.mybatis.base.model.SqlOp;

import java.util.Collection;

import static cn.org.atool.fluent.mybatis.base.model.SqlOp.*;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.toArray;

/**
 * 比较操作
 *
 * @param <R>
 * @author wudarui
 */
public interface IOperator<R> {
    /**
     * 执行比较操作
     *
     * @param op
     * @param args
     * @return
     */
    <O> R apply(SqlOp op, O... args);

    /**
     * 等于 =
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    default R eq(Object value) {
        return this.apply(EQ, value);
    }

    /**
     * 不等于 !=
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    default R ne(Object value) {
        return this.apply(NE, value);
    }

    /**
     * 大于
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    default R gt(Object value) {
        return this.apply(GT, value);
    }

    /**
     * 大于等于
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    default R ge(Object value) {
        return this.apply(GE, value);
    }

    /**
     * 小于
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    default R lt(Object value) {
        return this.apply(LT, value);
    }

    /**
     * 小于等于
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    default R le(Object value) {
        return this.apply(LE, value);
    }

    /**
     * in (values)
     *
     * @param values 条件值
     * @return 查询器或更新器
     */
    default R in(Object[] values) {
        return this.apply(IN, values);
    }

    /**
     * in (values)
     *
     * @param values 条件值
     * @return 查询器或更新器
     */
    default R in(int[] values) {
        return this.apply(IN, toArray(values));
    }

    /**
     * in (values)
     *
     * @param values 条件值
     * @return 查询器或更新器
     */
    default R in(long[] values) {
        return this.apply(IN, toArray(values));
    }

    /**
     * in (values)
     *
     * @param values 条件值
     * @return 查询器或更新器
     */
    default R in(Collection values) {
        return this.apply(IN, values == null ? new Object[0] : values.toArray());
    }

    /**
     * not in (values)
     *
     * @param values 条件值
     * @return 查询器或更新器
     */
    default R notIn(Object[] values) {
        return this.apply(NOT_IN, values);
    }

    /**
     * not in (values)
     *
     * @param values 条件值
     * @return 查询器或更新器
     */
    default R notIn(int[] values) {
        return this.apply(NOT_IN, toArray(values));
    }

    /**
     * not in (values)
     *
     * @param values 条件值
     * @return 查询器或更新器
     */
    default R notIn(long[] values) {
        return this.apply(NOT_IN, toArray(values));
    }

    /**
     * not in (values)
     *
     * @param values 条件值
     * @return 查询器或更新器
     */
    default R notIn(Collection values) {
        return this.apply(NOT_IN, values == null ? new Object[0] : values.toArray());
    }

    /**
     * @param value1 条件值
     * @param value2 条件值
     * @return 查询器或更新器
     */
    default R between(Object value1, Object value2) {
        return this.apply(BETWEEN, value1, value2);
    }

    /**
     * @param value1 条件值
     * @param value2 条件值
     * @return 查询器或更新器
     */
    default R notBetween(Object value1, Object value2) {
        return this.apply(NOT_BETWEEN, value1, value2);
    }
}