package cn.org.atool.fluent.mybatis.model;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.IRefs;
import cn.org.atool.fluent.mybatis.base.crud.FormSetter;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.model.SqlOp;
import cn.org.atool.fluent.mybatis.segment.WhereBase;
import cn.org.atool.fluent.mybatis.segment.model.WrapperData;
import lombok.NonNull;

import java.util.Map;
import java.util.function.Function;

/**
 * 通用的Form形式查询
 *
 * @param <E>
 * @param <S>
 * @author darui.wu
 */
public class FormQuery<E extends IEntity, S extends FormSetter<E, S>> implements IFormQuery<E, S> {
    private final Class<? extends IEntity> entityClazz;
    private final Map<String, Object> form;
    private final IQuery<E, ?> query;
    private final FormSetter<E, S> setter;

    public FormQuery(@NonNull E entity, @NonNull IQuery<E, ?> query, @NonNull Function<FormQuery, FormSetter> setter) {
        this.entityClazz = entity.getClass();
        this.form = entity.toEntityMap();
        this.query = query;
        this.setter = setter.apply(this);
    }

    public FormQuery(@NonNull E entity, @NonNull IQuery<E, ?> query, @NonNull Class<S> setter) {
        this.entityClazz = entity.getClass();
        this.form = entity.toEntityMap();
        this.query = query;
        this.setter = IRefs.instance().newFormSetter(setter, this);
    }

    public FormQuery(@NonNull Class<E> entityClass, @NonNull IQuery<E, ?> query, @NonNull Map form, @NonNull Class<S> setter) {
        this.entityClazz = entityClass;
        this.form = form;
        this.query = query;
        this.setter = IRefs.instance().newFormSetter(setter, this);
    }

    @Override
    public Class<? extends IEntity> entityClass() {
        return this.entityClazz;
    }

    @Override
    public S op(String op) {
        this.setter.set(c -> this.query.where().apply(c.column, SqlOp.valueOf(op), form.get(c.name)));
        return (S) setter;
    }

    @Override
    public IFormQuery<E, S> distinct() {
        this.query.distinct();
        return this;
    }

    @Override
    public IFormQuery<E, S> selectAll() {
        this.query.selectAll();
        return this;
    }

    @Override
    public IFormQuery<E, S> selectId() {
        this.query.selectId();
        return this;
    }

    @Override
    public IFormQuery<E, S> limit(int limit) {
        this.query.limit(limit);
        return this;
    }

    @Override
    public IFormQuery<E, S> limit(int start, int limit) {
        this.query.limit(start, limit);
        return this;
    }

    @Override
    public IFormQuery<E, S> last(String lastSql) {
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