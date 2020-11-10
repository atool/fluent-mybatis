package cn.org.atool.fluent.mybatis.segment.where;

import cn.org.atool.fluent.mybatis.functions.IoFunction;
import cn.org.atool.fluent.mybatis.base.IQuery;
import cn.org.atool.fluent.mybatis.segment.WhereBase;

import java.util.Collection;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static cn.org.atool.fluent.mybatis.base.model.SqlOp.*;

public interface ObjectWhere<
    WHERE extends WhereBase<WHERE, ?, NQ>,
    NQ extends IQuery<?, NQ>
    > extends BaseWhere<WHERE, NQ> {

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
     * @param value 条件值
     * @param when  为真时成立
     * @return 查询器或更新器
     */
    default <T> WHERE gt(T value, Predicate<T> when) {
        return this.apply(when.test(value), GT, value);
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
     * @param value 条件值
     * @param when  为真时成立
     * @return 查询器或更新器
     */
    default <T> WHERE ge(T value, Predicate<T> when) {
        return this.apply(when.test(value), GE, value);
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
     * @param value 条件值
     * @param when  为真时成立
     * @return 查询器或更新器
     */
    default <T> WHERE lt(T value, Predicate<T> when) {
        return this.apply(when.test(value), LT, value);
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
     * @param value 条件值
     * @param when  为真时成立
     * @return 查询器或更新器
     */
    default <T> WHERE le(T value, Predicate<T> when) {
        return this.apply(when.test(value), LE, value);
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
     * @param when   为真时成立
     * @return 查询器或更新器
     */
    default <T> WHERE in(T[] values, Predicate<T[]> when) {
        return this.apply(when.test(values), IN, values);
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
     * @param values 条件值
     * @param when   为真时成立
     * @return 查询器或更新器
     */
    default WHERE in(Collection values, Predicate<Collection> when) {
        return this.apply(when.test(values), IN, values == null ? new Object[0] : values.toArray());
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
     * where column IN (select ... )
     *
     * @param condition true时条件成立
     * @param select    子查询语句
     * @param args      子查询语句参数，对应select语句里面的 "?" 占位符
     * @param <O>
     * @return 查询器或更新器
     */
    <O> WHERE in(boolean condition, String select, O... args);

    /**
     * in (select ... )
     *
     * @param query 嵌套查询
     * @return 查询器或更新器
     */
    WHERE in(IoFunction<NQ> query);

    /**
     * in (select ... )
     *
     * @param condition true时条件成立
     * @param query     嵌套查询
     * @return 查询器或更新器
     */
    WHERE in(boolean condition, IoFunction<NQ> query);

    /**
     * in (select ... )
     *
     * @param klass 嵌套查询类
     * @param query 嵌套查询
     * @param <NQ>  嵌套查询类
     * @return 查询器或更新器
     */
    <NQ extends IQuery> WHERE in(Class<NQ> klass, IoFunction<NQ> query);

    /**
     * in (select ... )
     *
     * @param condition true时条件成立
     * @param klass     嵌套查询类
     * @param query     嵌套查询
     * @param <NQ>      嵌套查询类
     * @return 查询器或更新器
     */
    <NQ extends IQuery> WHERE in(boolean condition, Class<NQ> klass, IoFunction<NQ> query);

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
     * @param when   为真时成立
     * @return 查询器或更新器
     */
    default <T> WHERE notIn(T[] values, Predicate<T[]> when) {
        return this.apply(when.test(values), NOT_IN, values);
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
     * @param values 条件值
     * @param when   为真时成立
     * @return 查询器或更新器
     */
    default WHERE notIn(Collection values, Predicate<Collection> when) {
        return this.apply(when.test(values), NOT_IN, values == null ? new Object[0] : values.toArray());
    }

    /**
     * not in (select ... )
     *
     * @param query 嵌套查询
     * @return 查询器或更新器
     */
    WHERE notIn(IoFunction<NQ> query);

    /**
     * not in (select ... )
     *
     * @param condition true时条件成立
     * @param query     嵌套查询
     * @return 查询器或更新器
     */
    WHERE notIn(boolean condition, IoFunction<NQ> query);

    /**
     * not in (select ... )
     *
     * @param queryClass 嵌套查询类
     * @param query      嵌套查询
     * @param <NQ>       嵌套查询类
     * @return 查询器或更新器
     */
    <NQ extends IQuery<?, NQ>> WHERE notIn(Class<NQ> queryClass, IoFunction<NQ> query);

    /**
     * not in (select ... )
     *
     * @param condition  true时条件成立
     * @param queryClass 嵌套查询类
     * @param query      嵌套查询
     * @param <NQ>       嵌套查询类
     * @return 查询器或更新器
     */
    <NQ extends IQuery<?, NQ>> WHERE notIn(boolean condition, Class<NQ> queryClass, IoFunction<NQ> query);

    /**
     * @param value1 条件值
     * @param value2 条件值
     * @return 查询器或更新器
     */
    default <T> WHERE between(T value1, T value2) {
        return this.apply(BETWEEN, value1, value2);
    }

    /**
     * @param value1 条件值
     * @param value2 条件值
     * @param when   为真时成立
     * @return 查询器或更新器
     */
    default <T> WHERE between(T value1, T value2, BiPredicate<T, T> when) {
        return this.apply(when.test(value1, value2), BETWEEN, value1, value2);
    }

    /**
     * @param value1 条件值
     * @param value2 条件值
     * @return 查询器或更新器
     */
    default <T> WHERE notBetween(T value1, T value2) {
        return this.apply(NOT_BETWEEN, value1, value2);
    }

    /**
     * @param value1 条件值
     * @param value2 条件值
     * @param when   为真时成立
     * @return 查询器或更新器
     */
    default <T> WHERE notBetween(T value1, T value2, BiPredicate<T, T> when) {
        return this.apply(when.test(value1, value2), NOT_BETWEEN, value1, value2);
    }
}
