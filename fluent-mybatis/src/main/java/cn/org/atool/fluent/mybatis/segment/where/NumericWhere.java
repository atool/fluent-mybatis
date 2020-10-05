package cn.org.atool.fluent.mybatis.segment.where;

import cn.org.atool.fluent.mybatis.base.IQuery;
import cn.org.atool.fluent.mybatis.segment.WhereBase;

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
    WHERE in_IfNotEmpty(int[] values);

    /**
     * in (values)
     *
     * @param values 条件值
     * @return 查询器或更新器
     */
    WHERE in_IfNotEmpty(long[] values);

    /**
     * in (values)
     *
     * @param condition 为真时成立
     * @param values    条件值
     * @return 查询器或更新器
     */
    WHERE in(boolean condition, int[] values);

    /**
     * not in (values)
     *
     * @param values 条件值
     * @return 查询器或更新器
     */
    WHERE notIn_IfNotEmpty(int[] values);

    /**
     * not in (values)
     *
     * @param values 条件值
     * @return 查询器或更新器
     */
    WHERE notIn_IfNotEmpty(long[] values);

    /**
     * not in (values)
     *
     * @param condition 为真时成立
     * @param values    条件值
     * @return 查询器或更新器
     */
    WHERE notIn(boolean condition, int[] values);

    /**
     * not in (values)
     *
     * @param condition 为真时成立
     * @param values    条件值
     * @return 查询器或更新器
     */
    WHERE notIn(boolean condition, long[] values);

    /**
     * in (values)
     *
     * @param condition 为真时成立
     * @param values    条件值
     * @return 查询器或更新器
     */
    WHERE in(boolean condition, long[] values);

    /**
     * @param condition 为真时成立
     * @param value1    条件值
     * @param value2    条件值
     * @return 查询器或更新器
     */
    WHERE between(boolean condition, Object value1, Object value2);

    /**
     * @param condition 为真时成立
     * @param value1    条件值
     * @param value2    条件值
     * @return 查询器或更新器
     */
    WHERE notBetween(boolean condition, Object value1, Object value2);
}
