package cn.org.atool.fluent.mybatis.segment.where;

import cn.org.atool.fluent.mybatis.base.IQuery;
import cn.org.atool.fluent.mybatis.segment.WhereBase;

import static cn.org.atool.fluent.mybatis.base.model.SqlOp.*;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.toArray;

/**
 * 数字相关的比较
 *
 * @param <WHERE>
 * @param <NQ>
 */
public interface NumericWhere<
    WHERE extends WhereBase<WHERE, ?, NQ>,
    NQ extends IQuery<?, NQ>
    > extends ObjectWhere<WHERE, NQ> {
    /**
     * in (values)
     *
     * @param values 条件值
     * @return 查询器或更新器
     */
    default WHERE in(int[] values) {
        return this.apply(IN, toArray(values));
    }

    /**
     * in (values)
     *
     * @param values 条件值
     * @return 查询器或更新器
     */
    default WHERE in(long[] values) {
        return this.apply(IN, toArray(values));
    }

    /**
     * not in (values)
     *
     * @param values 条件值
     * @return 查询器或更新器
     */
    default WHERE notIn(int[] values) {
        return this.apply(NOT_IN, toArray(values));
    }

    /**
     * not in (values)
     *
     * @param values 条件值
     * @return 查询器或更新器
     */
    default WHERE notIn(long[] values) {
        return this.apply(NOT_IN, toArray(values));
    }

    /**
     * in (values)
     *
     * @param values 条件值
     * @return 查询器或更新器
     */
    default WHERE in_IfNotEmpty(int[] values) {
        return this.in_IfNotEmpty(toArray(values));
    }

    /**
     * in (values)
     *
     * @param values 条件值
     * @return 查询器或更新器
     */
    default WHERE in_IfNotEmpty(long[] values) {
        return this.in_IfNotEmpty(toArray(values));
    }

    /**
     * in (values)
     *
     * @param condition 为真时成立
     * @param values    条件值
     * @return 查询器或更新器
     */
    default WHERE in(boolean condition, int[] values) {
        return this.in(condition, toArray(values));
    }

    /**
     * not in (values)
     *
     * @param values 条件值
     * @return 查询器或更新器
     */
    default WHERE notIn_IfNotEmpty(int[] values) {
        return this.notIn_IfNotEmpty(toArray(values));
    }

    /**
     * not in (values)
     *
     * @param values 条件值
     * @return 查询器或更新器
     */
    default WHERE notIn_IfNotEmpty(long[] values) {
        return this.notIn_IfNotEmpty(toArray(values));
    }

    /**
     * not in (values)
     *
     * @param condition 为真时成立
     * @param values    条件值
     * @return 查询器或更新器
     */
    default WHERE notIn(boolean condition, int[] values) {
        return this.notIn(condition, toArray(values));
    }

    /**
     * not in (values)
     *
     * @param condition 为真时成立
     * @param values    条件值
     * @return 查询器或更新器
     */
    default WHERE notIn(boolean condition, long[] values) {
        return this.notIn(condition, toArray(values));
    }

    /**
     * in (values)
     *
     * @param condition 为真时成立
     * @param values    条件值
     * @return 查询器或更新器
     */
    default WHERE in(boolean condition, long[] values) {
        return this.in(condition, toArray(values));
    }
}
