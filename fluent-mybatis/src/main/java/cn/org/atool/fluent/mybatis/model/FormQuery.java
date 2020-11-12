package cn.org.atool.fluent.mybatis.model;

import cn.org.atool.fluent.mybatis.base.EntityRefs;
import cn.org.atool.fluent.mybatis.base.FormSetter;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.IQuery;
import cn.org.atool.fluent.mybatis.base.model.SqlOp;
import cn.org.atool.fluent.mybatis.segment.WhereBase;
import cn.org.atool.fluent.mybatis.segment.model.WrapperData;

import java.util.Map;

/**
 * 通用的Form形式查询
 *
 * @param <E>
 * @param <C>
 * @author darui.wu
 */
public class FormQuery<E extends IEntity, C extends FormSetter<IFormQuery<E, C>>> implements IFormQuery<E, C> {
    private final E entity;
    private final Map<String, Object> map;
    private final IQuery<E, ?> query;
    private final FormSetter<IFormQuery<E, C>> setter;

    public FormQuery(E entity, IQuery<E, ?> query, Class<C> setter) {
        this.entity = entity;
        this.map = this.entity.toEntityMap();
        this.query = query;
        this.setter = EntityRefs.instance().newFormSetter(setter, this);
    }

    @Override
    public C op(String op) {
        this.setter.set(c -> this.query.where().apply(c.column, SqlOp.valueOf(op), map.get(c.name)));
        return (C) setter;
    }

    @Override
    public IFormQuery<E, C> distinct() {
        this.query.distinct();
        return this;
    }

    @Override
    public IFormQuery<E, C> selectAll() {
        this.query.selectAll();
        return this;
    }

    @Override
    public IFormQuery<E, C> selectId() {
        this.query.selectId();
        return this;
    }

    @Override
    public IFormQuery<E, C> limit(int limit) {
        this.query.limit(limit);
        return this;
    }

    @Override
    public IFormQuery<E, C> limit(int start, int limit) {
        this.query.limit(start, limit);
        return this;
    }

    @Override
    public IFormQuery<E, C> last(String lastSql) {
        this.query.last(lastSql);
        return this;
    }

    @Override
    public WhereBase where() {
        return this.query.where();
    }

    @Override
    public WrapperData getWrapperData() {
        return this.query.getWrapperData();
    }
}