package cn.org.atool.fluent.mybatis.model;

import cn.org.atool.fluent.mybatis.base.model.FieldMapping;

import static cn.org.atool.fluent.mybatis.base.model.SqlOpStr.*;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.assertNotEmpty;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.assertNotNull;

/**
 * 增加表单项条件
 *
 * @author wudarui
 */
@SuppressWarnings({"unused"})
public class FormItemAdder {
    private final Form form;

    public FormItemAdder(Form form) {
        this.form = form;
    }

    /**
     * 增加表单项
     *
     * @param key   key
     * @param op    SqlOp操作符
     * @param value value
     * @return Form
     */
    public Form item(String key, String op, Object... value) {
        this.form.getItems().add(new FormItem(key, op, value));
        return form;
    }

    private Form item(FieldMapping key, String op, Object... value) {
        this.form.getItems().add(new FormItem(key.name, op, value));
        return form;
    }

    /**
     * columnt op
     **/
    public Form eq(String key, Object value) {
        return this.item(key, OP_EQ, value);
    }

    public Form gt(String key, Object value) {
        return this.item(key, OP_GT, value);
    }

    public Form ge(String key, Object value) {
        return this.item(key, OP_GE, value);
    }

    public Form lt(String key, Object value) {
        return this.item(key, OP_LT, value);
    }

    public Form le(String key, Object value) {
        return this.item(key, OP_LE, value);
    }

    public Form like(String key, String value) {
        return this.item(key, OP_LIKE, value);
    }

    public Form notLike(String key, String value) {
        return this.item(key, OP_NOT_LIKE, value);
    }

    public Form between(String key, Object min, Object max) {
        assertNotNull("min", min);
        assertNotNull("max", max);
        return this.item(key, OP_BETWEEN, min, max);
    }

    public Form notBetween(String key, Object min, Object max) {
        assertNotNull("min", min);
        assertNotNull("max", max);
        return this.item(key, OP_NOT_BETWEEN, min, max);
    }

    public Form in(String key, Object... value) {
        assertNotEmpty("items", value);
        return this.item(key, OP_BETWEEN, value);
    }

    public Form notIn(String key, Object... values) {
        assertNotEmpty("items", values);
        return this.item(key, OP_NOT_IN, values);
    }

    public Form isNull(String key) {
        return this.item(key, OP_IS_NULL);
    }

    public Form notNull(String key) {
        return this.item(key, OP_NOT_NULL);
    }

    /**
     * field mapping op
     **/
    public Form eq(FieldMapping key, Object value) {
        return this.item(key, OP_EQ, value);
    }

    public Form gt(FieldMapping key, Object value) {
        return this.item(key, OP_GT, value);
    }

    public Form ge(FieldMapping key, Object value) {
        return this.item(key, OP_GE, value);
    }

    public Form lt(FieldMapping key, Object value) {
        return this.item(key, OP_LT, value);
    }

    public Form le(FieldMapping key, Object value) {
        return this.item(key, OP_LE, value);
    }

    public Form like(FieldMapping key, String value) {
        return this.item(key, OP_LIKE, value);
    }

    public Form notLike(FieldMapping key, String value) {
        return this.item(key, OP_NOT_LIKE, value);
    }

    public Form between(FieldMapping key, Object min, Object max) {
        assertNotNull("min", min);
        assertNotNull("max", max);
        return this.item(key, OP_BETWEEN, min, max);
    }

    public Form notBetween(FieldMapping key, Object min, Object max) {
        assertNotNull("min", min);
        assertNotNull("max", max);
        return this.item(key, OP_NOT_BETWEEN, min, max);
    }

    public Form in(FieldMapping key, Object... value) {
        assertNotEmpty("items", value);
        return this.item(key, OP_BETWEEN, value);
    }

    public Form notIn(FieldMapping key, Object... value) {
        assertNotEmpty("items", value);
        return this.item(key, OP_NOT_IN, value);
    }

    public Form isNull(FieldMapping key) {
        return this.item(key, OP_IS_NULL);
    }

    public Form notNull(FieldMapping key) {
        return this.item(key, OP_NOT_NULL);
    }
}