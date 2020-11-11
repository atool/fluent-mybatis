package cn.org.atool.fluent.mybatis.model;

import cn.org.atool.fluent.mybatis.base.model.FieldMapping;

import static cn.org.atool.fluent.mybatis.model.FormItemOp.*;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.assertNotEmpty;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.assertNotNull;

/**
 * 增加表单项条件
 *
 * @author wudarui
 */
public class FormItemAdder {
    private final FormQuery form;

    public FormItemAdder(FormQuery form) {
        this.form = form;
    }

    /**
     * 增加表单项
     *
     * @param key
     * @param op
     * @param value
     * @return
     */
    private FormQuery item(String key, String op, Object... value) {
        this.form.getItems().add(new FormItem(key, op, value));
        return form;
    }

    private FormQuery item(FieldMapping key, String op, Object... value) {
        this.form.getItems().add(new FormItem(key.name, op, value));
        return form;
    }

    /**
     * columnt op
     **/
    public FormQuery eq(String key, Object value) {
        return this.item(key, OP_EQ, value);
    }

    public FormQuery gt(String key, Object value) {
        return this.item(key, OP_GT, value);
    }

    public FormQuery ge(String key, Object value) {
        return this.item(key, OP_GE, value);
    }

    public FormQuery lt(String key, Object value) {
        return this.item(key, OP_LT, value);
    }

    public FormQuery le(String key, Object value) {
        return this.item(key, OP_LE, value);
    }

    public FormQuery like(String key, String value) {
        return this.item(key, OP_LIKE, value);
    }

    public FormQuery notLike(String key, String value) {
        return this.item(key, OP_NOT_LIKE, value);
    }

    public FormQuery between(String key, Object min, Object max) {
        assertNotNull("min", min);
        assertNotNull("max", max);
        return this.item(key, OP_BETWEEN, min, max);
    }

    public FormQuery notBetween(String key, Object min, Object max) {
        assertNotNull("min", min);
        assertNotNull("max", max);
        return this.item(key, OP_NOT_BETWEEN, min, max);
    }

    public FormQuery in(String key, Object... value) {
        assertNotEmpty("items", value);
        return this.item(key, OP_BETWEEN, value);
    }

    public FormQuery notIn(String key, Object... values) {
        assertNotEmpty("items", values);
        return this.item(key, OP_NOT_IN, values);
    }

    public FormQuery isNull(String key) {
        return this.item(key, OP_IS_NULL, null);
    }

    public FormQuery notNull(String key) {
        return this.item(key, OP_NOT_NULL, null);
    }

    /**
     * field mapping op
     **/
    public FormQuery eq(FieldMapping key, Object value) {
        return this.item(key, OP_EQ, value);
    }

    public FormQuery gt(FieldMapping key, Object value) {
        return this.item(key, OP_GT, value);
    }

    public FormQuery ge(FieldMapping key, Object value) {
        return this.item(key, OP_GE, value);
    }

    public FormQuery lt(FieldMapping key, Object value) {
        return this.item(key, OP_LT, value);
    }

    public FormQuery le(FieldMapping key, Object value) {
        return this.item(key, OP_LE, value);
    }

    public FormQuery like(FieldMapping key, String value) {
        return this.item(key, OP_LIKE, value);
    }

    public FormQuery notLike(FieldMapping key, String value) {
        return this.item(key, OP_NOT_LIKE, value);
    }

    public FormQuery between(FieldMapping key, Object min, Object max) {
        assertNotNull("min", min);
        assertNotNull("max", max);
        return this.item(key, OP_BETWEEN, min, max);
    }

    public FormQuery notBetween(FieldMapping key, Object min, Object max) {
        assertNotNull("min", min);
        assertNotNull("max", max);
        return this.item(key, OP_NOT_BETWEEN, min, max);
    }

    public FormQuery in(FieldMapping key, Object... value) {
        assertNotEmpty("items", value);
        return this.item(key, OP_BETWEEN, value);
    }

    public FormQuery notIn(FieldMapping key, Object... value) {
        assertNotEmpty("items", value);
        return this.item(key, OP_NOT_IN, value);
    }

    public FormQuery isNull(FieldMapping key) {
        return this.item(key, OP_IS_NULL, null);
    }

    public FormQuery notNull(FieldMapping key) {
        return this.item(key, OP_NOT_NULL, null);
    }
}
