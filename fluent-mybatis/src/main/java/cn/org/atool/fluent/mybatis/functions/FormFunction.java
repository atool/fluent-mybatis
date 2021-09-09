package cn.org.atool.fluent.mybatis.functions;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.BaseFormSetter;
import cn.org.atool.fluent.mybatis.model.Form;
import cn.org.atool.fluent.mybatis.model.IFormApply;

import java.util.function.BiFunction;

@FunctionalInterface
public interface FormFunction<E extends IEntity, S extends BaseFormSetter>
    extends BiFunction<Object, Form, IFormApply<E, S>> {
    /**
     * 按照entity来定义条件值
     *
     * @param entity entity
     * @return entity条件设置器
     */
    default IFormApply<E, S> with(Object entity) {
        return this.apply(entity, new Form());
    }
}