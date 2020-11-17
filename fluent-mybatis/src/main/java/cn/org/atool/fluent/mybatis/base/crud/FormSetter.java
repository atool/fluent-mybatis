package cn.org.atool.fluent.mybatis.base.crud;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.model.IFormQuery;

import java.util.function.Consumer;

/**
 * 字段设置
 *
 * @param <S>
 * @author darui.wu
 */
public abstract class FormSetter<E extends IEntity, S extends FormSetter<E, S>> {
    /**
     * IFormQuery
     */
    private IFormQuery<E, S> query;

    private Consumer<FieldMapping> apply;

    protected FormSetter() {
    }

    protected IFormQuery<E, S> setQuery(IFormQuery query) {
        this.query = query;
        return this.query;
    }

    public void set(Consumer<FieldMapping> apply) {
        this.apply = apply;
    }

    /**
     * 设置当前操作字段
     *
     * @param field
     * @return
     */
    public IFormQuery<E, S> set(FieldMapping field) {
        this.apply.accept(field);
        return this.query;
    }

    public abstract Class<? extends IEntity> entityClass();
}