package cn.org.atool.fluent.form.setter;

import cn.org.atool.fluent.form.Form;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.base.model.SqlOpStr;
import cn.org.atool.fluent.mybatis.functions.IGetter;
import cn.org.atool.fluent.mybatis.utility.LambdaUtil;
import lombok.Getter;

import java.util.Collections;
import java.util.Map;

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
    @Getter
    private final S setter;
    /**
     * 源数据(可以是任何数据)
     */
    private Map map;
    /**
     * 要设置的表单
     */
    @Getter
    private Form form;

    public FormApply(S setter, Map map, Form form) {
        this.setter = setter;
        this.init(form, map);
    }

    private void init(Form form, Map map) {
        this.map = map == null ? Collections.emptyMap() : map;
        this.form = form == null ? new Form() : form;
        this.form.setEntityClass(this.setter.entityClass());
    }

    private String op;

    @Override
    public FormApply<E, S> and(IGetter<E> getter) {
        String field = LambdaUtil.resolve(getter);
        return this.and(field);
    }

    @Override
    public FormApply<E, S> and(String field) {
        this.form.getUpdate().put(field, map.get(field));
        return this;
    }

    private S op(String op) {
        this.op = op;
        return setter;
    }

    private FormApply<E, S> setFieldMapping(String op, IGetter<E> getter) {
        String field = LambdaUtil.resolve(getter);
        return this.setFieldMapping(op, field);
    }

    private FormApply<E, S> setFieldMapping(String op, String field) {
        this.op(op);
        IMapping mapping = this.setter._mapping();
        FieldMapping f = mapping.getFieldsMap().get(field);
        if (f == null) {
            throw new RuntimeException("The property[" + field + "] of entity[" + mapping.entityClass().getSimpleName() + "] not found.");
        }
        this.addWhere(f);
        return this;
    }

    @Override
    public IFormApply<E, S> eq(IGetter<E> getter) {
        return this.setFieldMapping(SqlOpStr.OP_EQ, getter);
    }

    @Override
    public IFormApply<E, S> eq(String field) {
        return this.setFieldMapping(SqlOpStr.OP_EQ, field);
    }

    @Override
    public IFormApply<E, S> ne(IGetter<E> getter) {
        return this.setFieldMapping(SqlOpStr.OP_NE, getter);
    }

    @Override
    public IFormApply<E, S> ne(String field) {
        return this.setFieldMapping(SqlOpStr.OP_NE, field);
    }

    @Override
    public IFormApply<E, S> gt(IGetter<E> getter) {
        return this.setFieldMapping(SqlOpStr.OP_GT, getter);
    }

    @Override
    public IFormApply<E, S> gt(String field) {
        return this.setFieldMapping(SqlOpStr.OP_GT, field);
    }

    @Override
    public IFormApply<E, S> ge(IGetter<E> getter) {
        return this.setFieldMapping(SqlOpStr.OP_GE, getter);
    }

    @Override
    public IFormApply<E, S> ge(String field) {
        return this.setFieldMapping(SqlOpStr.OP_GE, field);
    }

    @Override
    public IFormApply<E, S> lt(IGetter<E> getter) {
        return this.setFieldMapping(SqlOpStr.OP_LT, getter);
    }

    @Override
    public IFormApply<E, S> lt(String field) {
        return this.setFieldMapping(SqlOpStr.OP_LT, field);
    }

    @Override
    public IFormApply<E, S> le(IGetter<E> getter) {
        return this.setFieldMapping(SqlOpStr.OP_LE, getter);
    }

    @Override
    public IFormApply<E, S> le(String field) {
        return this.setFieldMapping(SqlOpStr.OP_LE, field);
    }

    @Override
    public IFormApply<E, S> like(IGetter<E> getter) {
        return this.setFieldMapping(SqlOpStr.OP_LIKE, getter);
    }

    @Override
    public IFormApply<E, S> like(String field) {
        return this.setFieldMapping(SqlOpStr.OP_LIKE, field);
    }

    @Override
    public IFormApply<E, S> likeLeft(IGetter<E> getter) {
        return this.setFieldMapping(SqlOpStr.OP_LIKE_LEFT, getter);
    }

    @Override
    public IFormApply<E, S> likeLeft(String field) {
        return this.setFieldMapping(SqlOpStr.OP_LIKE_LEFT, field);
    }

    @Override
    public IFormApply<E, S> likeRight(IGetter<E> getter) {
        return this.setFieldMapping(SqlOpStr.OP_LIKE_RIGHT, getter);
    }

    @Override
    public IFormApply<E, S> likeRight(String field) {
        return this.setFieldMapping(SqlOpStr.OP_LIKE_RIGHT, field);
    }

    public void addWhere(FieldMapping field) {
        this.form.and.where(field.name, op, map.get(field.name));
    }
}