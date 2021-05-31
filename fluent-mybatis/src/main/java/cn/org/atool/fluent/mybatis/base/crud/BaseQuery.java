package cn.org.atool.fluent.mybatis.base.crud;

import cn.org.atool.fluent.mybatis.If;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;
import cn.org.atool.fluent.mybatis.segment.BaseWrapper;
import cn.org.atool.fluent.mybatis.segment.model.PagedOffset;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static cn.org.atool.fluent.mybatis.base.model.FieldMapping.alias;
import static cn.org.atool.fluent.mybatis.mapper.StrConstant.*;

/**
 * AbstractQueryWrapper
 *
 * @param <E> 对应的实体类
 * @param <Q> 查询器
 * @author darui.wu
 * @date 2020/6/17 3:13 下午
 */
public abstract class BaseQuery<
    E extends IEntity,
    Q extends BaseQuery<E, Q>
    >
    extends BaseWrapper<E, Q, Q>
    implements IBaseQuery<E, Q> {

    protected BaseQuery(Supplier<String> table, String alias, Class entityClass, Class queryClass) {
        super(table, alias, entityClass, queryClass);
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
        if (this.primary() == null) {
            throw new FluentMybatisException("The primary key of in table[" + this.wrapperData.getTable() + "] was not found.");
        } else {
            return this.select(alias(this.tableAlias, this.primary()));
        }
    }

    /**
     * 查询指定字段
     *
     * @param columns 字段列表
     * @return Query
     */
    public Q select(String... columns) {
        if (If.notEmpty(columns)) {
            Stream.of(columns).filter(If::notBlank)
                .forEach(this.wrapperData::addSelectColumn);
        }
        return (Q) this;
    }

    @Override
    public Q limit(int limit) {
        return this.limit(0, limit);
    }

    @Override
    public Q limit(int from, int limit) {
        if (setUnion) {
            throw new RuntimeException("Limit syntax is not supported for union queries.");
        }
        this.wrapperData.setPaged(new PagedOffset(from, limit));
        return (Q) this;
    }

    @Override
    public Q last(String lastSql) {
        this.wrapperData.last(lastSql);
        return (Q) this;
    }

    @Override
    public abstract List<String> allFields();

    /**
     * select * from a where...
     * UNION
     * select * from b where...
     *
     * @param queries
     * @return
     */
    public Q union(IBaseQuery... queries) {
        return this.union(UNION, queries);
    }

    /**
     * select * from a where...
     * UNION ALL
     * select * from b where...
     *
     * @param queries
     * @return
     */
    public Q unionAll(IBaseQuery... queries) {
        return this.union(UNION_ALL, queries);
    }

    private boolean setUnion = false;

    private Q union(String key, IBaseQuery... queries) {
        if (this.wrapperData.getPaged() != null) {
            throw new RuntimeException("Limit syntax is not supported for union queries.");
        }
        if (queries == null || queries.length == 0) {
            throw new IllegalArgumentException("The size of parameter[queries] should be greater than zero.");
        }
        this.setUnion = true;
        for (IBaseQuery query : queries) {
            String sql = query.getWrapperData().getQuerySql();
            this.last(SPACE + key + SPACE + sql);
            query.getWrapperData().setSharedParameter(this.wrapperData);
        }
        return (Q) this;
    }
}