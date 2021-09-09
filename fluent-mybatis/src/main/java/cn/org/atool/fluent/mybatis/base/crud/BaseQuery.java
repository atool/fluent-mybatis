package cn.org.atool.fluent.mybatis.base.crud;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.model.Column;
import cn.org.atool.fluent.mybatis.base.model.UniqueFieldType;
import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;
import cn.org.atool.fluent.mybatis.metadata.JoinType;
import cn.org.atool.fluent.mybatis.segment.BaseWrapper;
import cn.org.atool.fluent.mybatis.segment.JoinOn;
import cn.org.atool.fluent.mybatis.segment.model.PagedOffset;

import java.util.function.Supplier;

import static cn.org.atool.fluent.mybatis.base.model.FieldMapping.alias;
import static cn.org.atool.fluent.mybatis.mapper.StrConstant.UNION;
import static cn.org.atool.fluent.mybatis.mapper.StrConstant.UNION_ALL;

/**
 * AbstractQueryWrapper
 *
 * @param <E> 对应的实体类
 * @param <Q> 查询器
 * @author darui.wu 2020/6/17 3:13 下午
 */
@SuppressWarnings({"unchecked", "rawtypes", "unused"})
public abstract class BaseQuery<
    E extends IEntity,
    Q extends BaseQuery<E, Q>
    >
    extends BaseWrapper<E, Q, Q>
    implements IBaseQuery<E, Q> {

    protected BaseQuery(Supplier<String> table, String alias, Class entityClass) {
        super(table, alias, entityClass);
    }

    @Override
    public Q distinct() {
        this.wrapperData.setDistinct(true);
        return (Q) this;
    }

    /**
     * 显式指定查询所有字段, 在join查询中有用
     *
     * @return Query
     */
    @Override
    public Q selectAll() {
        this.allFields().stream().map(c -> alias(this.tableAlias, c)).forEach(this::select);
        return (Q) this;
    }

    @Override
    public Q selectId() {
        String primary = this.fieldName(UniqueFieldType.PRIMARY_ID);
        if (primary == null) {
            throw new FluentMybatisException("The primary key of in table[" + this.wrapperData.getTable() + "] was not found.");
        } else {
            return this.select(primary);
        }
    }

    /**
     * 查询指定字段
     *
     * @param columns 字段列表
     * @return Query
     */
    public Q select(String... columns) {
        if (columns == null) {
            return (Q) this;
        }
        for (String column : columns) {
            Column _column = Column.column(column, this);
            this.wrapperData.addSelectColumn(_column.wrapColumn());
        }
        return (Q) this;
    }

    @Override
    public Q limit(int limit) {
        return this.limit(0, limit);
    }

    @Override
    public Q limit(int from, int limit) {
        this.wrapperData.setPaged(new PagedOffset(from, limit));
        return (Q) this;
    }

    @Override
    public Q last(String lastSql) {
        this.wrapperData.last(lastSql);
        return (Q) this;
    }

    /**
     * select * from a where...
     * UNION
     * select * from b where...
     *
     * @param queries 查询条件列表
     * @return ignore
     */
    public Q union(IBaseQuery... queries) {
        return this.union(UNION, queries);
    }

    /**
     * select * from a where...
     * UNION ALL
     * select * from b where...
     *
     * @param queries 查询条件列表
     * @return ignore
     */
    public Q unionAll(IBaseQuery... queries) {
        return this.union(UNION_ALL, queries);
    }

    private Q union(String key, IBaseQuery... queries) {
        if (this.wrapperData.getPaged() != null) {
            throw new RuntimeException("Limit syntax is not supported for union queries.");
        }
        if (queries == null || queries.length == 0) {
            throw new IllegalArgumentException("The size of parameter[queries] should be greater than zero.");
        }
        for (IBaseQuery query : queries) {
            this.wrapperData.union(key, query);
            query.getWrapperData().sharedParameter(this.wrapperData);
        }
        return (Q) this;
    }

    /**
     * <pre>
     *  构造JoinBuild<左查询,右查询>
     * </pre>
     *
     * @param query 右查询
     * @param <QR>  右查询类型
     * @return JoinOn
     */
    public <QR extends BaseQuery<?, QR>> JoinOn<Q, QR, JoinToBuilder<Q>> join(QR query) {
        return JoinBuilder.from((Q) this).join(query);
    }

    /**
     * <pre>
     *  构造JoinBuild<左查询,右查询>
     * </pre>
     *
     * @param joinType 连接类型
     * @param query    右查询
     * @param <QR>     右查询类型
     * @return JoinOn
     */
    public <QR extends BaseQuery<?, QR>> JoinOn<Q, QR, JoinToBuilder<Q>> join(JoinType joinType, QR query) {
        switch (joinType) {
            case LeftJoin:
                return JoinBuilder.from((Q) this).leftJoin(query);
            case RightJoin:
                return JoinBuilder.from((Q) this).rightJoin(query);
            default:
                return JoinBuilder.from((Q) this).join(query);
        }
    }
}