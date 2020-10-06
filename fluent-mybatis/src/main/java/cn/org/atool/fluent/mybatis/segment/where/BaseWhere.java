package cn.org.atool.fluent.mybatis.segment.where;

import cn.org.atool.fluent.mybatis.base.IQuery;
import cn.org.atool.fluent.mybatis.base.model.SqlOp;
import cn.org.atool.fluent.mybatis.segment.WhereBase;

import static cn.org.atool.fluent.mybatis.base.model.SqlOp.*;

/**
 * 基础比较: apply, is null, not null, eq, ne
 *
 * @param <WHERE>
 * @param <NQ>
 */
public interface BaseWhere<
    WHERE extends WhereBase<WHERE, ?, NQ>,
    NQ extends IQuery<?, NQ>
    > {

    <O> WHERE apply(SqlOp op, O... args);

    <O> WHERE apply(boolean condition, SqlOp op, O... args);

    /**
     * is null
     *
     * @return 查询器或更新器
     */
    default WHERE isNull() {
        return this.apply(IS_NULL);
    }

    /**
     * is null
     *
     * @param condition 条件为真时成立
     * @return 查询器或更新器
     */
    default WHERE isNull(boolean condition) {
        return this.apply(condition, IS_NULL);
    }


    /**
     * not null
     *
     * @return 查询器或更新器
     */
    default WHERE isNotNull() {
        return this.apply(IS_NOT_NULL);
    }

    /**
     * not null
     *
     * @param condition 条件为真时成立
     * @return 查询器或更新器
     */
    default WHERE isNotNull(boolean condition) {
        return this.apply(condition, IS_NOT_NULL);
    }


    // eq

    /**
     * 等于 =
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    default WHERE eq(Object value) {
        return this.apply(EQ, value);
    }


    /**
     * 等于 =
     *
     * @param condition 条件为真时成立
     * @param value     条件值
     * @return 查询器或更新器
     */
    default WHERE eq(boolean condition, Object value) {
        return this.apply(condition, EQ, value);
    }

    /**
     * 等于 =, 值不为空时成立
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    default WHERE eq_IfNotNull(Object value) {
        return this.apply(value != null, EQ, value);
    }

    // ne

    /**
     * 不等于 !=
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    default WHERE ne(Object value) {
        return this.apply(NE, value);
    }

    /**
     * 不等于 !=
     *
     * @param condition 为真时成立
     * @param value     条件值
     * @return 查询器或更新器
     */
    default WHERE ne(boolean condition, Object value) {
        return this.apply(condition, NE, value);
    }

    /**
     * 不等于 !=
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    default WHERE ne_IfNotNull(Object value) {
        return this.apply(value != null, NE, value);
    }

    /**
     * where 自定义条件(包括操作符在内）
     * 比如 where.age().apply("=34").end()
     * <p>
     * ！！！慎用！！！！
     * 有sql注入风险
     *
     * @param opArgs 自定义比较语句
     * @return 查询器或更新器
     */
    WHERE apply(String opArgs);
}
