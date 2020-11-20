package cn.org.atool.fluent.mybatis.functions;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.FormSetter;
import cn.org.atool.fluent.mybatis.model.Form;
import cn.org.atool.fluent.mybatis.model.IFormApply;

import java.util.function.BiFunction;

@FunctionalInterface
public interface FormFunction<E extends IEntity, S extends FormSetter>
    extends BiFunction<Object, Form, IFormApply<E, S>> {

    default IFormApply<E, S> apply(Object object) {
        return this.apply(object, new Form());
    }
}