package cn.org.atool.fluent.mybatis.segment.where;

import cn.org.atool.fluent.mybatis.base.IQuery;
import cn.org.atool.fluent.mybatis.segment.WhereBase;
import cn.org.atool.fluent.mybatis.segment.model.SqlLike;

import static cn.org.atool.fluent.mybatis.base.model.SqlOp.*;
import static cn.org.atool.fluent.mybatis.base.model.SqlOp.LIKE;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.isNotBlank;

/**
 * 字符串相关的比较
 *
 * @param <WHERE>
 * @param <NQ>
 */
public interface StringWhere<
    WHERE extends WhereBase<WHERE, ?, NQ>,
    NQ extends IQuery<?, NQ>
    > extends ObjectWhere<WHERE, NQ> {

    /**
     * eq_IfNotBlank
     *
     * @param value 条件值
     * @return self
     */
    default WHERE eq_IfNotBlank(String value) {
        return this.apply(isNotBlank(value), EQ, value);
    }

    /**
     * ne_IfNotBlank
     *
     * @param value 条件值
     * @return self
     */
    default WHERE ne_IfNotBlank(String value) {
        return this.apply(isNotBlank(value), NE, value);
    }

    /**
     * gt_IfNotBlank
     *
     * @param value 条件值
     * @return self
     */
    default WHERE gt_IfNotBlank(String value) {
        return this.apply(isNotBlank(value), GT, value);
    }

    /**
     * ge_IfNotBlank
     *
     * @param value 条件值
     * @return self
     */
    default WHERE ge_IfNotBlank(String value) {
        return this.apply(isNotBlank(value), GE, value);
    }

    /**
     * lt_IfNotBlank
     *
     * @param value 条件值
     * @return self
     */
    default WHERE lt_IfNotBlank(String value) {
        return this.apply(isNotBlank(value), LT, value);
    }

    /**
     * le_IfNotBlank
     *
     * @param value 条件值
     * @return self
     */
    default WHERE le_IfNotBlank(String value) {
        return this.apply(isNotBlank(value), LE, value);
    }

    /**
     * like '%value%'
     *
     * @param value 条件值
     * @return self
     */
    default WHERE like(String value) {
        return this.apply(LIKE, SqlLike.like(value));
    }

    /**
     * like '%value%'
     *
     * @param condition 成立条件
     * @param value     条件值
     * @return self
     */
    default WHERE like(boolean condition, String value) {
        return this.apply(condition, LIKE, SqlLike.like(value));
    }

    /**
     * value不为空时, 执行 like '%value%'
     *
     * @param value 条件值
     * @return self
     */
    default WHERE like_IfNotBlank(String value) {
        return this.apply(isNotBlank(value), LIKE, SqlLike.like(value));
    }

    /**
     * not like '%value%'
     *
     * @param value 条件值
     * @return self
     */
    default WHERE notLike(String value) {
        return this.apply(NOT_LIKE, SqlLike.like(value));
    }

    /**
     * not like '%value%'
     *
     * @param condition 成立条件
     * @param value     条件值
     * @return self
     */
    default WHERE notLike(boolean condition, String value) {
        return this.apply(condition, NOT_LIKE, SqlLike.like(value));
    }

    /**
     * not like '%value%'
     *
     * @param value 条件值
     * @return where
     */
    default WHERE notLike_IfNotBlank(String value) {
        return this.apply(isNotBlank(value), NOT_LIKE, SqlLike.like(value));
    }

    /**
     * like '%value'
     *
     * @param value left like value
     * @return where
     */
    default WHERE likeLeft(String value) {
        return this.apply(LIKE, SqlLike.left(value));
    }

    /**
     * like '%value'
     *
     * @param condition 执行条件
     * @param value     left like value
     * @return where
     */
    default WHERE likeLeft(boolean condition, String value) {
        return this.apply(condition, LIKE, SqlLike.left(value));
    }

    /**
     * like '%value'
     *
     * @param value left like value
     * @return where
     */
    default WHERE likeLeft_IfNotBlank(String value) {
        return this.apply(isNotBlank(value), LIKE, SqlLike.left(value));
    }

    /**
     * like 'value%'
     *
     * @param value right like value
     * @return where
     */
    default WHERE likeRight(String value) {
        return this.apply(LIKE, SqlLike.right(value));
    }

    /**
     * like 'value%'
     *
     * @param condition 执行条件
     * @param value     right like value
     * @return where
     */
    default WHERE likeRight(boolean condition, String value) {
        return this.apply(condition, LIKE, SqlLike.right(value));
    }

    /**
     * like 'value%'
     *
     * @param value right like value
     * @return where
     */
    default WHERE likeRight_IfNotBlank(String value) {
        return this.apply(isNotBlank(value), LIKE, SqlLike.right(value));
    }
}
