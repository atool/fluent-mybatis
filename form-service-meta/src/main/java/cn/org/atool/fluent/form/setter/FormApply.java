package cn.org.atool.fluent.form.setter;

import cn.org.atool.fluent.form.Form;
import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.functions.IGetter;
import cn.org.atool.fluent.mybatis.utility.LambdaUtil;
import lombok.Getter;

import java.util.Collections;
import java.util.Map;

import static cn.org.atool.fluent.form.setter.FormSqlOp.*;

/**
 * FormApply
 *
 * @author darui.wu
 */
@SuppressWarnings({"rawtypes"})
public final class FormApply implements IFormApply {
    /**
     * IDE智能提示对象
     */
    @Getter
    private final FormSetter setter;
    /**
     * 源数据(可以是任何数据)
     */
    private final Map map;
    /**
     * 要设置的表单
     */
    @Getter
    private final Form form;

    public FormApply(FormSetter setter, Map map, Form form) {
        this.setter = setter;
        this.map = map == null ? Collections.emptyMap() : map;
        this.form = form == null ? new Form() : form;
        this.form.setEntityClass(this.setter.entityClass());
    }

    private String op;

    @Override
    public <E> FormApply and(IGetter<E> getter) {
        String field = LambdaUtil.resolve(getter);
        return this.and(field);
    }

    @Override
    public FormApply and(String field) {
        this.form.getUpdate().put(field, map.get(field));
        return this;
    }

    private <E> FormApply setFieldMapping(String op, IGetter<E> getter) {
        String field = LambdaUtil.resolve(getter);
        return this.setFieldMapping(op, field);
    }

    private FormApply setFieldMapping(String op, String field) {
        this.op = op;
        IMapping mapping = this.setter._mapping();
        FieldMapping f = mapping.getFieldsMap().get(field);
        if (f == null) {
            throw new RuntimeException("The property[" + field + "] of entity[" + mapping.entityClass().getSimpleName() + "] not found.");
        }
        this.addWhere(f);
        return this;
    }

    @Override
    public <E> FormApply eq(IGetter<E> getter) {
        return this.setFieldMapping(OP_EQ, getter);
    }

    @Override
    public FormApply eq(String field) {
        return this.setFieldMapping(OP_EQ, field);
    }

    @Override
    public <E> FormApply ne(IGetter<E> getter) {
        return this.setFieldMapping(OP_NE, getter);
    }

    @Override
    public FormApply ne(String field) {
        return this.setFieldMapping(OP_NE, field);
    }

    @Override
    public <E> FormApply gt(IGetter<E> getter) {
        return this.setFieldMapping(OP_GT, getter);
    }

    @Override
    public FormApply gt(String field) {
        return this.setFieldMapping(OP_GT, field);
    }

    @Override
    public <E> FormApply ge(IGetter<E> getter) {
        return this.setFieldMapping(OP_GE, getter);
    }

    @Override
    public FormApply ge(String field) {
        return this.setFieldMapping(OP_GE, field);
    }

    @Override
    public <E> FormApply lt(IGetter<E> getter) {
        return this.setFieldMapping(OP_LT, getter);
    }

    @Override
    public FormApply lt(String field) {
        return this.setFieldMapping(OP_LT, field);
    }

    @Override
    public <E> FormApply le(IGetter<E> getter) {
        return this.setFieldMapping(OP_LE, getter);
    }

    @Override
    public FormApply le(String field) {
        return this.setFieldMapping(OP_LE, field);
    }

    @Override
    public <E> FormApply like(IGetter<E> getter) {
        return this.setFieldMapping(OP_LIKE, getter);
    }

    @Override
    public FormApply like(String field) {
        return this.setFieldMapping(OP_LIKE, field);
    }

    @Override
    public <E> FormApply likeLeft(IGetter<E> getter) {
        return this.setFieldMapping(OP_LIKE_LEFT, getter);
    }

    @Override
    public FormApply likeLeft(String field) {
        return this.setFieldMapping(OP_LIKE_LEFT, field);
    }

    @Override
    public <E> FormApply likeRight(IGetter<E> getter) {
        return this.setFieldMapping(OP_LIKE_RIGHT, getter);
    }

    @Override
    public FormApply likeRight(String field) {
        return this.setFieldMapping(OP_LIKE_RIGHT, field);
    }

    public void addWhere(FieldMapping field) {
        this.form.and.where(field.name, op, map.get(field.name));
    }
}