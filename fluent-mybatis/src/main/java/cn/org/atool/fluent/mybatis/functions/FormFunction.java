package cn.org.atool.fluent.mybatis.functions;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.BaseFormSetter;
import cn.org.atool.fluent.mybatis.model.form.Form;
import cn.org.atool.fluent.mybatis.model.form.FormApply;
import cn.org.atool.fluent.mybatis.model.form.FormKit;
import cn.org.atool.fluent.mybatis.model.form.IFormApply;

import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * FormFunction: 表单初始化函数
 *
 * @author darui.wu
 */
@SuppressWarnings({"unchecked", "rawtypes"})
@FunctionalInterface
public interface FormFunction<E extends IEntity, S extends BaseFormSetter>
    extends BiFunction<Object, Form, IFormApply<E, S>> {
    /**
     * 按照entity来定义条件值
     *
     * @param entity entity
     * @return entity条件设置器
     */
    default Form with(Object entity, Consumer<IFormApply<E, S>> apply) {
        Form form = FormKit.newForm();
        FormApply formApply = (FormApply) this.apply(entity, form);
        form.setEntityClass(formApply.getSetter().entityClass());
        apply.accept(formApply);
        return form;
    }
}