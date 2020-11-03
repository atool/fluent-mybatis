package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.IQuery;
import cn.org.atool.fluent.mybatis.base.IWrapper;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.base.model.SqlOp;
import cn.org.atool.fluent.mybatis.segment.model.KeyWordSegment;
import cn.org.atool.fluent.mybatis.segment.model.Parameters;
import cn.org.atool.fluent.mybatis.utility.NestedQueryFactory;
import lombok.experimental.Accessors;

import java.util.Map;
import java.util.function.Function;

import static cn.org.atool.fluent.mybatis.If.notNull;
import static cn.org.atool.fluent.mybatis.base.model.SqlOp.*;
import static cn.org.atool.fluent.mybatis.mapper.StrConstant.EMPTY;
import static cn.org.atool.fluent.mybatis.segment.model.KeyWordSegment.AND;
import static cn.org.atool.fluent.mybatis.segment.model.KeyWordSegment.OR;

/**
 * BaseQueryAnd: AND或者OR操作基类
 *
 * @param <WHERE>   更新器
 * @param <WRAPPER> 查询或者更新
 * @param <NestedQ> WRAPPER 对应的嵌套查询器
 * @author darui.wu
 */
@Accessors(chain = true)
public abstract class WhereBase<
    WHERE extends WhereBase<WHERE, WRAPPER, NestedQ>,
    WRAPPER extends IWrapper<?, WRAPPER, NestedQ>,
    NestedQ extends IQuery<?, NestedQ>
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
     * where条件设置为entity对象非空属性
     *
     * @param entity
     * @return 查询器StudentQuery
     */
    public WHERE eqNotNull(IEntity entity) {
        return this.eqNotNull(entity.toColumnMap());
    }

    /**
     * map 所有非空属性等于 =
     * key: column字段名称
     * value: 设置值, 忽略null值
     *
     * @param params map 类型的参数, key 是字段名, value 是字段值
     * @param <V>
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
            this.wrapper.validateColumn(k);
            if (notNull(v)) {
                this.wrapper.getWrapperData().apply(AND, k, EQ, v);
            } else if (!ignoreNull) {
                this.wrapper.getWrapperData().apply(AND, k, IS_NULL);
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
        wrapper.getWrapperData().apply(currOp, EMPTY, select, EXISTS, values);
        return this.and;
    }

    /**
     * EXISTS ( sql语句 )
     *
     * <p>例: EXISTS("select id from table where age = 1")</p>
     *
     * @param query 嵌套查询
     * @return self
     */
    public WHERE exists(Function<NestedQ, NestedQ> query) {
        return (WHERE) this.exists(wrapper.getWrapperData().getQueryClass(), query);
    }

    /**
     * EXISTS ( sql语句 )
     *
     * <p>例: EXISTS("select id from table where age = 1")</p>
     *
     * @param queryClass 嵌套查询对应的类
     * @param query      嵌套查询
     * @return self
     */
    public <ANQ extends IQuery<?, ANQ>> WHERE exists(Class<ANQ> queryClass, Function<ANQ, ANQ> query) {
        Parameters parameters = wrapper.getWrapperData().getParameters();
        ANQ nestQuery = NestedQueryFactory.nested(queryClass, parameters);
        query.apply(nestQuery);
        wrapper.getWrapperData().apply(currOp, EMPTY, nestQuery.getWrapperData().getQuerySql(), EXISTS);
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
        wrapper.getWrapperData().apply(currOp, EMPTY, select, NOT_EXISTS, values);
        return this.and;
    }

    /**
     * NOT EXISTS ( sql语句 )
     *
     * <p>例: NOT EXISTS("select id from table where age = 1")</p>
     *
     * @param query 嵌套查询
     * @return self
     */
    public WHERE notExists(Function<NestedQ, NestedQ> query) {
        return (WHERE) this.notExists(wrapper.getWrapperData().getQueryClass(), query);
    }

    /**
     * NOT EXISTS ( sql语句 )
     *
     * <p>例: NOT EXISTS("select id from table where age = 1")</p>
     *
     * @param queryClass 嵌套查询对应的类
     * @param query      嵌套查询
     * @return self
     */
    public <ANQ extends IQuery> WHERE notExists(Class<ANQ> queryClass, Function<ANQ, ANQ> query) {
        Parameters parameters = wrapper.getWrapperData().getParameters();
        ANQ nestQuery = NestedQueryFactory.nested(queryClass, parameters);
        query.apply(nestQuery);
        wrapper.getWrapperData().apply(currOp, EMPTY, nestQuery.getWrapperData().getQuerySql(), NOT_EXISTS);
        return this.and;
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
    public WHERE apply(String applySql, Object... paras) {
        wrapper.getWrapperData().apply(this.currOp, EMPTY, applySql, RETAIN, paras);
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
    public WHERE apply(String column, SqlOp op, Object... paras) {
        this.wrapper.getWrapperData().apply(this.currOp, this.columnWithAlias(column), op, paras);
        return this.and;
    }

    public WHERE apply(FieldMapping column, SqlOp op, Object... paras) {
        this.wrapper.getWrapperData().apply(this.currOp, this.columnWithAlias(column), op, paras);
        return this.and;
    }

    /**
     * column op (format(sql, args))
     *
     * @param column
     * @param sql
     * @param op
     * @param args
     * @return
     */
    WHERE apply(FieldMapping column, String sql, SqlOp op, Object... args) {
        this.wrapper.getWrapperData().apply(this.currOp, this.columnWithAlias(column), sql, op, args);
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
    public WHERE and(Function<WRAPPER, WRAPPER> query) {
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
    public WHERE or(Function<WRAPPER, WRAPPER> query) {
        return this.nestedWhere(OR, query);
    }

    private WHERE nestedWhere(KeyWordSegment andOr, Function<WRAPPER, WRAPPER> query) {
        final WRAPPER nested = NestedQueryFactory.nested(this.queryClass(), wrapper.getWrapperData().getParameters());
        query.apply(nested);
        String sql = nested.getWrapperData().getMergeSql();
        if (sql != null && !sql.trim().isEmpty()) {
            wrapper.getWrapperData().apply(andOr, EMPTY, sql, BRACKET);
        }
        return this.and;
    }

    Class queryClass() {
        return this.wrapper.getWrapperData().getQueryClass();
    }

    Parameters getParameters() {
        return this.wrapper.getWrapperData().getParameters();
    }

    @Override
    protected WhereApply apply() {
        return this.apply;
    }
}