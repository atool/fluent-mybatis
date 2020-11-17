package cn.org.atool.fluent.mybatis.functions;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.FormSetter;
import cn.org.atool.fluent.mybatis.model.IFormQuery;

import java.util.Map;
import java.util.function.Function;

/**
 * FormApply
 *
 * @param <E>
 * @param <S>
 * @author darui.wu
 */
public final class FormApply<E extends IEntity, S extends FormSetter<E, S>> {
    private final Function<IEntity, IFormQuery> byEntity;

    private final Function<Map, IFormQuery> byMap;

    public FormApply(Function<IEntity, IFormQuery> byEntity, Function<Map, IFormQuery> byMap) {
        this.byEntity = byEntity;
        this.byMap = byMap;
    }

    public IFormQuery<E, S> byEntity(E entity) {
        return byEntity.apply(entity);
    }

    public IFormQuery<E, S> byMap(Map map) {
        return byMap.apply(map);
    }
}