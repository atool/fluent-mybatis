package cn.org.atool.fluent.mybatis.functions;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.BaseFormSetter;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.model.Form;
import cn.org.atool.fluent.mybatis.model.IFormApply;
import cn.org.atool.fluent.mybatis.utility.FormHelper;
import cn.org.atool.fluent.mybatis.utility.LambdaUtil;
import lombok.Getter;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;

import static cn.org.atool.fluent.mybatis.base.model.SqlOpStr.*;

/**
 * FormApply
 *
 * @param <S>
 * @author darui.wu
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public final class FormApply<E extends IEntity, S extends BaseFormSetter> implements IFormApply<E, S> {
    /**
     * IDE智能提示对象
     */
    private final S setter;
    /**
     * 源数据(可以是任何数据)
     */
    private final Map map;
    /**
     * 要设置的表单
     */
    @Getter
    private final Form form;

    public FormApply(Function<FormApply, BaseFormSetter> setterApply, Map map, Form form) {
        this.setter = (S) setterApply.apply(this);
        this.map = map;
        this.form = form == null ? new Form() : form;
    }

    public FormApply(S setter, Map map, Form form) {
        this.setter = setter;
        this.map = map == null ? Collections.emptyMap() : map;
        this.form = form == null ? new Form() : form;
    }

    private String op;

    @Override
    public S op(String op) {
        this.op = op;
        return setter;
    }

    private FormApply<E, S> setFieldMapping(String op, IGetter<E> getter) {
        this.op(op);
        String field = LambdaUtil.resolve(getter);
        IMapping mapping = this.setter._mapping();
        FieldMapping f = mapping.getFieldsMap().get(field);
        if (f == null) {
            throw new RuntimeException("The property[" + field + "] of entity[" + mapping.entityClass().getSimpleName() + "] not found.");
        }
        this.set(f);
        return this;
    }

    public IFormApply<E, S> eq(IGetter<E> getter) {
        return this.setFieldMapping(OP_EQ, getter);
    }

    @Override
    public IFormApply<E, S> ne(IGetter<E> getter) {
        return this.setFieldMapping(OP_NE, getter);
    }

    @Override
    public IFormApply<E, S> gt(IGetter<E> getter) {
        return this.setFieldMapping(OP_GT, getter);
    }

    @Override
    public IFormApply<E, S> ge(IGetter<E> getter) {
        return this.setFieldMapping(OP_GE, getter);
    }

    @Override
    public IFormApply<E, S> lt(IGetter<E> getter) {
        return this.setFieldMapping(OP_LT, getter);
    }

    @Override
    public IFormApply<E, S> le(IGetter<E> getter) {
        return this.setFieldMapping(OP_LE, getter);
    }

    @Override
    public IFormApply<E, S> like(IGetter<E> getter) {
        return this.setFieldMapping(OP_LIKE, getter);
    }

    @Override
    public IFormApply<E, S> likeLeft(IGetter<E> getter) {
        return this.setFieldMapping(OP_LEFT_LIKE, getter);
    }

    @Override
    public IFormApply<E, S> likeRight(IGetter<E> getter) {
        return this.setFieldMapping(OP_RIGHT_LIKE, getter);
    }

    public void set(FieldMapping field) {
        this.form.add.item(field.name, op, map.get(field.name));
    }

    @Override
    public IQuery<E> query() {
        return FormHelper.toQuery(this.setter.entityClass(), this.form);
    }
}