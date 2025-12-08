package cn.org.atool.fluent.mybatis.base.crud;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.model.UniqueType;
import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;
import cn.org.atool.fluent.mybatis.functions.IGetter;
import cn.org.atool.fluent.mybatis.functions.StringSupplier;
import cn.org.atool.fluent.mybatis.metadata.JoinType;
import cn.org.atool.fluent.mybatis.segment.BaseWrapper;
import cn.org.atool.fluent.mybatis.segment.JoinOn;
import cn.org.atool.fluent.mybatis.segment.fragment.Column;
import cn.org.atool.fluent.mybatis.segment.fragment.Fragments;
import cn.org.atool.fluent.mybatis.segment.fragment.IFragment;
import cn.org.atool.fluent.mybatis.segment.model.PagedOffset;
import cn.org.atool.fluent.mybatis.utility.LambdaUtil;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

import static cn.org.atool.fluent.mybatis.base.model.FieldMapping.alias;
import static cn.org.atool.fluent.mybatis.utility.StrConstant.UNION;
import static cn.org.atool.fluent.mybatis.utility.StrConstant.UNION_ALL;

/**
 * AbstractQueryWrapper
 *
 * @param <E> 对应的实体类
 * @param <Q> 查询器
 * @author darui.wu 2020/6/17 3:13 下午
 */
@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
public abstract class BaseQuery<E extends IEntity, Q extends BaseQuery<E, Q>>
        extends BaseWrapper<E, Q, Q>
        implements IBaseQuery<E, Q> {

    protected BaseQuery(StringSupplier table, StringSupplier alias, Class entityClass) {
        super(Fragments.fragment(table), alias, entityClass);
    }

    protected BaseQuery(IFragment table, StringSupplier alias, Class entityClass) {
        super(table, alias, entityClass);
    }

    @Override
    public Q distinct() {
        this.data.setDistinct(true);
        return (Q) this;
    }

    /**
     * 显式指定查询所有字段, 在join查询中有用
     *
     * @return Query
     */
    @Override
    public Q selectAll() {
        this.allFields().stream().map(c -> alias(this.getTableAlias(), c)).forEach(this::select);
        return (Q) this;
    }

    @Override
    public Q selectId() {
        String primary = this.fieldName(UniqueType.PRIMARY_ID);
        if (primary == null) {
            throw new FluentMybatisException("The primary key of in table was not found.");
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
            this.data.select(Column.set(this, column));
        }
        return (Q) this;
    }

    @Override
    public Q limit(int limit) {
        return this.limit(0, limit);
    }

    @Override
    public Q limit(int from, int limit) {
        this.data.setPaged(new PagedOffset(from, limit));
        return (Q) this;
    }

    @Override
    public Q last(String lastSql) {
        this.data.last(lastSql);
        return (Q) this;
    }

    @Override
    public IQuery union(IQuery... queries) {
        return this.union(UNION, queries);
    }

    @Override
    public IQuery unionAll(IQuery... queries) {
        return this.union(UNION_ALL, queries);
    }

    /**
     * <pre>
     *  构造JoinBuild&lt;左查询,右查询&gt;
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
     *  构造JoinBuild&lt;左查询,右查询&gt;
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

    @Getter
    private final Set<String> withRelations = new HashSet<>();

    /**
     * listEntity和findOne查询时, 同时执行 refMethod 方法
     *
     * @param refMethod 关联查询方法
     * @return ignore
     */
    public <ET> Q with(IGetter<ET> refMethod) {
        return this.with(LambdaUtil.resolve(refMethod));
    }

    /**
     * listEntity和findOne查询时, 同时执行 refMethod 方法
     *
     * @param refMethod 关联查询方法
     * @return ignore
     */
    public Q with(String refMethod) {
        this.mapping().ifPresent(m -> {
            if (m.refKeys().containsKey(refMethod)) {
                this.withRelations.add(refMethod);
            }
        });
        return (Q) this;
    }
}