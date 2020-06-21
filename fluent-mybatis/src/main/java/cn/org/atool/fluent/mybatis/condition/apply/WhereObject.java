package cn.org.atool.fluent.mybatis.condition.apply;

import cn.org.atool.fluent.mybatis.condition.base.BaseWhere;
import cn.org.atool.fluent.mybatis.condition.base.Wrapper;
import cn.org.atool.fluent.mybatis.condition.model.KeyWordSegment;
import cn.org.atool.fluent.mybatis.interfaces.IEntity;
import cn.org.atool.fluent.mybatis.interfaces.IQuery;
import cn.org.atool.fluent.mybatis.utility.NestedQueryFactory;

import java.util.Collection;
import java.util.function.Consumer;

import static cn.org.atool.fluent.mybatis.condition.model.SqlOp.*;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.assertNotEmpty;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.assertNotNull;

/**
 * AndObject
 *
 * @param <E> 对应的实体类
 * @param <V> 值类型
 * @param <W> 更新器或查询器
 * @param <Q> 对应的嵌套查询器
 * @author darui.wu
 */
public class WhereObject<E extends IEntity, V, W extends Wrapper<E, W, Q>, Q extends IQuery<E, Q>> {

    protected final BaseWhere queryAnd;

    protected final KeyWordSegment orAnd;

    protected final String column;

    protected final String property;

    public WhereObject(BaseWhere queryAnd, String column, String property) {
        this.queryAnd = queryAnd;
        this.orAnd = this.queryAnd.getOrAnd();
        this.column = column;
        this.property = property;
    }

    /**
     * is null
     *
     * @return 查询器或更新器
     */
    public W isNull() {
        return this.wrapper().apply(orAnd, column, IS_NULL);
    }

    /**
     * is null
     *
     * @param condition 条件为真时成立
     * @return 查询器或更新器
     */
    public W isNull(boolean condition) {
        return condition ? this.isNull() : wrapper();
    }

    /**
     * not null
     *
     * @return 查询器或更新器
     */
    public W isNotNull() {
        return this.wrapper().apply(orAnd, column, IS_NOT_NULL);
    }

    /**
     * not null
     *
     * @param condition 条件为真时成立
     * @return 查询器或更新器
     */
    public W isNotNull(boolean condition) {
        return condition ? this.isNotNull() : wrapper();
    }
    // eq

    /**
     * 等于 =
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    public W eq(V value) {
        assertNotNull(property, value);
        return this.wrapper().apply(orAnd, column, EQ, value);
    }

    /**
     * 等于 =
     *
     * @param condition 条件为真时成立
     * @param value     条件值
     * @return 查询器或更新器
     */
    public W eq(boolean condition, V value) {
        return condition ? this.eq(value) : wrapper();
    }


    /**
     * 等于 =, 值不为空时成立
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    public W eq_IfNotNull(V value) {
        return this.eq(value != null, value);
    }


    // ne

    /**
     * 不等于 !=
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    public W ne(V value) {
        assertNotNull(property, value);
        return this.wrapper().apply(orAnd, column, NE, value);
    }

    /**
     * 不等于 !=
     *
     * @param condition 为真时成立
     * @param value     条件值
     * @return 查询器或更新器
     */
    public W ne(boolean condition, V value) {
        return condition ? this.ne(value) : wrapper();
    }


    /**
     * 不等于 !=
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    public W ne_IfNotNull(V value) {
        return this.ne(value != null, value);
    }


    //gt

    /**
     * 大于
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    public W gt(V value) {
        assertNotNull(property, value);
        return this.wrapper().apply(orAnd, column, GT, value);
    }

    /**
     * 大于
     *
     * @param condition 为真时成立
     * @param value     条件值
     * @return 查询器或更新器
     */
    public W gt(boolean condition, V value) {
        return condition ? this.gt(value) : wrapper();
    }

    /**
     * 大于
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    public W gt_IfNotNull(V value) {
        return this.gt(value != null, value);
    }


    //ge

    /**
     * 大于等于
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    public W ge(V value) {
        assertNotNull(property, value);
        return this.wrapper().apply(orAnd, column, GE, value);
    }

    /**
     * 大于等于
     *
     * @param condition 为真时成立
     * @param value     条件值
     * @return 查询器或更新器
     */
    public W ge(boolean condition, V value) {
        return condition ? this.ge(value) : wrapper();
    }


    /**
     * 大于等于
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    public W ge_IfNotNull(V value) {
        return this.ge(value != null, value);
    }


    //lt

    /**
     * 小于
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    public W lt(V value) {
        assertNotNull(property, value);
        return this.wrapper().apply(orAnd, column, LT, value);
    }

    /**
     * 小于
     *
     * @param condition 为真时成立
     * @param value     条件值
     * @return 查询器或更新器
     */
    public W lt(boolean condition, V value) {
        return condition ? this.lt(value) : wrapper();
    }


    /**
     * 小于
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    public W lt_IfNotNull(V value) {
        return this.lt(value != null, value);
    }


    //le

    /**
     * 小于等于
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    public W le(V value) {
        assertNotNull(property, value);
        return this.wrapper().apply(orAnd, column, LE, value);
    }

    /**
     * 小于等于
     *
     * @param condition 为真时成立
     * @param value     条件值
     * @return 查询器或更新器
     */
    public W le(boolean condition, V value) {
        return condition ? this.le(value) : wrapper();
    }


    /**
     * 小于等于
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    public W le_IfNotNull(V value) {
        return this.le(value != null, value);
    }


    //in

    /**
     * @param values 条件值
     * @return 查询器或更新器
     */
    public W in(Collection<V> values) {
        assertNotEmpty(property, values);
        return this.wrapper().apply(orAnd, column, IN, values.toArray());
    }

    /**
     * @param condition 为真时成立
     * @param values    条件值
     * @return 查询器或更新器
     */
    public W in(boolean condition, Collection<V> values) {
        return condition ? this.in(values) : wrapper();
    }


    /**
     * @param values 条件值
     * @return 查询器或更新器
     */
    public W in_IfNotEmpty(Collection<V> values) {
        return this.in(values != null && !values.isEmpty(), values);
    }


    /**
     * in (values)
     *
     * @param values 条件值
     * @return 查询器或更新器
     */
    public W in(V... values) {
        assertNotEmpty(property, values);
        return this.wrapper().apply(orAnd, column, IN, values);
    }

    /**
     * in (values)
     *
     * @param condition 为真时成立
     * @param values    条件值
     * @return 查询器或更新器
     */
    public W in(boolean condition, V... values) {
        return condition ? this.in(values) : wrapper();
    }

    /**
     * where column IN (select ... )
     *
     * @param select 子查询语句
     * @param values 子查询语句参数，对应select语句里面的 "?" 占位符
     * @return 查询器或更新器
     */
    public W inSql(String select, Object... values) {
        return this.wrapper().apply(orAnd, column, select, IN, values);
    }

    /**
     * in (select ... )
     *
     * @param query 嵌套查询
     * @return 查询器或更新器
     */
    public W in(Consumer<Q> query) {
        return this.in(this.wrapper().queryClass(), query);
    }

    /**
     * in (select ... )
     *
     * @param klass 嵌套查询类
     * @param query 嵌套查询
     * @param <NQ>  嵌套查询类
     * @return 查询器或更新器
     */
    public <NQ extends IQuery> W in(Class<NQ> klass, Consumer<NQ> query) {
        NQ nested = NestedQueryFactory.nested(klass, this.wrapper().getParameters());
        query.accept(nested);
        return this.wrapper().apply(orAnd, column, nested.getQuerySql(), IN);
    }

    /**
     * in (values)
     *
     * @param values 条件值
     * @return 查询器或更新器
     */
    public W in_IfNotEmpty(V... values) {
        return this.in(values != null && values.length > 0, values);
    }

    //not in

    /**
     * not in (values)
     *
     * @param values 条件值
     * @return 查询器或更新器
     */
    public W notIn(Collection<V> values) {
        assertNotEmpty(property, values);
        return this.wrapper().apply(orAnd, column, NOT_IN, values.toArray());
    }

    /**
     * not in (values)
     *
     * @param condition 为真时成立
     * @param values    条件值
     * @return 查询器或更新器
     */
    public W notIn(boolean condition, Collection<V> values) {
        return condition ? this.notIn(values) : wrapper();
    }


    /**
     * not in (values)
     *
     * @param values 条件值
     * @return 查询器或更新器
     */
    public W notIn_IfNotEmpty(Collection<V> values) {
        return this.notIn(values != null && !values.isEmpty(), values);
    }

    /**
     * not in (values)
     *
     * @param values 条件值
     * @return 查询器或更新器
     */
    public W notIn(V... values) {
        assertNotEmpty(property, values);
        return this.wrapper().apply(orAnd, column, NOT_IN, values);
    }

    /**
     * not in (values)
     *
     * @param condition 为真时成立
     * @param values    条件值
     * @return 查询器或更新器
     */
    public W notIn(boolean condition, V... values) {
        return condition ? this.notIn(values) : wrapper();
    }

    /**
     * not in (values)
     *
     * @param values 条件值
     * @return 查询器或更新器
     */
    public W notIn_IfNotEmpty(V... values) {
        return this.notIn(values != null && values.length > 0, values);
    }

    /**
     * not in (select ... )
     *
     * @param query 嵌套查询
     * @return 查询器或更新器
     */
    public W notIn(Consumer<Q> query) {
        return this.notIn(this.wrapper().queryClass(), query);
    }

    /**
     * not in (select ... )
     *
     * @param queryClass 嵌套查询类
     * @param query      嵌套查询
     * @param <NQ>       嵌套查询类
     * @return 查询器或更新器
     */
    public <NQ extends IQuery<?, NQ>> W notIn(Class<NQ> queryClass, Consumer<NQ> query) {
        NQ nested = NestedQueryFactory.nested(queryClass, this.wrapper().getParameters());
        query.accept(nested);
        return this.wrapper().apply(orAnd, column, nested.getQuerySql(), NOT_IN);
    }
    //between

    /**
     * @param value1 条件值
     * @param value2 条件值
     * @return 查询器或更新器
     */
    public W between(V value1, V value2) {
        assertNotNull(property, value1, value2);
        return this.wrapper().apply(orAnd, column, BETWEEN, value1, value2);
    }

    /**
     * @param condition 为真时成立
     * @param value1    条件值
     * @param value2    条件值
     * @return 查询器或更新器
     */
    public W between(boolean condition, V value1, V value2) {
        return condition ? this.between(value1, value2) : wrapper();
    }

    //not between

    /**
     * @param value1 条件值
     * @param value2 条件值
     * @return 查询器或更新器
     */
    public W notBetween(V value1, V value2) {
        assertNotNull(property, value1, value2);
        return this.wrapper().apply(orAnd, column, NOT_BETWEEN, value1, value2);
    }

    /**
     * @param condition 为真时成立
     * @param value1    条件值
     * @param value2    条件值
     * @return 查询器或更新器
     */
    public W notBetween(boolean condition, V value1, V value2) {
        return condition ? this.notBetween(value1, value2) : wrapper();
    }

    /**
     * set 自定义
     * ！！！慎用！！！！
     * 有sql注入风险
     *
     * @param sql 自定义操作
     * @return 查询器或更新器
     */
    public W apply(String sql) {
        return this.wrapper().apply(orAnd, column, sql, RETAIN);
    }

    /**
     * 返回对应的查询器或更新器
     *
     * @return 查询器或更新器
     */
    protected W wrapper() {
        return (W) this.queryAnd.getWrapper();
    }
}