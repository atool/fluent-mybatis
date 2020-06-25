package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.base.model.SqlOp;
import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;
import cn.org.atool.fluent.mybatis.segment.model.IOperator;
import cn.org.atool.fluent.mybatis.segment.model.SqlLike;
import cn.org.atool.fluent.mybatis.base.IQuery;
import cn.org.atool.fluent.mybatis.utility.NestedQueryFactory;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Stream;

import static cn.org.atool.fluent.mybatis.base.model.SqlOp.*;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.*;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * AndObject
 *
 * @param <WHERE> 条件设置器
 * @param <NQ>    对应的嵌套查询器
 * @author darui.wu
 */
public class WhereApply<
    WHERE extends WhereBase<WHERE, ?, NQ>,
    NQ extends IQuery<?, NQ>
    > extends BaseApply<WHERE, NQ> implements IOperator<WHERE> {

    public WhereApply(WHERE where) {
        super(where);
    }

    @Override
    public WHERE apply(SqlOp op, Object... args) {
        if (op.getArgSize() > 0) {
            assertNotEmpty(current.name, args);
            if (args.length != op.getArgSize()) {
                throw new FluentMybatisException(op.getArgSize() + " parameters are required, but " + args.length + " is passed in");
            }
            Stream.of(args).forEach(arg -> assertNotNull(current.name, arg));
        }
        return this.segment.apply(current.column, op, args);
    }

    private WHERE apply(boolean condition, SqlOp op, Object... args) {
        return condition ? this.apply(op, args) : segment;
    }

    /**
     * is null
     *
     * @return 查询器或更新器
     */
    public WHERE isNull() {
        return this.apply(IS_NULL);
    }

    /**
     * is null
     *
     * @param condition 条件为真时成立
     * @return 查询器或更新器
     */
    public WHERE isNull(boolean condition) {
        return this.apply(condition, IS_NULL);
    }

    /**
     * not null
     *
     * @return 查询器或更新器
     */
    public WHERE isNotNull() {
        return this.apply(IS_NOT_NULL);
    }

    /**
     * not null
     *
     * @param condition 条件为真时成立
     * @return 查询器或更新器
     */
    public WHERE isNotNull(boolean condition) {
        return this.apply(condition, IS_NOT_NULL);
    }

    // eq

    /**
     * 等于 =
     *
     * @param condition 条件为真时成立
     * @param value     条件值
     * @return 查询器或更新器
     */
    public WHERE eq(boolean condition, Object value) {
        return this.apply(condition, EQ, value);
    }

    /**
     * 等于 =, 值不为空时成立
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    public WHERE eq_IfNotNull(Object value) {
        return this.apply(value != null, EQ, value);
    }

    // ne

    /**
     * 不等于 !=
     *
     * @param condition 为真时成立
     * @param value     条件值
     * @return 查询器或更新器
     */
    public WHERE ne(boolean condition, Object value) {
        return this.apply(condition, NE, value);
    }


    /**
     * 不等于 !=
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    public WHERE ne_IfNotNull(Object value) {
        return this.apply(value != null, NE, value);
    }


    //gt

    /**
     * 大于
     *
     * @param condition 为真时成立
     * @param value     条件值
     * @return 查询器或更新器
     */
    public WHERE gt(boolean condition, Object value) {
        return this.apply(condition, GT, value);
    }

    /**
     * 大于
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    public WHERE gt_IfNotNull(Object value) {
        return this.apply(value != null, GT, value);
    }

    /**
     * 大于等于
     *
     * @param condition 为真时成立
     * @param value     条件值
     * @return 查询器或更新器
     */
    public WHERE ge(boolean condition, Object value) {
        return this.apply(condition, GE, value);
    }

    /**
     * 大于等于
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    public WHERE ge_IfNotNull(Object value) {
        return this.apply(value != null, GE, value);
    }

    /**
     * 小于
     *
     * @param condition 为真时成立
     * @param value     条件值
     * @return 查询器或更新器
     */
    public WHERE lt(boolean condition, Object value) {
        return this.apply(condition, LT, value);
    }

    /**
     * 小于
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    public WHERE lt_IfNotNull(Object value) {
        return this.apply(value != null, LT, value);
    }

    /**
     * 小于等于
     *
     * @param condition 为真时成立
     * @param value     条件值
     * @return 查询器或更新器
     */
    public WHERE le(boolean condition, Object value) {
        return this.apply(condition, LE, value);
    }

    /**
     * 小于等于
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    public WHERE le_IfNotNull(Object value) {
        return this.apply(value != null, LE, value);
    }

    /**
     * @param condition 为真时成立
     * @param values    条件值
     * @return 查询器或更新器
     */
    public WHERE in(boolean condition, Collection<Object> values) {
        if (condition) {
            return this.apply(IN, values.toArray());
        } else {
            return this.segment;
        }
    }

    /**
     * @param values 条件值
     * @return 查询器或更新器
     */
    public WHERE in_IfNotEmpty(Collection<Object> values) {
        return this.in(values != null && !values.isEmpty(), values);
    }

    /**
     * in (values)
     *
     * @param condition 为真时成立
     * @param values    条件值
     * @return 查询器或更新器
     */
    public WHERE in(boolean condition, Object[] values) {
        return this.apply(condition, IN, values);
    }

    /**
     * where column IN (select ... )
     *
     * @param select 子查询语句
     * @param values 子查询语句参数，对应select语句里面的 "?" 占位符
     * @return 查询器或更新器
     */
    public WHERE inSql(String select, Object... values) {
        return this.segment.apply(current.column, select, IN, values);
    }

    /**
     * in (select ... )
     *
     * @param query 嵌套查询
     * @return 查询器或更新器
     */
    public WHERE in(Function<NQ, NQ> query) {
        return (WHERE) this.in(this.segment.queryClass(), query);
    }

    /**
     * in (select ... )
     *
     * @param klass 嵌套查询类
     * @param query 嵌套查询
     * @param <NQ>  嵌套查询类
     * @return 查询器或更新器
     */
    public <NQ extends IQuery> WHERE in(Class<NQ> klass, Function<NQ, NQ> query) {
        NQ nested = NestedQueryFactory.nested(klass, this.segment.getParameters());
        query.apply(nested);
        return this.segment.apply(current.column, nested.getWrapperData().getQuerySql(), IN);
    }

    /**
     * in (values)
     *
     * @param values 条件值
     * @return 查询器或更新器
     */
    public WHERE in_IfNotEmpty(Object[] values) {
        return this.in(values != null && values.length > 0, values);
    }

    /**
     * not in (values)
     *
     * @param condition 为真时成立
     * @param values    条件值
     * @return 查询器或更新器
     */
    public WHERE notIn(boolean condition, Collection<Object> values) {
        return condition ? this.notIn(values) : segment;
    }

    /**
     * not in (values)
     *
     * @param values 条件值
     * @return 查询器或更新器
     */
    public WHERE notIn_IfNotEmpty(Collection<Object> values) {
        return this.notIn(values != null && !values.isEmpty(), values);
    }

    /**
     * not in (values)
     *
     * @param condition 为真时成立
     * @param values    条件值
     * @return 查询器或更新器
     */
    public WHERE notIn(boolean condition, Object[] values) {
        return this.apply(condition, NOT_IN, values);
    }

    /**
     * not in (values)
     *
     * @param values 条件值
     * @return 查询器或更新器
     */
    public WHERE notIn_IfNotEmpty(Object[] values) {
        return this.notIn(values != null && values.length > 0, values);
    }

    /**
     * not in (select ... )
     *
     * @param query 嵌套查询
     * @return 查询器或更新器
     */
    public WHERE notIn(Function<NQ, NQ> query) {
        return (WHERE) this.notIn(this.segment.queryClass(), query);
    }

    /**
     * not in (select ... )
     *
     * @param queryClass 嵌套查询类
     * @param query      嵌套查询
     * @param <NQ>       嵌套查询类
     * @return 查询器或更新器
     */
    public <NQ extends IQuery<?, NQ>> WHERE notIn(Class<NQ> queryClass, Function<NQ, NQ> query) {
        NQ nested = NestedQueryFactory.nested(queryClass, this.segment.getParameters());
        query.apply(nested);
        return this.segment.apply(current.column, nested.getWrapperData().getQuerySql(), NOT_IN);
    }

    /**
     * @param condition 为真时成立
     * @param value1    条件值
     * @param value2    条件值
     * @return 查询器或更新器
     */
    public WHERE between(boolean condition, Object value1, Object value2) {
        return this.apply(condition, BETWEEN, value1, value2);
    }

    /**
     * @param condition 为真时成立
     * @param value1    条件值
     * @param value2    条件值
     * @return 查询器或更新器
     */
    public WHERE notBetween(boolean condition, Object value1, Object value2) {
        return this.apply(condition, NOT_BETWEEN, value1, value2);
    }

    /**
     * ==================================================
     *                   以下针对字符串操作
     * ==================================================
     */

    /**
     * eq_IfNotBlank
     *
     * @param value 条件值
     * @return self
     */
    public WHERE eq_IfNotBlank(String value) {
        return this.apply(isNotBlank(value), EQ, value);
    }

    /**
     * ne_IfNotBlank
     *
     * @param value 条件值
     * @return self
     */
    public WHERE ne_IfNotBlank(String value) {
        return this.apply(isNotBlank(value), NE, value);
    }

    /**
     * gt_IfNotBlank
     *
     * @param value 条件值
     * @return self
     */
    public WHERE gt_IfNotBlank(String value) {
        return this.apply(isNotBlank(value), GT, value);
    }

    /**
     * ge_IfNotBlank
     *
     * @param value 条件值
     * @return self
     */
    public WHERE ge_IfNotBlank(String value) {
        return this.apply(isNotBlank(value), GE, value);
    }

    /**
     * lt_IfNotBlank
     *
     * @param value 条件值
     * @return self
     */
    public WHERE lt_IfNotBlank(String value) {
        return this.apply(isNotBlank(value), LT, value);
    }

    /**
     * le_IfNotBlank
     *
     * @param value 条件值
     * @return self
     */
    public WHERE le_IfNotBlank(String value) {
        return this.apply(isNotBlank(value), LE, value);
    }

    /**
     * like '%value%'
     *
     * @param value 条件值
     * @return self
     */
    public WHERE like(String value) {
        return this.apply(LIKE, SqlLike.like(value));
    }

    /**
     * like '%value%'
     *
     * @param condition 成立条件
     * @param value     条件值
     * @return self
     */
    public WHERE like(boolean condition, String value) {
        return this.apply(condition, LIKE, SqlLike.like(value));
    }

    /**
     * value不为空时, 执行 like '%value%'
     *
     * @param value 条件值
     * @return self
     */
    public WHERE like_IfNotBlank(String value) {
        return this.apply(isNotBlank(value), LIKE, SqlLike.like(value));
    }

    /**
     * not like '%value%'
     *
     * @param value 条件值
     * @return self
     */
    public WHERE notLike(String value) {
        return this.apply(NOT_LIKE, SqlLike.like(value));
    }

    /**
     * not like '%value%'
     *
     * @param condition 成立条件
     * @param value     条件值
     * @return self
     */
    public WHERE notLike(boolean condition, String value) {
        return this.apply(condition, NOT_LIKE, SqlLike.like(value));
    }

    /**
     * not like '%value%'
     *
     * @param value 条件值
     * @return where
     */
    public WHERE notLike_IfNotBlank(String value) {
        return this.apply(isNotBlank(value), NOT_LIKE, SqlLike.like(value));
    }

    /**
     * like '%value'
     *
     * @param value left like value
     * @return where
     */
    public WHERE likeLeft(String value) {
        return this.apply(LIKE, SqlLike.left(value));
    }

    /**
     * like '%value'
     *
     * @param condition 执行条件
     * @param value     left like value
     * @return where
     */
    public WHERE likeLeft(boolean condition, String value) {
        return this.apply(condition, LIKE, SqlLike.left(value));
    }

    /**
     * like '%value'
     *
     * @param value left like value
     * @return where
     */
    public WHERE likeLeft_IfNotBlank(String value) {
        return this.apply(isNotBlank(value), LIKE, SqlLike.left(value));
    }

    /**
     * like 'value%'
     *
     * @param value right like value
     * @return where
     */
    public WHERE likeRight(String value) {
        return this.apply(LIKE, SqlLike.right(value));
    }

    /**
     * like 'value%'
     *
     * @param condition 执行条件
     * @param value     right like value
     * @return where
     */
    public WHERE likeRight(boolean condition, String value) {
        return this.apply(condition, LIKE, SqlLike.right(value));
    }

    /**
     * like 'value%'
     *
     * @param value right like value
     * @return where
     */
    public WHERE likeRight_IfNotBlank(String value) {
        return this.apply(isNotBlank(value), LIKE, SqlLike.right(value));
    }

    /**
     * set 自定义
     * ！！！慎用！！！！
     * 有sql注入风险
     *
     * @param sql 自定义操作
     * @return 查询器或更新器
     */
    public WHERE apply(String sql) {
        return this.segment.apply(current.column, sql, RETAIN);
    }
}