package cn.org.atool.fluent.mybatis.functions;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.FormSetter;
import cn.org.atool.fluent.mybatis.model.IFormQuery;

import java.util.Map;
import java.util.function.Function;

/**
 * FormApply
 *
 * @param <S>
 * @author darui.wu
 */
public final class FormApply<S extends FormSetter<S>> {
    private final Function<IEntity, IFormQuery> byEntity;

    private final Function<Map, IFormQuery> byMap;

    public FormApply(Function<IEntity, IFormQuery> byEntity, Function<Map, IFormQuery> byMap) {
        this.byEntity = byEntity;
        this.byMap = byMap;
    }

    public <E extends IEntity> IFormQuery<S> by(E entity) {
        return byEntity.apply(entity);
    }

    public IFormQuery<S> by(Map map) {
        return byMap.apply(map);
    }
}