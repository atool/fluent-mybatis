package cn.org.atool.fluent.mybatis.segment.where;

import cn.org.atool.fluent.mybatis.base.IQuery;
import cn.org.atool.fluent.mybatis.base.model.SqlOp;
import cn.org.atool.fluent.mybatis.segment.WhereBase;
import cn.org.atool.fluent.mybatis.segment.model.IOperator;

import java.util.Collection;
import java.util.function.Function;

public interface ObjectWhere<
    WHERE extends WhereBase<WHERE, ?, NQ>,
    NQ extends IQuery<?, NQ>
    > extends IOperator<WHERE> {

    <O> WHERE apply(SqlOp op, O... args);

    <O> WHERE apply(boolean condition, SqlOp op, O... args);

    /**
     * is null
     *
     * @return 查询器或更新器
     */
    WHERE isNull();

    /**
     * is null
     *
     * @param condition 条件为真时成立
     * @return 查询器或更新器
     */
    WHERE isNull(boolean condition);

    /**
     * not null
     *
     * @return 查询器或更新器
     */
    WHERE isNotNull();

    /**
     * not null
     *
     * @param condition 条件为真时成立
     * @return 查询器或更新器
     */
    WHERE isNotNull(boolean condition);

    // eq

    /**
     * 等于 =
     *
     * @param condition 条件为真时成立
     * @param value     条件值
     * @return 查询器或更新器
     */
    WHERE eq(boolean condition, Object value);

    /**
     * 等于 =, 值不为空时成立
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    WHERE eq_IfNotNull(Object value);

    // ne

    /**
     * 不等于 !=
     *
     * @param condition 为真时成立
     * @param value     条件值
     * @return 查询器或更新器
     */
    WHERE ne(boolean condition, Object value);

    /**
     * 不等于 !=
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    WHERE ne_IfNotNull(Object value);


    //gt

    /**
     * 大于
     *
     * @param condition 为真时成立
     * @param value     条件值
     * @return 查询器或更新器
     */
    WHERE gt(boolean condition, Object value);

    /**
     * 大于
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    WHERE gt_IfNotNull(Object value);

    /**
     * 大于等于
     *
     * @param condition 为真时成立
     * @param value     条件值
     * @return 查询器或更新器
     */
    WHERE ge(boolean condition, Object value);

    /**
     * 大于等于
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    WHERE ge_IfNotNull(Object value);

    /**
     * 小于
     *
     * @param condition 为真时成立
     * @param value     条件值
     * @return 查询器或更新器
     */
    WHERE lt(boolean condition, Object value);

    /**
     * 小于
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    WHERE lt_IfNotNull(Object value);

    /**
     * 小于等于
     *
     * @param condition 为真时成立
     * @param value     条件值
     * @return 查询器或更新器
     */
    WHERE le(boolean condition, Object value);

    /**
     * 小于等于
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    WHERE le_IfNotNull(Object value);

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
    WHERE in_IfNotEmpty(Object[] values);

    /**
     * in (values)
     *
     * @param values 条件值
     * @return 查询器或更新器
     */
    WHERE in_IfNotEmpty(Collection values);

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
    WHERE notIn_IfNotEmpty(Object[] values);

    /**
     * not in (values)
     *
     * @param values 条件值
     * @return 查询器或更新器
     */
    WHERE notIn_IfNotEmpty(Collection values);

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
