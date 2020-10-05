package cn.org.atool.fluent.mybatis.segment.where;

import cn.org.atool.fluent.mybatis.base.IQuery;
import cn.org.atool.fluent.mybatis.segment.WhereBase;

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
    WHERE eq_IfNotBlank(String value);

    /**
     * ne_IfNotBlank
     *
     * @param value 条件值
     * @return self
     */
    WHERE ne_IfNotBlank(String value);

    /**
     * gt_IfNotBlank
     *
     * @param value 条件值
     * @return self
     */
    WHERE gt_IfNotBlank(String value);

    /**
     * ge_IfNotBlank
     *
     * @param value 条件值
     * @return self
     */
    WHERE ge_IfNotBlank(String value);

    /**
     * lt_IfNotBlank
     *
     * @param value 条件值
     * @return self
     */
    WHERE lt_IfNotBlank(String value);

    /**
     * le_IfNotBlank
     *
     * @param value 条件值
     * @return self
     */
    WHERE le_IfNotBlank(String value);

    /**
     * like '%value%'
     *
     * @param value 条件值
     * @return self
     */
    WHERE like(String value);

    /**
     * like '%value%'
     *
     * @param condition 成立条件
     * @param value     条件值
     * @return self
     */
    WHERE like(boolean condition, String value);

    /**
     * value不为空时, 执行 like '%value%'
     *
     * @param value 条件值
     * @return self
     */
    WHERE like_IfNotBlank(String value);

    /**
     * not like '%value%'
     *
     * @param value 条件值
     * @return self
     */
    WHERE notLike(String value);

    /**
     * not like '%value%'
     *
     * @param condition 成立条件
     * @param value     条件值
     * @return self
     */
    WHERE notLike(boolean condition, String value);

    /**
     * not like '%value%'
     *
     * @param value 条件值
     * @return where
     */
    WHERE notLike_IfNotBlank(String value);

    /**
     * like '%value'
     *
     * @param value left like value
     * @return where
     */
    WHERE likeLeft(String value);

    /**
     * like '%value'
     *
     * @param condition 执行条件
     * @param value     left like value
     * @return where
     */
    WHERE likeLeft(boolean condition, String value);

    /**
     * like '%value'
     *
     * @param value left like value
     * @return where
     */
    WHERE likeLeft_IfNotBlank(String value);

    /**
     * like 'value%'
     *
     * @param value right like value
     * @return where
     */
    WHERE likeRight(String value);

    /**
     * like 'value%'
     *
     * @param condition 执行条件
     * @param value     right like value
     * @return where
     */
    WHERE likeRight(boolean condition, String value);

    /**
     * like 'value%'
     *
     * @param value right like value
     * @return where
     */
    WHERE likeRight_IfNotBlank(String value);
}
