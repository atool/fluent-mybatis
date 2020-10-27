package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.IJoinQuery;
import cn.org.atool.fluent.mybatis.base.IQuery;
import cn.org.atool.fluent.mybatis.base.JoinOn;
import cn.org.atool.fluent.mybatis.base.impl.BaseQuery;
import cn.org.atool.fluent.mybatis.functions.JoinConsumer;
import cn.org.atool.fluent.mybatis.metadata.JoinType;
import cn.org.atool.fluent.mybatis.segment.model.PagedOffset;
import cn.org.atool.fluent.mybatis.segment.model.ParameterPair;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static cn.org.atool.fluent.mybatis.segment.JoinWrapperData.T1_ALIAS;
import static cn.org.atool.fluent.mybatis.segment.JoinWrapperData.T2_ALIAS;

public class JoinQuery<Q1 extends BaseQuery<?, Q1>>
    implements IQuery<IEntity, JoinQuery<Q1>>, IJoinQuery<Q1> {
    private final Class<Q1> queryClass;

    private final Q1 query;

    private final List<BaseQuery> queries = new ArrayList<>();

    @Getter
    private JoinWrapperData wrapperData;

    public JoinQuery(Class<Q1> queryClass, Function<Q1, Q1> query) {
        this.queryClass = queryClass;
        this.query = newQuery(queryClass, T1_ALIAS, new ParameterPair());
        query.apply(this.query);
        this.wrapperData = new JoinWrapperData(this.query, this.queries);
    }

    public static <Q1 extends BaseQuery<?, Q1>> IJoinQuery<Q1> from(Class<Q1> clazz, Function<Q1, Q1> query) {
        return new JoinQuery<Q1>(clazz, query);
    }

    @Override
    public <Q2 extends BaseQuery<?, Q2>> IJoinQuery<Q1> join(Class<Q2> clazz,
                                                             Function<Q2, Q2> query,
                                                             JoinConsumer<Q1, Q2> join) {
        return join(JoinType.Join, clazz, query, join);
    }

    @Override
    public <Q2 extends BaseQuery<?, Q2>> IJoinQuery<Q1> leftJoin(Class<Q2> clazz,
                                                                 Function<Q2, Q2> query,
                                                                 JoinConsumer<Q1, Q2> join) {
        return join(JoinType.LeftJoin, clazz, query, join);
    }

    @Override
    public <Q2 extends BaseQuery<?, Q2>> IJoinQuery<Q1> rightJoin(Class<Q2> clazz,
                                                                  Function<Q2, Q2> query,
                                                                  JoinConsumer<Q1, Q2> join) {
        return join(JoinType.RightJoin, clazz, query, join);
    }

    private <Q2 extends BaseQuery<?, Q2>> IJoinQuery<Q1> join(JoinType joinType,
                                                              Class<Q2> queryClass,
                                                              Function<Q2, Q2> apply,
                                                              JoinConsumer<Q1, Q2> join) {
        Q2 query = newQuery(queryClass, T2_ALIAS, this.query.wrapperData.getParameters());
        apply.apply(query);
        JoinOn on = new JoinOn(this.query, joinType, query);
        join.accept(on, newEmptyQuery(this.queryClass), newEmptyQuery(queryClass));
        this.wrapperData.addTable(on.table());
        return this;
    }


    @Override
    public JoinQuery<Q1> distinct() {
        this.wrapperData.setDistinct(true);
        return this;
    }

    @Override
    public JoinQuery<Q1> limit(int limit) {
        this.wrapperData.setPaged(new PagedOffset(0, limit));
        return this;
    }

    @Override
    public JoinQuery<Q1> limit(int start, int limit) {
        this.wrapperData.setPaged(new PagedOffset(start, limit));
        return this;
    }

    @Override
    public JoinQuery<Q1> last(String lastSql) {
        this.wrapperData.last(lastSql);
        return this;
    }

    @Override
    public JoinQuery<Q1> selectId() {
        throw new RuntimeException("not support");
    }

    @Override
    public WhereBase<?, JoinQuery<Q1>, JoinQuery<Q1>> where() {
        throw new RuntimeException("not support");
    }

    /**
     * 执行on条件时, 新创建查询对象, 避免对原有对象的造成干扰
     *
     * @param klass
     * @param <Q>
     * @return
     */
    private static <Q extends BaseQuery<?, Q>> Q newEmptyQuery(Class<Q> klass) {
        try {
            return klass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(String.format("new %s() error: %s",
                klass.getSimpleName(), e.getMessage()), e);
        }
    }

    private static <Q extends BaseQuery<?, Q>> Q newQuery(Class<Q> queryClass, String alias, ParameterPair parameters) {
        try {
            return queryClass.getConstructor(String.class, ParameterPair.class).newInstance(alias, parameters);
        } catch (Exception e) {
            throw new RuntimeException(String.format("new %s(String, ParameterPair) error: %s",
                queryClass.getSimpleName(), e.getMessage()), e);
        }
    }
}