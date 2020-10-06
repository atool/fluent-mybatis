package cn.org.atool.fluent.mybatis.segment.where;

import cn.org.atool.fluent.mybatis.base.IQuery;
import cn.org.atool.fluent.mybatis.segment.WhereBase;

import java.util.Collection;
import java.util.function.Function;

import static cn.org.atool.fluent.mybatis.base.model.SqlOp.*;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.isNotEmpty;

public interface ObjectWhere<
    WHERE extends WhereBase<WHERE, ?, NQ>,
    NQ extends IQuery<?, NQ>
    > extends BaseWhere<WHERE, NQ> {

    //gt

    /**
     * 大于
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    default WHERE gt(Object value) {
        return this.apply(GT, value);
    }

    /**
     * 大于
     *
     * @param condition 为真时成立
     * @param value     条件值
     * @return 查询器或更新器
     */
    default WHERE gt(boolean condition, Object value) {
        return this.apply(condition, GT, value);
    }

    /**
     * 大于
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    default WHERE gt_IfNotNull(Object value) {
        return this.apply(value != null, GT, value);
    }

    /**
     * 大于等于
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    default WHERE ge(Object value) {
        return this.apply(GE, value);
    }

    /**
     * 大于等于
     *
     * @param condition 为真时成立
     * @param value     条件值
     * @return 查询器或更新器
     */
    default WHERE ge(boolean condition, Object value) {
        return this.apply(condition, GE, value);
    }

    /**
     * 大于等于
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    default WHERE ge_IfNotNull(Object value) {
        return this.apply(value != null, GE, value);
    }

    /**
     * 小于
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    default WHERE lt(Object value) {
        return this.apply(LT, value);
    }

    /**
     * 小于
     *
     * @param condition 为真时成立
     * @param value     条件值
     * @return 查询器或更新器
     */
    default WHERE lt(boolean condition, Object value) {
        return this.apply(condition, LT, value);
    }

    /**
     * 小于
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    default WHERE lt_IfNotNull(Object value) {
        return this.apply(value != null, LT, value);
    }

    /**
     * 小于等于
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    default WHERE le(Object value) {
        return this.apply(LE, value);
    }

    /**
     * 小于等于
     *
     * @param condition 为真时成立
     * @param value     条件值
     * @return 查询器或更新器
     */
    default WHERE le(boolean condition, Object value) {
        return this.apply(condition, LE, value);
    }

    /**
     * 小于等于
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    default WHERE le_IfNotNull(Object value) {
        return this.apply(value != null, LE, value);
    }

    /**
     * in (values)
     *
     * @param values 条件值
     * @return 查询器或更新器
     */
    default WHERE in(Object[] values) {
        return this.apply(IN, values);
    }

    /**
     * in (values)
     *
     * @param values 条件值
     * @return 查询器或更新器
     */
    default WHERE in(Collection values) {
        return this.apply(IN, values == null ? new Object[0] : values.toArray());
    }

    /**
     * in (values)
     *
     * @param condition 为真时成立
     * @param values    条件值
     * @return 查询器或更新器
     */
    WHERE in(boolean condition, Object[] values);

    /**
     * in (values)
     *
     * @param condition 为真时成立
     * @param values    条件值
     * @return 查询器或更新器
     */
    WHERE in(boolean condition, Collection values);

    /**
     * in (values)
     *
     * @param values 条件值
     * @return 查询器或更新器
     */
    default WHERE in_IfNotEmpty(Object[] values) {
        return this.in(isNotEmpty(values), values);
    }

    /**
     * in (values)
     *
     * @param values 条件值
     * @return 查询器或更新器
     */
    default WHERE in_IfNotEmpty(Collection values) {
        return this.in(isNotEmpty(values), values);
    }

    /**
     * where column IN (select ... )
     *
     * @param select 子查询语句
     * @param args   子查询语句参数，对应select语句里面的 "?" 占位符
     * @return 查询器或更新器
     */
    <O> WHERE in(String select, O... args);

    /**
     * in (select ... )
     *
     * @param query 嵌套查询
     * @return 查询器或更新器
     */
    WHERE in(Function<NQ, NQ> query);

    /**
     * in (select ... )
     *
     * @param klass 嵌套查询类
     * @param query 嵌套查询
     * @param <NQ>  嵌套查询类
     * @return 查询器或更新器
     */
    <NQ extends IQuery> WHERE in(Class<NQ> klass, Function<NQ, NQ> query);

    /**
     * not in (values)
     *
     * @param values 条件值
     * @return 查询器或更新器
     */
    default WHERE notIn(Object[] values) {
        return this.apply(NOT_IN, values);
    }


    /**
     * not in (values)
     *
     * @param values 条件值
     * @return 查询器或更新器
     */
    default WHERE notIn(Collection values) {
        return this.apply(NOT_IN, values == null ? new Object[0] : values.toArray());
    }

    /**
     * not in (values)
     *
     * @param condition 为真时成立
     * @param values    条件值
     * @return 查询器或更新器
     */
    WHERE notIn(boolean condition, Object[] values);

    /**
     * not in (values)
     *
     * @param condition 为真时成立
     * @param values    条件值
     * @return 查询器或更新器
     */
    WHERE notIn(boolean condition, Collection values);

    /**
     * not in (values)
     *
     * @param values 条件值
     * @return 查询器或更新器
     */
    default WHERE notIn_IfNotEmpty(Object[] values) {
        return this.notIn(isNotEmpty(values), values);
    }

    /**
     * not in (values)
     *
     * @param values 条件值
     * @return 查询器或更新器
     */
    default WHERE notIn_IfNotEmpty(Collection values) {
        return this.notIn(isNotEmpty(values), values);
    }

    /**
     * not in (select ... )
     *
     * @param query 嵌套查询
     * @return 查询器或更新器
     */
    WHERE notIn(Function<NQ, NQ> query);

    /**
     * not in (select ... )
     *
     * @param queryClass 嵌套查询类
     * @param query      嵌套查询
     * @param <NQ>       嵌套查询类
     * @return 查询器或更新器
     */
    <NQ extends IQuery<?, NQ>> WHERE notIn(Class<NQ> queryClass, Function<NQ, NQ> query);

    /**
     * @param value1 条件值
     * @param value2 条件值
     * @return 查询器或更新器
     */
    default WHERE between(Object value1, Object value2) {
        return this.apply(BETWEEN, value1, value2);
    }

    /**
     * @param value1 条件值
     * @param value2 条件值
     * @return 查询器或更新器
     */
    default WHERE notBetween(Object value1, Object value2) {
        return this.apply(NOT_BETWEEN, value1, value2);
    }

    /**
     * @param condition 为真时成立
     * @param value1    条件值
     * @param value2    条件值
     * @return 查询器或更新器
     */
    default WHERE between(boolean condition, Object value1, Object value2) {
        return this.apply(condition, BETWEEN, value1, value2);
    }

    /**
     * @param condition 为真时成立
     * @param value1    条件值
     * @param value2    条件值
     * @return 查询器或更新器
     */
    default WHERE notBetween(boolean condition, Object value1, Object value2) {
        return this.apply(condition, NOT_BETWEEN, value1, value2);
    }
}
