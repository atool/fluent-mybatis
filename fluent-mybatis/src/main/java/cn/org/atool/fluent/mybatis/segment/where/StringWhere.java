package cn.org.atool.fluent.mybatis.segment.where;

import cn.org.atool.fluent.mybatis.base.crud.IBaseQuery;
import cn.org.atool.fluent.mybatis.ifs.Ifs;
import cn.org.atool.fluent.mybatis.mapper.SqlLike;
import cn.org.atool.fluent.mybatis.segment.WhereBase;

import java.util.function.Predicate;

import static cn.org.atool.fluent.mybatis.base.model.SqlOp.LIKE;
import static cn.org.atool.fluent.mybatis.base.model.SqlOp.NOT_LIKE;

/**
 * 字符串相关的比较
 *
 * @param <WHERE>
 * @param <NQ>
 */
@SuppressWarnings({"unused"})
public interface StringWhere<
    WHERE extends WhereBase<WHERE, ?, NQ>,
    NQ extends IBaseQuery<?, NQ>
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
    WHERE like(final String value, final Predicate<String> when);

    /**
     * 按Ifs条件设置where值
     *
     * @param ifs if conditions
     * @param <T> type
     * @return WHERE
     */
    default <T> WHERE like(Ifs<String> ifs) {
        return this.apply(LIKE, ifs);
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
    WHERE notLike(String value, Predicate<String> when);

    /**
     * 按Ifs条件设置where值
     *
     * @param ifs if conditions
     * @param <T> type
     * @return WHERE
     */
    default <T> WHERE notLike(Ifs<String> ifs) {
        return this.apply(NOT_LIKE, ifs);
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
    WHERE likeLeft(String value, Predicate<String> when);

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
    WHERE likeRight(String value, Predicate<String> when);
}