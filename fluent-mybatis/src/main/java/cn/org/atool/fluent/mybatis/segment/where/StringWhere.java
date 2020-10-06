package cn.org.atool.fluent.mybatis.segment.where;

import cn.org.atool.fluent.mybatis.base.IQuery;
import cn.org.atool.fluent.mybatis.segment.WhereBase;
import cn.org.atool.fluent.mybatis.segment.model.SqlLike;

import java.util.function.Predicate;

import static cn.org.atool.fluent.mybatis.base.model.SqlOp.LIKE;
import static cn.org.atool.fluent.mybatis.base.model.SqlOp.NOT_LIKE;

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
     * @param value 条件值
     * @param when  成立条件
     * @return self
     */
    default WHERE like(String value, Predicate<String> when) {
        return this.apply(when.test(value), LIKE, SqlLike.like(value));
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
     * @param value 条件值
     * @param when  成立条件
     * @return self
     */
    default WHERE notLike(String value, Predicate<String> when) {
        return this.apply(when.test(value), NOT_LIKE, SqlLike.like(value));
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
     * @param value left like value
     * @param when  执行条件
     * @return where
     */
    default WHERE likeLeft(String value, Predicate<String> when) {
        return this.apply(when.test(value), LIKE, SqlLike.left(value));
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
     * @param value right like value
     * @param when  执行条件
     * @return where
     */
    default WHERE likeRight(String value, Predicate<String> when) {
        return this.apply(when.test(value), LIKE, SqlLike.right(value));
    }
}
