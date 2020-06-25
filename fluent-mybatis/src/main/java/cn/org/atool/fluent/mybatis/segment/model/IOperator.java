package cn.org.atool.fluent.mybatis.segment.model;

import cn.org.atool.fluent.mybatis.base.model.SqlOp;

import java.util.Collection;

import static cn.org.atool.fluent.mybatis.base.model.SqlOp.*;
import static cn.org.atool.fluent.mybatis.base.model.SqlOp.NOT_BETWEEN;

/**
 * 比较操作
 *
 * @param <R> 
 * @author wudarui
 */
public interface IOperator<R> {
    R apply(SqlOp op, Object... args);
    // eq

    /**
     * 等于 =
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    default R eq(Object value) {
        return this.apply(EQ, value);
    }

    // ne

    /**
     * 不等于 !=
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    default R ne(Object value) {
        return this.apply(NE, value);
    }


    //gt

    /**
     * 大于
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    default R gt(Object value) {
        return this.apply(GT, value);
    }

    //ge

    /**
     * 大于等于
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    default R ge(Object value) {
        return this.apply(GE, value);
    }


    //lt

    /**
     * 小于
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    default R lt(Object value) {
        return this.apply(LT, value);
    }

    //le

    /**
     * 小于等于
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    default R le(Object value) {
        return this.apply(LE, value);
    }


    //in

    /**
     * @param values 条件值
     * @return 查询器或更新器
     */
    default R in(Collection<Object> values) {
        return this.apply(IN, values.toArray());
    }


    /**
     * in (values)
     *
     * @param values 条件值
     * @return 查询器或更新器
     */
    default R in(Object... values) {
        return this.apply(IN, values);
    }

    //not in

    /**
     * not in (values)
     *
     * @param values 条件值
     * @return 查询器或更新器
     */
    default R notIn(Collection<Object> values) {
        return this.apply(NOT_IN, values.toArray());
    }

    //between

    /**
     * @param value1 条件值
     * @param value2 条件值
     * @return 查询器或更新器
     */
    default R between(Object value1, Object value2) {
        return this.apply(BETWEEN, value1, value2);
    }


    //not between

    /**
     * @param value1 条件值
     * @param value2 条件值
     * @return 查询器或更新器
     */
    default R notBetween(Object value1, Object value2) {
        return this.apply(NOT_BETWEEN, value1, value2);
    }
}
