package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.IJoinQuery;
import cn.org.atool.fluent.mybatis.base.IQuery;
import cn.org.atool.fluent.mybatis.base.impl.BaseQuery;
import cn.org.atool.fluent.mybatis.functions.JoinConsumer;
import cn.org.atool.fluent.mybatis.metadata.JoinType;
import cn.org.atool.fluent.mybatis.segment.model.PagedOffset;
import cn.org.atool.fluent.mybatis.segment.model.ParameterPair;
import lombok.Getter;

import java.util.function.Function;

import static cn.org.atool.fluent.mybatis.segment.JoinWrapperData.T1_ALIAS;
import static cn.org.atool.fluent.mybatis.segment.JoinWrapperData.T2_ALIAS;

public class JoinQuery<Q1 extends BaseQuery<?, Q1>, Q2 extends BaseQuery<?, Q2>>
    implements IQuery<IEntity, JoinQuery<Q1, Q2>>, IJoinQuery<Q1, Q2> {
    private Class<Q1> query1Class;

    private Q1 query1;

    private Class<Q2> query2Class;

    private Q2 query2;

    @Getter
    private JoinWrapperData wrapperData;

    public JoinQuery(Class<Q1> query1Class, Class<Q2> query2Class) {
        this.query1Class = query1Class;
        this.query2Class = query2Class;
        try {
            ParameterPair parameters = new ParameterPair();
            this.query1 = query1Class.getConstructor(String.class, ParameterPair.class).newInstance(T1_ALIAS, parameters);
            this.query2 = newQueryInstance(query2Class, parameters);
            this.wrapperData = new JoinWrapperData(parameters, this.query1, this.query2);
        } catch (Exception e) {
            throw new RuntimeException("new JoinQuery Instance error: " + e.getMessage(), e);
        }
    }

    private Q2 newQueryInstance(Class<Q2> query2Class, ParameterPair parameters) throws InstantiationException, IllegalAccessException, java.lang.reflect.InvocationTargetException, NoSuchMethodException {
        return query2Class.getConstructor(String.class, ParameterPair.class).newInstance(T2_ALIAS, parameters);
    }

    public static <Q1 extends BaseQuery<?, Q1>, Q2 extends BaseQuery<?, Q2>>
    IJoinQuery<Q1, Q2> create(Class<Q1> query1, Class<Q2> query2) {
        return new JoinQuery<>(query1, query2);
    }

    public IJoinQuery<Q1, Q2> join(Function<Q1, Q1> query1,
                                   Function<Q2, Q2> query2,
                                   JoinConsumer<Q1, Q2> join) {
        return join(JoinType.Join, query1, query2, join);
    }

    public IJoinQuery<Q1, Q2> leftJoin(Function<Q1, Q1> query1,
                                       Function<Q2, Q2> query2,
                                       JoinConsumer<Q1, Q2> join) {
        return join(JoinType.LeftJoin, query1, query2, join);
    }

    public IJoinQuery<Q1, Q2> rightJoin(Function<Q1, Q1> query1,
                                        Function<Q2, Q2> query2,
                                        JoinConsumer<Q1, Q2> join) {
        return join(JoinType.RightJoin, query1, query2, join);
    }

    private IJoinQuery<Q1, Q2> join(JoinType joinType,
                                    Function<Q1, Q1> query1,
                                    Function<Q2, Q2> query2,
                                    JoinConsumer<Q1, Q2> join) {
        this.wrapperData.setJoinType(joinType);
        query1.apply(this.query1);
        query2.apply(this.query2);
        join.accept(this.wrapperData, newEmptyQuery(this.query1Class), newEmptyQuery(this.query2Class));
        return this;
    }

    /**
     * 执行on条件时, 新创建查询对象, 避免对原有对象的造成干扰
     *
     * @param klass
     * @param <Q>
     * @return
     */
    private <Q extends BaseQuery<?, Q>> Q newEmptyQuery(Class<Q> klass) {
        try {
            return klass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(String.format("new %s() error: %s", klass.getSimpleName(), e.getMessage()), e);
        }
    }

    @Override
    public JoinQuery<Q1, Q2> distinct() {
        this.wrapperData.setDistinct(true);
        return this;
    }

    @Override
    public JoinQuery<Q1, Q2> limit(int limit) {
        this.wrapperData.setPaged(new PagedOffset(0, limit));
        return this;
    }

    @Override
    public JoinQuery<Q1, Q2> limit(int start, int limit) {
        this.wrapperData.setPaged(new PagedOffset(start, limit));
        return this;
    }

    @Override
    public JoinQuery<Q1, Q2> last(String lastSql) {
        this.wrapperData.last(lastSql);
        return this;
    }

    @Override
    public JoinQuery<Q1, Q2> selectId() {
        throw new RuntimeException("not support");
    }

    @Override
    public WhereBase<?, JoinQuery<Q1, Q2>, JoinQuery<Q1, Q2>> where() {
        throw new RuntimeException("not support");
    }
}