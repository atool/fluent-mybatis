package cn.org.atool.fluent.mybatis.functions;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.FormSetter;
import cn.org.atool.fluent.mybatis.model.IFormQuery;

import java.util.function.Function;

@FunctionalInterface
public interface FormApply<E extends IEntity, S extends FormSetter<E, S>>
    extends Function<E, IFormQuery<E, S>> {
}
