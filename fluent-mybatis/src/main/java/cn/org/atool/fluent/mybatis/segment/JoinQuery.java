package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.*;
import cn.org.atool.fluent.mybatis.functions.QFunction;
import cn.org.atool.fluent.mybatis.metadata.JoinType;
import cn.org.atool.fluent.mybatis.segment.model.PagedOffset;
import cn.org.atool.fluent.mybatis.segment.model.Parameters;
import cn.org.atool.fluent.mybatis.utility.MybatisUtil;
import lombok.Getter;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 联合查询条件
 *
 * @param <QL>
 */
public class JoinQuery<QL extends BaseQuery<?, QL>>
    implements IBaseQuery<IEntity, JoinQuery<QL>>, JoinBuilder1<QL>, JoinBuilder2<QL> {
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

    private final Parameters parameters = new Parameters();

    @Getter
    private JoinWrapperData wrapperData;

    /**
     * 别名列表
     */
    private List<String> alias = new ArrayList<>(8);

    @Override
    public String[] getAlias() {
        return this.alias.toArray(new String[0]);
    }

    /**
     * 如果有必要，需要显式设置query表别名
     *
     * @param query
     */
    public JoinQuery(QL query) {
        this.assertQueryAlias(query);
        this.query = query;
        this.query.setSharedParameter(this.parameters);
        this.queryClass = (Class<QL>) query.getClass();
        this.wrapperData = new JoinWrapperData(this.query, this.queries, this.parameters);
        this.alias.add(this.query.tableAlias);
    }

    /**
     * 框架自动设置query的表别名
     *
     * @param queryClass
     * @param query
     */
    public JoinQuery(Class<QL> queryClass, QFunction<QL> query) {
        this.queryClass = queryClass;
        this.query = newQuery(queryClass, Parameters.alias());
        this.query.setSharedParameter(this.parameters);
        query.apply(this.query);
        this.wrapperData = new JoinWrapperData(this.query, this.queries, this.parameters);
        this.alias.add(this.query.tableAlias);
    }

    @Override
    public <QR extends BaseQuery<?, QR>> JoinOn<QL, QR, JoinBuilder2<QL>> join(Class<QR> clazz, QFunction<QR> query) {
        return join(JoinType.Join, clazz, query);
    }

    @Override
    public <QR extends BaseQuery<?, QR>> JoinOn<QL, QR, JoinBuilder2<QL>> leftJoin(Class<QR> clazz, QFunction<QR> query) {
        return join(JoinType.LeftJoin, clazz, query);
    }

    @Override
    public <QR extends BaseQuery<?, QR>> JoinOn<QL, QR, JoinBuilder2<QL>> rightJoin(Class<QR> clazz, QFunction<QR> query) {
        return join(JoinType.RightJoin, clazz, query);
    }

    @Override
    public <QR extends BaseQuery<?, QR>> JoinOn<QL, QR, JoinBuilder1<QL>> join(QR query) {
        return join(JoinType.Join, query);
    }

    @Override
    public <QR extends BaseQuery<?, QR>> JoinOn<QL, QR, JoinBuilder1<QL>> leftJoin(QR query) {
        return join(JoinType.LeftJoin, query);
    }

    @Override
    public <QR extends BaseQuery<?, QR>> JoinOn<QL, QR, JoinBuilder1<QL>> rightJoin(QR query) {
        return join(JoinType.RightJoin, query);
    }

    private <QR extends BaseQuery<?, QR>> JoinOn<QL, QR, JoinBuilder1<QL>> join(
        JoinType joinType, QR query) {
        this.assertQueryAlias(query);
        query.setSharedParameter(this.query);

        this.queries.add(query);
        this.alias.add(query.tableAlias);
        return new JoinOn<>(this, this.queryClass, this.query, joinType, (Class<QR>) query.getClass(), query);
    }

    /**
     * 判断query查询表别名已经设置
     *
     * @param query
     * @param <QR>
     */
    private <QR extends BaseQuery<?, QR>> void assertQueryAlias(QR query) {
        MybatisUtil.assertNotNull("query", query);
        if (BaseWrapperHelper.isBlankAlias(query)) {
            String err = String.format("the alias in the join query table must be set, " +
                "please use constructor: new %s(String alias, Parameters parameters)", query.getClass().getSimpleName());
            throw new RuntimeException(err);
        }
    }

    private <QR extends BaseQuery<?, QR>> JoinOn<QL, QR, JoinBuilder2<QL>> join(
        JoinType joinType, Class<QR> queryClass, QFunction<QR> apply
    ) {
        QR query = newQuery(queryClass, Parameters.alias());
        query.setSharedParameter(this.parameters);
        this.queries.add(query);
        apply.apply(query);
        this.alias.add(query.tableAlias);
        return new JoinOn<>(this, this.queryClass, this.query, joinType, queryClass, query);
    }

    @Override
    public JoinBuilder<QL> select(String... columns) {
        for (String column : columns) {
            this.wrapperData.addSelectColumn(column);
        }
        return this;
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
    public IQuery build() {
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

    private static <Q extends BaseQuery<?, Q>> Q newQuery(Class<Q> queryClass, String alias) {
        try {
            if (!QueryAliasConstructors.containsKey(queryClass)) {
                QueryAliasConstructors.put(queryClass, queryClass.getConstructor(String.class));
            }
            return (Q) QueryAliasConstructors.get(queryClass).newInstance(alias);
        } catch (Exception e) {
            throw new RuntimeException(String.format("new %s(String, ParameterPair) error: %s",
                queryClass.getSimpleName(), e.getMessage()), e);
        }
    }
}