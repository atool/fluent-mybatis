package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.If;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.IRef;
import cn.org.atool.fluent.mybatis.base.crud.*;
import cn.org.atool.fluent.mybatis.base.model.Column;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.base.model.ISqlOp;
import cn.org.atool.fluent.mybatis.functions.IGetter;
import cn.org.atool.fluent.mybatis.functions.QFunction;
import cn.org.atool.fluent.mybatis.segment.model.KeyWordSegment;
import cn.org.atool.fluent.mybatis.segment.model.Parameters;
import cn.org.atool.fluent.mybatis.utility.MappingKits;
import cn.org.atool.fluent.mybatis.utility.NestedQueryFactory;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static cn.org.atool.fluent.mybatis.If.notNull;
import static cn.org.atool.fluent.mybatis.base.model.Column.EMPTY_COLUMN;
import static cn.org.atool.fluent.mybatis.base.model.SqlOp.*;
import static cn.org.atool.fluent.mybatis.segment.model.KeyWordSegment.AND;
import static cn.org.atool.fluent.mybatis.segment.model.KeyWordSegment.OR;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.assertNotNull;

/**
 * BaseQueryAnd: AND或者OR操作基类
 *
 * @param <WHERE>   更新器
 * @param <WRAPPER> 查询或者更新
 * @param <NestedQ> WRAPPER 对应的嵌套查询器
 * @author darui.wu
 */
@SuppressWarnings({"unchecked", "unused", "rawtypes"})
@Accessors(chain = true)
public abstract class WhereBase<
    WHERE extends WhereBase<WHERE, WRAPPER, NestedQ>,
    WRAPPER extends IWrapper<?, WRAPPER, NestedQ>,
    NestedQ extends IBaseQuery<?, NestedQ>
    >
    extends BaseSegment<WhereApply<WHERE, NestedQ>, WRAPPER> {

    private final WhereApply<WHERE, NestedQ> apply = new WhereApply<>((WHERE) this);

    /**
     * 当前连接符: AND 连接, OR 连接
     */
    private final KeyWordSegment currOp;

    public WHERE and;

    public WHERE or;

    protected WhereBase(WRAPPER wrapper) {
        super(wrapper);
        this.currOp = AND;
        this.and = (WHERE) this;
        this.or = this.buildOr(this.and);
    }

    protected WhereBase(WRAPPER wrapper, WHERE and) {
        super(wrapper);
        this.currOp = OR;
        this.and = and;
        this.or = (WHERE) this;
    }

    /**
     * 根据and where构造or where实例
     *
     * @param and and where
     * @return or where
     */
    protected abstract WHERE buildOr(WHERE and);

    /**
     * 设置默认条件
     * <p>
     * Query: {@link IDefaultSetter#setQueryDefault(IQuery)}
     * Update: {@link IDefaultSetter#setUpdateDefault(IUpdate)}
     *
     * @return WHERE
     */
    public WHERE defaults() {
        BaseDefaults defaults = IRef.instance().defaults(this.wrapper.entityClass);
        if (defaults == null) {
            return this.and;
        }
        if (wrapper instanceof IQuery) {
            defaults.defaultSetter().setQueryDefault((IQuery) this.wrapper);
        } else if (wrapper instanceof IUpdate) {
            defaults.defaultSetter().setUpdateDefault((IUpdate) wrapper);
        }
        return this.and;
    }

    /**
     * 根据entity非空字段设置where条件
     * <p>
     * replaced by {@link #eqByEntity(IEntity, String...)}
     *
     * @param entity 实例
     * @return 查询器StudentQuery
     */
    @Deprecated
    public WHERE eqNotNull(IEntity entity) {
        return this.eqByEntity(entity);
    }

    /**
     * 根据entity和字段predicate判断来设置where条件
     *
     * @param entity    实例
     * @param predicate 判断字段是否作为where条件 (columnName, columnValue)->{}
     * @return WHERE
     */
    public WHERE eqByEntity(IEntity entity, BiPredicate<String, Object> predicate) {
        assertNotNull("entity", entity);
        Map<String, Object> map = entity.toColumnMap(false);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String column = entry.getKey();
            Object value = entry.getValue();
            if (predicate.test(column, value)) {
                if (value == null) {
                    this.apply(column, IS_NULL);
                } else {
                    this.apply(column, EQ, value);
                }
            }
        }
        return this.and;
    }

    /**
     * 根据entity设置where条件
     *
     * <pre>
     * o 指定字段列表, 可以是 null 值
     * o 无指定字段时, 所有非空entity字段
     * </pre>
     *
     * @param entity  实例
     * @param columns 要设置条件的字段
     * @return 查询器StudentQuery
     */
    public WHERE eqByEntity(IEntity entity, String... columns) {
        super.byEntity(entity, (column, value) -> {
            if (value == null) {
                this.apply(column, IS_NULL);
            } else {
                this.apply(column, EQ, value);
            }
        }, true, Arrays.asList(columns));
        return this.and;
    }

    /**
     * 根据entity指定字段(允许null)设置where条件
     *
     * @param entity  实例
     * @param column  要设置条件的字段
     * @param columns 要设置条件的字段
     * @return 查询器StudentQuery
     */
    public WHERE eqByEntity(IEntity entity, FieldMapping column, FieldMapping... columns) {
        assertNotNull("entity", entity);
        String[] arr = MappingKits.toColumns(column, columns);
        return this.eqByEntity(entity, arr);
    }

    /**
     * 根据entity指定字段(允许null)设置where条件
     *
     * @param entity  实例
     * @param column  要设置条件的字段
     * @param columns 要设置条件的字段
     * @return 查询器StudentQuery
     */
    public <E extends IEntity> WHERE eqByEntity(E entity, IGetter<E> column, IGetter<E>... columns) {
        assertNotNull("entity", entity);
        Class klass = IRef.instance().findFluentEntityClass(entity.getClass());
        String[] arr = MappingKits.toColumns(klass, column, columns);
        return this.eqByEntity(entity, arr);
    }

    /**
     * 根据entity(排除指定字段)设置where条件
     *
     * <pre>
     * o 无指定字段时, 条件等于所有字段(包括null值)
     * </pre>
     *
     * @param entity   实例
     * @param excludes 排除设置条件的字段
     * @return 查询器StudentQuery
     */
    public WHERE eqByExclude(IEntity entity, String... excludes) {
        super.byExclude(entity, (column, value) -> {
            if (value == null) {
                this.apply(column, IS_NULL);
            } else {
                this.apply(column, EQ, value);
            }
        }, true, Arrays.asList(excludes));
        return this.and;
    }

    public <E extends IEntity> WHERE eqByExclude(E entity, IGetter<E> exclude, IGetter<E>... excludes) {
        assertNotNull("entity", entity);
        Class klass = IRef.instance().findFluentEntityClass(entity.getClass());
        String[] arr = MappingKits.toColumns(klass, exclude, excludes);
        return this.eqByExclude(entity, arr);
    }

    public WHERE eqByExclude(IEntity entity, FieldMapping exclude, FieldMapping... excludes) {
        assertNotNull("entity", entity);
        String[] arr = MappingKits.toColumns(exclude, excludes);
        return this.eqByExclude(entity, arr);
    }

    /**
     * map 所有非空属性等于 =
     * key: column字段名称
     * value: 设置值, 忽略null值
     *
     * @param params map 类型的参数, key 是字段名, value 是字段值
     * @param <V>    值类型
     * @return self
     */
    public <V> WHERE eqNotNull(Map<String, V> params) {
        return eqMap(params, true);
    }

    /**
     * map 所有非空属性等于 =
     * key: column字段名称
     * value: 设置值
     *
     * @param params     map 类型的参数, key 是字段名, value 是字段值
     * @param ignoreNull value为null时,是否忽略。如果ignoreNull = false, 且value=null, 会执行 column is null判断
     * @return self
     */
    public <V> WHERE eqMap(Map<String, V> params, boolean ignoreNull) {
        params.forEach((k, v) -> {
            Column column = Column.column(k, this.wrapper);
            if (notNull(v)) {
                this.wrapper.getWrapperData().apply(AND, column, EQ, v);
            } else if (!ignoreNull) {
                this.wrapper.getWrapperData().apply(AND, column, IS_NULL);
            }
        });
        return this.and;
    }

    /**
     * EXISTS ( sql语句 )
     *
     * <p>例: EXISTS("select id from table where age = 1")</p>
     *
     * @param select exists sql语句
     * @param values 参数, 对应 select 语句中的 "?" 占位符
     * @return self
     */
    public WHERE exists(String select, Object... values) {
        wrapper.getWrapperData().apply(currOp, EMPTY_COLUMN, EXISTS, select, values);
        return this.and;
    }

    /**
     * use {@link #exists(Predicate, String, Object...)}
     */
    @Deprecated
    public WHERE exists(boolean condition, String select, Object... values) {
        return this.exists(a -> condition, select, values);
    }

    /**
     * EXISTS ( sql语句 )
     *
     * <p>例: EXISTS("select id from table where age = 1")</p>
     *
     * @param predicate true时条件成立
     * @param select    exists sql语句
     * @param values    参数, 对应 select 语句中的 "?" 占位符
     * @return self
     */
    public WHERE exists(Predicate<Object[]> predicate, String select, Object... values) {
        return predicate.test(values) ? this.exists(select, values) : this.and;
    }

    /**
     * EXISTS ( sql语句 )
     *
     * <p>例: EXISTS("select id from table where age = 1")</p>
     *
     * @param query 嵌套查询
     * @return self
     */
    public WHERE exists(QFunction<NestedQ> query) {
        NestedQ nestQuery = NestedQueryFactory.nested(this.wrapper, false);
        query.apply(nestQuery);
        return this.exists(nestQuery);
    }

    /**
     * EXISTS ( sql语句 )
     *
     * @param condition 条件为真时成立
     * @param query     子查询
     * @return WHERE
     */
    public WHERE exists(boolean condition, QFunction<NestedQ> query) {
        return condition ? this.exists(query) : this.and;
    }

    /**
     * EXISTS ( sql语句 )
     *
     * <p>例: EXISTS("select id from table where age = 1")</p>
     *
     * @param query 嵌套查询
     * @return self
     */
    public WHERE exists(IQuery query) {
        ((BaseWrapper) query).sharedParameter(wrapper);
        wrapper.getWrapperData().apply(currOp, EMPTY_COLUMN, EXISTS, query.getWrapperData().sqlWithoutPaged());
        return this.and;
    }

    /**
     * EXISTS ( sql语句 )
     *
     * @param condition 条件为真时成立
     * @param query     子查询
     * @return WHERE
     */
    public WHERE exists(boolean condition, IQuery query) {
        if (condition) {
            ((BaseWrapper) query).sharedParameter(wrapper);
            wrapper.getWrapperData().apply(currOp, EMPTY_COLUMN, EXISTS, query.getWrapperData().sqlWithoutPaged());
        }
        return this.and;
    }

    /**
     * NOT EXISTS ( sql语句 )
     *
     * <p>例: NOT EXISTS("select id from table where age = 1")</p>
     *
     * @param select not exists sql语句
     * @param values select语句参数, 对应notExistsSql语句中的 "?" 占位符
     * @return self
     */
    public WHERE notExists(String select, Object... values) {
        wrapper.getWrapperData().apply(currOp, EMPTY_COLUMN, NOT_EXISTS, select, values);
        return this.and;
    }

    /**
     * use {@link #notExists(Predicate, String, Object...)}
     */
    @Deprecated
    public WHERE notExists(boolean condition, String select, Object... values) {
        return this.notExists(a -> condition, select, values);
    }

    /**
     * NOT EXISTS ( sql语句 )
     *
     * <p>例: NOT EXISTS("select id from table where age = 1")</p>
     *
     * @param predicate true时条件成立
     * @param select    not exists sql语句
     * @param values    select语句参数, 对应notExistsSql语句中的 "?" 占位符
     * @return self
     */
    public WHERE notExists(Predicate<Object[]> predicate, String select, Object... values) {
        return predicate.test(values) ? this.notExists(select, values) : this.and;
    }

    /**
     * NOT EXISTS ( sql语句 )
     *
     * <p>例: NOT EXISTS("select id from table where age = 1")</p>
     *
     * @param query 嵌套查询
     * @return self
     */
    public WHERE notExists(QFunction<NestedQ> query) {
        NestedQ nestQuery = NestedQueryFactory.nested(this.wrapper, false);
        query.apply(nestQuery);
        return this.notExists(nestQuery);
    }

    /**
     * NOT EXISTS ( sql语句 )
     *
     * <p>例: NOT EXISTS("select id from table where age = 1")</p>
     *
     * @param condition true时条件成立
     * @param query     嵌套查询
     * @return self
     */
    public WHERE notExists(boolean condition, QFunction<NestedQ> query) {
        return condition ? this.notExists(query) : this.and;
    }

    /**
     * NOT EXISTS ( sql语句 )
     *
     * <p>例: NOT EXISTS("select id from table where age = 1")</p>
     *
     * @param query 子查询
     * @return self
     */
    public WHERE notExists(IQuery query) {
        ((BaseWrapper) query).sharedParameter(wrapper);
        wrapper.getWrapperData().apply(currOp, EMPTY_COLUMN, NOT_EXISTS, query.getWrapperData().sqlWithoutPaged());
        return this.and;
    }

    /**
     * NOT EXISTS ( sql语句 )
     *
     * <p>例: NOT EXISTS("select id from table where age = 1")</p>
     *
     * @param condition true时条件成立
     * @param query     嵌套查询
     * @return self
     */
    public WHERE notExists(boolean condition, IQuery query) {
        return condition ? this.notExists(query) : this.and;
    }

    /**
     * 拼接 sql
     *
     * <p>例1: and[or] apply("id = 1")</p>
     * <p>例2: and[or] apply("date_format(dateColumn,'%Y-%m-%d') = '2008-08-08'")</p>
     * <p>例3: and[or] apply("date_format(dateColumn,'%Y-%m-%d') = ?", LocalDate.now())</p>
     *
     * @param applySql 拼接的sql语句
     * @param paras    对应sql语句的 "?" 参数
     * @return children
     */
    public WHERE applyFunc(String applySql, Object... paras) {
        wrapper.getWrapperData().apply(this.currOp, EMPTY_COLUMN, RETAIN, applySql, paras);
        return this.and;
    }

    /**
     * 自定义column赋值比较
     *
     * @param column 自定义的column值
     * @return WhereApply
     */
    public WhereApply<WHERE, NestedQ> apply(String column) {
        return this.set(new FieldMapping(null, column));
    }

    /**
     * 根据条件拼接 sql
     *
     * @param predicate if true: 拼接applySql; false: 丢弃条件拼接
     * @param applySql  要拼接的sql语句
     * @param paras     sql参数
     * @return WHERE
     */
    public WHERE applyIf(Predicate<Object[]> predicate, String applySql, Object... paras) {
        if (predicate.test(paras)) {
            this.applyFunc(applySql, paras);
        }
        return this.and;
    }

    /**
     * 增加and[or]条件
     *
     * @param column 字段
     * @param op     操作
     * @param paras  操作参数
     * @return 条件设置器
     */
    public WHERE apply(String column, ISqlOp op, Object... paras) {
        Column _column = Column.column(column, this.wrapper);
        this.wrapper.getWrapperData().apply(this.currOp, _column, op, paras);
        return this.and;
    }

    public WHERE applyIf(Predicate<Object[]> predicate, String column, ISqlOp op, Object... paras) {
        if (predicate.test(paras)) {
            this.apply(column, op, paras);
        }
        return this.and;
    }

    /**
     * 增加and[or]条件
     *
     * @param column 字段
     * @param op     操作
     * @param paras  操作参数
     * @return 条件设置器
     */
    public WHERE apply(Column column, ISqlOp op, Object... paras) {
        this.wrapper.getWrapperData().apply(this.currOp, column, op, paras);
        return this.and;
    }

    public WHERE applyIf(Predicate<Object[]> predicate, Column column, ISqlOp op, Object... paras) {
        if (predicate.test(paras)) {
            this.apply(column, op, paras);
        }
        return this.and;
    }

    /**
     * column op (format(sql, args))
     *
     * @param column     字段
     * @param op         条件操作
     * @param expression 函数或sql片段
     * @param args       参数
     * @return WHERE
     */
    WHERE apply(Column column, ISqlOp op, String expression, Object... args) {
        this.wrapper.getWrapperData().apply(this.currOp, column, op, expression, args);
        return this.and;
    }

    WHERE applyIf(Predicate<Object[]> predicate, Column column, ISqlOp op, String expression, Object... args) {
        if (predicate.test(args)) {
            this.apply(column, op, expression, args);
        }
        return this.and;
    }

    /**
     * 嵌套查询
     * <p>
     * 例: and(i -&gt; i.eq("name", "value1").ne("status", "status1"))
     * </p>
     *
     * @param query 消费函数
     * @return children
     */
    public WHERE and(QFunction<NestedQ> query) {
        return this.nestedWhere(AND, query);
    }

    /**
     * 嵌套查询
     * <p>
     * 例: or(i -&gt; i.eq("name", "value1").ne("status", "status1"))
     * </p>
     *
     * @param query 消费函数
     * @return children
     */
    public WHERE or(QFunction<NestedQ> query) {
        return this.nestedWhere(OR, query);
    }

    /**
     * and (子条件)
     *
     * @param query 子查询
     * @return WHERE
     */
    public WHERE and(IQuery query) {
        return nestedWhere(AND, query);
    }

    /**
     * or (子条件)
     *
     * @param query 子查询
     * @return WHERE
     */
    public WHERE or(IQuery query) {
        return nestedWhere(OR, query);
    }

    private WHERE nestedWhere(KeyWordSegment andOr, QFunction query) {
        final IQuery nested = NestedQueryFactory.nested(this.wrapper, true);
        query.apply(nested);
        return this.nestedWhere(andOr, nested);
    }

    private WHERE nestedWhere(KeyWordSegment andOr, IQuery query) {
        String sql = query.getWrapperData().getMergeSql();
        if (If.notBlank(sql)) {
            ((BaseWrapper) query).sharedParameter(this.wrapper);
            wrapper.getWrapperData().apply(andOr, EMPTY_COLUMN, BRACKET, sql);
        }
        return this.and;
    }

    Parameters getParameters() {
        return this.wrapper.getWrapperData().getParameters();
    }

    @Override
    protected WhereApply apply() {
        return this.apply;
    }
}