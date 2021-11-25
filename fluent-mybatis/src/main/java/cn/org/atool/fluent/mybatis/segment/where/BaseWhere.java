package cn.org.atool.fluent.mybatis.segment.where;

import cn.org.atool.fluent.mybatis.base.crud.IBaseQuery;
import cn.org.atool.fluent.mybatis.base.model.ISqlOp;
import cn.org.atool.fluent.mybatis.ifs.Ifs;
import cn.org.atool.fluent.mybatis.segment.WhereBase;

import java.util.Collection;
import java.util.function.Predicate;

import static cn.org.atool.fluent.mybatis.base.model.SqlOp.*;

/**
 * 基础比较: apply, is null, not null, eq, ne
 *
 * @param <WHERE>
 * @param <NQ>
 */
@SuppressWarnings({"unchecked", "unused"})
public interface BaseWhere<
    WHERE extends WhereBase<WHERE, ?, NQ>,
    NQ extends IBaseQuery<?, NQ>
    > {

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
        return this.apply(args -> condition, IS_NULL);
    }


    /**
     * not null
     *
     * @return 查询器或更新器
     */
    default WHERE notNull() {
        return this.apply(NOT_NULL);
    }

    /**
     * not null
     *
     * @param condition 条件为真时成立
     * @return 查询器或更新器
     */
    default WHERE notNull(boolean condition) {
        return this.apply(args -> condition, NOT_NULL);
    }

    /**
     * 等于 =
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    default <T> WHERE eq(T value) {
        return this.apply(EQ, value);
    }

    /**
     * 按Ifs条件设置where值
     *
     * @param ifs 条件
     * @param <T> 类型
     * @return ignore
     */
    default <T> WHERE eq(Ifs<T> ifs) {
        return this.apply(EQ, ifs);
    }

    /**
     * 等于 =, 值不为空时成立
     *
     * @param value 条件值
     * @param when  条件为真时成立
     * @return 查询器或更新器
     */
    default <T> WHERE eq(T value, Predicate<T> when) {
        return this.apply(args -> when.test(value), EQ, value);
    }

    /**
     * 不等于 !=
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    default <T> WHERE ne(T value) {
        return this.apply(NE, value);
    }

    /**
     * 不等于 !=
     *
     * @param value 条件值
     * @param when  为真时成立
     * @return 查询器或更新器
     */
    default <T> WHERE ne(T value, Predicate<T> when) {
        return this.apply(args -> when.test(value), NE, value);
    }

    /**
     * 按Ifs条件设置where值
     *
     * @param ifs 条件
     * @param <T> 类型
     * @return ignore
     */
    default <T> WHERE ne(Ifs<T> ifs) {
        return this.apply(NE, ifs);
    }

    /**
     * in (values)
     *
     * @param values 条件值
     * @return 查询器或更新器
     */
    WHERE in(Collection values);

    <T> WHERE apply(ISqlOp op, T... args);

    /**
     * 多条件操作
     *
     * @param op  操作符
     * @param ifs 条件
     * @param <T> 类型
     * @return ignore
     */
    <T> WHERE apply(ISqlOp op, Ifs<T> ifs);

    /**
     * {@link #apply(Predicate, ISqlOp, Object[])}
     */
    default <O> WHERE apply(boolean condition, ISqlOp op, O... args) {
        return this.apply(a -> condition, op, args);
    }

    <T> WHERE apply(Predicate<Object[]> predicate, ISqlOp op, T... args);

    /**
     * where 自定义条件(包括操作符在内）
     * 比如 where.age().apply("=34").end()
     *
     * @param opArgs 自定义比较语句
     * @return 查询器或更新器
     */
    WHERE apply(String opArgs);

    /**
     * 自定义 函数或表达式
     *
     * @param op         比较符号
     * @param expression 函数或表达式
     * @param args       函数或表达式的参数
     * @return ignore
     */
    WHERE applyFunc(ISqlOp op, String expression, Object... args);

    /**
     * use {@link #applyFunc(Predicate, ISqlOp, String, Object...)}
     */
    @Deprecated
    default WHERE applyFunc(boolean condition, ISqlOp op, String expression, Object... args) {
        return this.applyFunc(a -> condition, op, expression, args);
    }

    /**
     * 自定义 函数或表达式
     *
     * @param predicate  true时成立
     * @param op         比较符号
     * @param expression 函数或表达式
     * @param args       函数或表达式的参数
     * @return ignore
     */
    WHERE applyFunc(Predicate<Object[]> predicate, ISqlOp op, String expression, Object... args);
}