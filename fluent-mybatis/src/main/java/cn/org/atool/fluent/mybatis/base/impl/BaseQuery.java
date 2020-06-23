package cn.org.atool.fluent.mybatis.base.impl;

import cn.org.atool.fluent.mybatis.base.model.FieldMeta;
import cn.org.atool.fluent.mybatis.segment.model.PagedOffset;
import cn.org.atool.fluent.mybatis.segment.BaseWrapper;
import cn.org.atool.fluent.mybatis.segment.model.ParameterPair;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.IQuery;

import java.util.stream.Stream;

import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.isNotEmpty;

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
    implements IQuery<E, Q> {
    private static final long serialVersionUID = 5541270981919538655L;

    protected BaseQuery(String table, Class entityClass, Class queryClass) {
        super(table, entityClass, queryClass);
    }

    /**
     * 非对外公开的构造方法,只用于生产嵌套 sql
     */
    protected BaseQuery(String table, ParameterPair parameters, Class entityClass, Class queryClass) {
        super(table, parameters, entityClass, queryClass);
    }

    @Override
    public Q distinct() {
        this.wrapperData.setDistinct(true);
        return (Q) this;
    }

    @Override
    public Q select(String... columns) {
        Stream.of(columns).filter(s -> isNotEmpty(s)).forEach(this.wrapperData::addSelectColumn);
        return (Q) this;
    }

    @Override
    public Q select(FieldMeta... fields) {
        Stream.of(fields).filter(field -> field != null).map(field -> field.column).forEach(this.wrapperData::addSelectColumn);
        return (Q) this;
    }

    @Override
    public Q limit(int limit) {
        this.wrapperData.setPaged(new PagedOffset(0, limit));
        return (Q) this;
    }

    @Override
    public Q limit(int from, int limit) {
        this.wrapperData.setPaged(new PagedOffset(from, limit));
        return (Q) this;
    }
}