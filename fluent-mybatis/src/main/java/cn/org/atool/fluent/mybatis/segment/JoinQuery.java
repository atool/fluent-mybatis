package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.IQuery;
import cn.org.atool.fluent.mybatis.base.JoinBuilder;
import cn.org.atool.fluent.mybatis.base.impl.BaseQuery;
import cn.org.atool.fluent.mybatis.metadata.JoinType;
import cn.org.atool.fluent.mybatis.segment.model.PagedOffset;
import cn.org.atool.fluent.mybatis.segment.model.ParameterPair;
import lombok.Getter;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 联合查询条件
 *
 * @param <QL>
 */
public class JoinQuery<QL extends BaseQuery<?, QL>>
    implements IQuery<IEntity, JoinQuery<QL>>, JoinBuilder<QL> {
    /**
     * 主查询类型
     */
    private final Class<QL> queryClass;
    /**
     * 主查询条件
     */
    private final QL query;
    /**
     * join查询, 允许有多个join
     */
    private final List<BaseQuery> queries = new ArrayList<>();

    @Getter
    private JoinWrapperData wrapperData;

    public JoinQuery(Class<QL> queryClass, Function<QL, QL> query) {
        this.queryClass = queryClass;
        this.query = newQuery(queryClass, alias(), new ParameterPair());
        query.apply(this.query);
        this.wrapperData = new JoinWrapperData(this.query, this.queries);
    }

    @Override
    public <QR extends BaseQuery<?, QR>> JoinOn<QL, QR> join(
        Class<QR> clazz,
        Function<QR, QR> query) {
        return join(JoinType.Join, clazz, query);
    }

    @Override
    public <QR extends BaseQuery<?, QR>> JoinOn<QL, QR> leftJoin(
        Class<QR> clazz,
        Function<QR, QR> query) {
        return join(JoinType.LeftJoin, clazz, query);
    }

    @Override
    public <QR extends BaseQuery<?, QR>> JoinOn<QL, QR> rightJoin(
        Class<QR> clazz,
        Function<QR, QR> query) {
        return join(JoinType.RightJoin, clazz, query);
    }

    private <QR extends BaseQuery<?, QR>> JoinOn<QL, QR> join(
        JoinType joinType,
        Class<QR> queryClass,
        Function<QR, QR> apply) {
        QR query = newQuery(queryClass, alias(), this.query.wrapperData.getParameters());
        this.queries.add(query);
        apply.apply(query);
        return new JoinOn<>(this, this.queryClass, this.query, joinType, queryClass, query);
    }

    @Override
    public JoinQuery<QL> distinct() {
        this.wrapperData.setDistinct(true);
        return this;
    }

    @Override
    public JoinQuery<QL> limit(int limit) {
        this.wrapperData.setPaged(new PagedOffset(0, limit));
        return this;
    }

    @Override
    public JoinQuery<QL> limit(int start, int limit) {
        this.wrapperData.setPaged(new PagedOffset(start, limit));
        return this;
    }

    @Override
    public JoinQuery<QL> last(String lastSql) {
        this.wrapperData.last(lastSql);
        return this;
    }

    @Override
    public JoinQuery<QL> selectAll() {
        throw new RuntimeException("not support");
    }

    @Override
    public JoinQuery<QL> selectId() {
        throw new RuntimeException("not support");
    }

    @Override
    public WhereBase<?, JoinQuery<QL>, JoinQuery<QL>> where() {
        throw new RuntimeException("not support");
    }

    private static Map<Class, Constructor> QueryAliasConstructors = new HashMap<>(128);

    private static <Q extends BaseQuery<?, Q>> Q newQuery(Class<Q> queryClass, String alias, ParameterPair parameters) {
        try {
            if (!QueryAliasConstructors.containsKey(queryClass)) {
                QueryAliasConstructors.put(queryClass, queryClass.getConstructor(String.class, ParameterPair.class));
            }
            return (Q) QueryAliasConstructors.get(queryClass).newInstance(alias, parameters);
        } catch (Exception e) {
            throw new RuntimeException(String.format("new %s(String, ParameterPair) error: %s",
                queryClass.getSimpleName(), e.getMessage()), e);
        }
    }

    private int aliasIndex = 1;

    private String alias() {
        return String.format("t%d", aliasIndex++);
    }
}