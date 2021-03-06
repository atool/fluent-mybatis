package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.IRefs;
import cn.org.atool.fluent.mybatis.base.crud.*;
import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.fluent.mybatis.metadata.JoinType;
import cn.org.atool.fluent.mybatis.segment.model.PagedOffset;
import cn.org.atool.fluent.mybatis.segment.model.Parameters;
import cn.org.atool.fluent.mybatis.utility.MybatisUtil;
import lombok.Setter;
import lombok.experimental.Accessors;

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
@SuppressWarnings({"rawtypes", "unused", "unchecked"})
@Accessors(chain = true)
public class JoinQuery<QL extends BaseQuery<?, QL>>
    extends BaseWrapper<IEntity, JoinQuery<QL>, JoinQuery<QL>>
    implements IBaseQuery<IEntity, JoinQuery<QL>>, JoinBuilder1<QL> {
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

    /**
     * 别名列表
     */
    private final List<String> alias = new ArrayList<>(8);

    @Override
    public String[] getAlias() {
        return this.alias.toArray(new String[0]);
    }

    /**
     * 如果有必要，需要显式设置query表别名
     *
     * @param query 左查询
     */
    public JoinQuery(QL query) {
        super(null);
        this.assertQueryAlias(query);
        this.query = query;
        Parameters parameters = new Parameters();
        this.query.sharedParameter(parameters);
        this.queryClass = (Class<QL>) query.getClass();
        super.wrapperData = new JoinWrapperData(this.query, this.queries, parameters);
        this.alias.add(this.query.tableAlias);
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
        query.sharedParameter(this.query);

        this.queries.add(query);
        this.alias.add(query.tableAlias);
        return new JoinOn<>(this, this.queryClass, this.query, joinType, (Class<QR>) query.getClass(), query);
    }

    /**
     * 判断query查询表别名已经设置
     *
     * @param query 右查询
     * @param <QR>  右查询类型
     */
    private <QR extends BaseQuery<?, QR>> void assertQueryAlias(QR query) {
        MybatisUtil.assertNotNull("query", query);
        if (BaseWrapperHelper.isBlankAlias(query)) {
            String err = String.format("the table alias of join query must be set, " +
                "please use constructor: new %s(String alias)", query.getClass().getSimpleName());
            throw new RuntimeException(err);
        }
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
    public JoinQuery<QL> build() {
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

    /**
     * 查询条件 where ...
     */
    public final JoinQueryWhere where = new JoinQueryWhere(this);

    @Override
    public JoinQueryWhere where() {
        return this.where;
    }

    private static final Map<Class, Constructor> QueryAliasConstructors = new HashMap<>(128);

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

    @Override
    public JoinWrapperData getWrapperData() {
        return (JoinWrapperData) super.wrapperData;
    }

    @Override
    protected List<String> allFields() {
        List<String> all = new ArrayList<>();
        for (BaseQuery query : this.queries) {
            all.addAll(query.allFields());
        }
        return all;
    }

    @Setter
    private DbType dbType;

    @Override
    public DbType dbType() {
        if (dbType != null) {
            return dbType;
        }
        dbType = this.query.dbType();
        if (dbType != null) {
            return dbType;
        }
        for (IQuery query : this.queries) {
            dbType = ((BaseWrapper) query).dbType();
            if (dbType != null) {
                return dbType;
            }
        }
        dbType = IRefs.instance().defaultDbType();
        return dbType;
    }
}