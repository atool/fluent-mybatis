package cn.org.atool.fluent.mybatis.condition.base;

import cn.org.atool.fluent.mybatis.condition.model.KeyWordSegment;
import cn.org.atool.fluent.mybatis.condition.model.ParameterPair;
import cn.org.atool.fluent.mybatis.interfaces.IEntity;
import cn.org.atool.fluent.mybatis.interfaces.IQuery;
import cn.org.atool.fluent.mybatis.utility.NestedQueryFactory;
import lombok.Getter;

import java.util.function.Consumer;

import static cn.org.atool.fluent.mybatis.condition.model.Constants.EMPTY;
import static cn.org.atool.fluent.mybatis.condition.model.KeyWordSegment.AND;
import static cn.org.atool.fluent.mybatis.condition.model.KeyWordSegment.OR;
import static cn.org.atool.fluent.mybatis.condition.model.SqlOp.EXISTS;
import static cn.org.atool.fluent.mybatis.condition.model.SqlOp.NOT_EXISTS;

/**
 * BaseQueryAnd: AND或者OR操作基类
 *
 * @param <E> 实体对象
 * @param <W> 查询器或更新器
 * @param <Q> 对应的嵌套查询器
 * @author darui.wu
 */
public abstract class BaseWhere<E extends IEntity, W extends Wrapper<E, W, Q>, Q extends IQuery<E, Q>> {
    @Getter
    private final W wrapper;
    /**
     * AND 连接
     * OR 连接
     */
    @Getter
    private final KeyWordSegment orAnd;

    protected BaseWhere(W wrapper, boolean and) {
        this.wrapper = wrapper;
        this.orAnd = and ? AND : OR;
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
    public W exists(String select, Object... values) {
        return wrapper.apply(orAnd, EMPTY, select, EXISTS, values);
    }

    /**
     * EXISTS ( sql语句 )
     *
     * <p>例: EXISTS("select id from table where age = 1")</p>
     *
     * @param query 嵌套查询
     * @return self
     */
    public W exists(Consumer<Q> query) {
        return this.exists(wrapper.queryClass(), query);
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
    public <NQ extends IQuery<?, NQ>> W exists(Class<NQ> queryClass, Consumer<NQ> query) {
        ParameterPair parameters = wrapper.getParameters();
        NQ nestQuery = NestedQueryFactory.nested(queryClass, parameters);
        query.accept(nestQuery);
        return wrapper.apply(orAnd, EMPTY, nestQuery.getQuerySql(), EXISTS);
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
    public W notExists(String select, Object... values) {
        return wrapper.apply(orAnd, EMPTY, select, NOT_EXISTS, values);
    }

    /**
     * NOT EXISTS ( sql语句 )
     *
     * <p>例: NOT EXISTS("select id from table where age = 1")</p>
     *
     * @param query 嵌套查询
     * @return self
     */
    public W notExists(Consumer<Q> query) {
        return this.notExists(wrapper.queryClass(), query);
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
    public <Q extends IQuery<E, Q>> W notExists(Class<Q> queryClass, Consumer<Q> query) {
        ParameterPair parameters = wrapper.getParameters();
        Q nestQuery = NestedQueryFactory.nested(queryClass, parameters);
        query.accept(nestQuery);
        return wrapper.apply(orAnd, EMPTY, nestQuery.getQuerySql(), NOT_EXISTS);
    }
}