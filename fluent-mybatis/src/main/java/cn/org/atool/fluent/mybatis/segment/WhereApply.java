package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.segment.model.SqlLike;
import cn.org.atool.fluent.mybatis.interfaces.IQuery;
import cn.org.atool.fluent.mybatis.utility.NestedQueryFactory;

import java.util.Collection;
import java.util.function.Function;

import static cn.org.atool.fluent.mybatis.segment.model.SqlOp.*;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.*;

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
    > extends BaseApply<WHERE, NQ> {

    public WhereApply(WHERE where) {
        super(where);
    }

    /**
     * is null
     *
     * @return 查询器或更新器
     */
    public WHERE isNull() {
        return this.segment.apply(current.column, IS_NULL);
    }

    /**
     * is null
     *
     * @param condition 条件为真时成立
     * @return 查询器或更新器
     */
    public WHERE isNull(boolean condition) {
        return condition ? this.isNull() : segment;
    }

    /**
     * not null
     *
     * @return 查询器或更新器
     */
    public WHERE isNotNull() {
        return this.segment.apply(current.column, IS_NOT_NULL);
    }

    /**
     * not null
     *
     * @param condition 条件为真时成立
     * @return 查询器或更新器
     */
    public WHERE isNotNull(boolean condition) {
        return condition ? this.isNotNull() : segment;
    }

    // eq

    /**
     * 等于 =
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    public WHERE eq(Object value) {
        assertNotNull(current.name, value);
        return this.segment.apply(current.column, EQ, value);
    }

    /**
     * 等于 =
     *
     * @param condition 条件为真时成立
     * @param value     条件值
     * @return 查询器或更新器
     */
    public WHERE eq(boolean condition, Object value) {
        return condition ? this.eq(value) : segment;
    }


    /**
     * 等于 =, 值不为空时成立
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    public WHERE eq_IfNotNull(Object value) {
        return this.eq(value != null, value);
    }

    // ne

    /**
     * 不等于 !=
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    public WHERE ne(Object value) {
        assertNotNull(current.name, value);
        return this.segment.apply(current.column, NE, value);
    }

    /**
     * 不等于 !=
     *
     * @param condition 为真时成立
     * @param value     条件值
     * @return 查询器或更新器
     */
    public WHERE ne(boolean condition, Object value) {
        return condition ? this.ne(value) : segment;
    }


    /**
     * 不等于 !=
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    public WHERE ne_IfNotNull(Object value) {
        return this.ne(value != null, value);
    }


    //gt

    /**
     * 大于
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    public WHERE gt(Object value) {
        assertNotNull(current.name, value);
        return this.segment.apply(current.column, GT, value);
    }

    /**
     * 大于
     *
     * @param condition 为真时成立
     * @param value     条件值
     * @return 查询器或更新器
     */
    public WHERE gt(boolean condition, Object value) {
        return condition ? this.gt(value) : segment;
    }

    /**
     * 大于
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    public WHERE gt_IfNotNull(Object value) {
        return this.gt(value != null, value);
    }


    //ge

    /**
     * 大于等于
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    public WHERE ge(Object value) {
        assertNotNull(current.name, value);
        return this.segment.apply(current.column, GE, value);
    }

    /**
     * 大于等于
     *
     * @param condition 为真时成立
     * @param value     条件值
     * @return 查询器或更新器
     */
    public WHERE ge(boolean condition, Object value) {
        return condition ? this.ge(value) : segment;
    }


    /**
     * 大于等于
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    public WHERE ge_IfNotNull(Object value) {
        return this.ge(value != null, value);
    }


    //lt

    /**
     * 小于
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    public WHERE lt(Object value) {
        assertNotNull(current.name, value);
        return this.segment.apply(current.column, LT, value);
    }

    /**
     * 小于
     *
     * @param condition 为真时成立
     * @param value     条件值
     * @return 查询器或更新器
     */
    public WHERE lt(boolean condition, Object value) {
        return condition ? this.lt(value) : segment;
    }


    /**
     * 小于
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    public WHERE lt_IfNotNull(Object value) {
        return this.lt(value != null, value);
    }


    //le

    /**
     * 小于等于
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    public WHERE le(Object value) {
        assertNotNull(current.name, value);
        return this.segment.apply(current.column, LE, value);
    }

    /**
     * 小于等于
     *
     * @param condition 为真时成立
     * @param value     条件值
     * @return 查询器或更新器
     */
    public WHERE le(boolean condition, Object value) {
        return condition ? this.le(value) : segment;
    }


    /**
     * 小于等于
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    public WHERE le_IfNotNull(Object value) {
        return this.le(value != null, value);
    }


    //in

    /**
     * @param values 条件值
     * @return 查询器或更新器
     */
    public WHERE in(Collection<Object> values) {
        assertNotEmpty(current.name, values);
        return this.segment.apply(current.column, IN, values.toArray());
    }

    /**
     * @param condition 为真时成立
     * @param values    条件值
     * @return 查询器或更新器
     */
    public WHERE in(boolean condition, Collection<Object> values) {
        return condition ? this.in(values) : segment;
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
     * @param values 条件值
     * @return 查询器或更新器
     */
    public WHERE in(Object[] values) {
        assertNotEmpty(current.name, values);
        return this.segment.apply(current.column, IN, values);
    }

    /**
     * in (values)
     *
     * @param condition 为真时成立
     * @param values    条件值
     * @return 查询器或更新器
     */
    public WHERE in(boolean condition, Object[] values) {
        return condition ?
            this.in(values) : segment;
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

    //not in

    /**
     * not in (values)
     *
     * @param values 条件值
     * @return 查询器或更新器
     */
    public WHERE notIn(Collection<Object> values) {
        assertNotEmpty(current.name, values);
        return this.segment.apply(current.column, NOT_IN, values.toArray());
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
     * @param values 条件值
     * @return 查询器或更新器
     */
    public WHERE notIn(Object[] values) {
        assertNotEmpty(current.name, values);
        return this.segment.apply(current.column, NOT_IN, values);
    }

    /**
     * not in (values)
     *
     * @param condition 为真时成立
     * @param values    条件值
     * @return 查询器或更新器
     */
    public WHERE notIn(boolean condition, Object[] values) {
        return condition ? this.notIn(values) : segment;
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
    //between

    /**
     * @param value1 条件值
     * @param value2 条件值
     * @return 查询器或更新器
     */
    public WHERE between(Object value1, Object value2) {
        assertNotNull(current.name, value1, value2);
        return this.segment.apply(current.column, BETWEEN, value1, value2);
    }

    /**
     * @param condition 为真时成立
     * @param value1    条件值
     * @param value2    条件值
     * @return 查询器或更新器
     */
    public WHERE between(boolean condition, Object value1, Object value2) {
        return condition ? this.between(value1, value2) : segment;
    }

    //not between

    /**
     * @param value1 条件值
     * @param value2 条件值
     * @return 查询器或更新器
     */
    public WHERE notBetween(Object value1, Object value2) {
        assertNotNull(current.name, value1, value2);
        return this.segment.apply(current.column, NOT_BETWEEN, value1, value2);
    }

    /**
     * @param condition 为真时成立
     * @param value1    条件值
     * @param value2    条件值
     * @return 查询器或更新器
     */
    public WHERE notBetween(boolean condition, Object value1, Object value2) {
        return condition ? this.notBetween(value1, value2) : segment;
    }

    //////

    /**
     * eq_IfNotBlank
     *
     * @param value 条件值
     * @return self
     */
    public WHERE eq_IfNotBlank(String value) {
        return this.eq(isNotEmpty(value), value);
    }

    /**
     * ne_IfNotBlank
     *
     * @param value 条件值
     * @return self
     */
    public WHERE ne_IfNotBlank(String value) {
        return this.ne(isNotEmpty(value), value);
    }

    /**
     * gt_IfNotBlank
     *
     * @param value 条件值
     * @return self
     */
    public WHERE gt_IfNotBlank(String value) {
        return this.gt(isNotEmpty(value), value);
    }

    /**
     * ge_IfNotBlank
     *
     * @param value 条件值
     * @return self
     */
    public WHERE ge_IfNotBlank(String value) {
        return this.ge(isNotEmpty(value), value);
    }

    /**
     * lt_IfNotBlank
     *
     * @param value 条件值
     * @return self
     */
    public WHERE lt_IfNotBlank(String value) {
        return this.lt(isNotEmpty(value), value);
    }

    /**
     * le_IfNotBlank
     *
     * @param value 条件值
     * @return self
     */
    public WHERE le_IfNotBlank(String value) {
        return this.le(isNotEmpty(value), value);
    }

    //like

    /**
     * like '%value%'
     *
     * @param value 条件值
     * @return self
     */
    public WHERE like(String value) {
        assertNotBlank(current.name, value);
        return this.segment.apply(current.column, LIKE, SqlLike.like(value));
    }

    /**
     * like '%value%'
     *
     * @param condition 成立条件
     * @param value     条件值
     * @return self
     */
    public WHERE like(boolean condition, String value) {
        return condition ? this.like(value) : segment;
    }


    /**
     * value不为空时, 执行 like '%value%'
     *
     * @param value 条件值
     * @return self
     */
    public WHERE like_IfNotBlank(String value) {
        return this.like(isNotEmpty(value), value);
    }

    //not like

    /**
     * not like '%value%'
     *
     * @param value 条件值
     * @return self
     */
    public WHERE notLike(String value) {
        assertNotBlank(current.name, value);
        return this.segment.apply(current.column, NOT_LIKE, SqlLike.like(value));
    }

    /**
     * not like '%value%'
     *
     * @param condition 成立条件
     * @param value     条件值
     * @return self
     */
    public WHERE notLike(boolean condition, String value) {
        return condition ? this.notLike(value) : segment;
    }

    /**
     * not like '%value%'
     *
     * @param value 条件值
     * @return self
     */
    public WHERE notLike_IfNotBlank(String value) {
        return this.notLike(isNotEmpty(value), value);
    }

    //like left

    /**
     * like '%value'
     *
     * @param value
     * @return
     */
    public WHERE likeLeft(String value) {
        assertNotBlank(current.name, value);
        return this.segment.apply(current.column, LIKE, SqlLike.left(value));
    }

    public WHERE likeLeft(boolean condition, String value) {
        return condition ? this.likeLeft(value) : segment;
    }


    public WHERE likeLeft_IfNotBlank(String value) {
        return this.likeLeft(isNotEmpty(value), value);
    }

    //like right

    /**
     * like 'value%'
     *
     * @param value
     * @return
     */
    public WHERE likeRight(String value) {
        assertNotBlank(current.name, value);
        return this.segment.apply(current.column, LIKE, SqlLike.right(value));
    }

    public WHERE likeRight(boolean condition, String value) {
        return condition ? this.likeRight(value) : segment;
    }

    public WHERE likeRight_IfNotBlank(String value) {
        return this.likeRight(isNotEmpty(value), value);
    }
    //////////

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