package cn.org.atool.fluent.mybatis.functions;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.FormSetter;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.model.Form;
import cn.org.atool.fluent.mybatis.model.IFormApply;
import cn.org.atool.fluent.mybatis.utility.FormHelper;

import java.util.Map;
import java.util.function.Function;

/**
 * FormApply
 *
 * @param <S>
 * @author darui.wu
 */
public final class FormApply<E extends IEntity, S extends FormSetter> implements IFormApply<E, S> {
    /**
     * IDE智能提示对象
     */
    private S setter;
    /**
     * 源数据(可以是任何数据)
     */
    private Map map;
    /**
     * 要设置的表单
     */
    private Form form;

    public FormApply(Function<FormApply, FormSetter> setterApply, Map map, Form form) {
        this.setter = (S) setterApply.apply(this);
        this.map = map;
        this.form = form == null ? new Form() : form;
    }

    public FormApply(S setter, Map map, Form form) {
        this.setter = setter;
        this.map = map;
        this.form = form == null ? new Form() : form;
    }

    private String op;

    @Override
    public S op(String op) {
        this.op = op;
        return setter;
    }

    public void set(FieldMapping field) {
        this.form.add.item(field.name, op, map.get(field.name));
    }

    @Override
    public IQuery<E> query() {
        return FormHelper.toQuery(this.setter.entityClass(), this.form);
    }
}