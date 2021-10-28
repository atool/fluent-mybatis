package cn.org.atool.fluent.form.setter;

import cn.org.atool.fluent.form.Form;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.utility.MybatisUtil;

/**
 * 增加表单项条件
 *
 * @author wudarui
 */
@SuppressWarnings({"unused", "rawtypes"})
public class FormItemAdder {
    private final Form form;

    public FormItemAdder(Form form) {
        this.form = form;
    }

    /**
     * 增加 and 条件项
     *
     * @param field key
     * @param op    SqlOp操作符
     * @param value value
     * @return Form
     */
    public Form where(String field, String op, Object... value) {
        this.form.getWhere().add(new FormEntry(field, op, value));
        return form;
    }

    public Form eq(FieldMapping key, Object value) {
        return this.where(key.name, FormSqlOp.OP_EQ, value);
    }

    public Form eq(String key, Object value) {
        return this.where(key, FormSqlOp.OP_EQ, value);
    }

    public Form gt(FieldMapping key, Object value) {
        return this.where(key.name, FormSqlOp.OP_GT, value);
    }

    public Form gt(String key, Object value) {
        return this.where(key, FormSqlOp.OP_GT, value);
    }

    public Form ge(FieldMapping key, Object value) {
        return this.where(key.name, FormSqlOp.OP_GE, value);
    }

    public Form ge(String key, Object value) {
        return this.where(key, FormSqlOp.OP_GE, value);
    }

    public Form lt(FieldMapping key, Object value) {
        return this.where(key.name, FormSqlOp.OP_LT, value);
    }

    public Form lt(String key, Object value) {
        return this.where(key, FormSqlOp.OP_LT, value);
    }

    public Form le(FieldMapping key, Object value) {
        return this.where(key.name, FormSqlOp.OP_LE, value);
    }

    public Form le(String key, Object value) {
        return this.where(key, FormSqlOp.OP_LE, value);
    }

    public Form ne(String key, Object value) {
        return this.where(key, FormSqlOp.OP_NE, value);
    }

    public Form like(FieldMapping key, String value) {
        return this.where(key.name, FormSqlOp.OP_LIKE, value);
    }

    public Form like(String key, String value) {
        return this.where(key, FormSqlOp.OP_LIKE, value);
    }

    public Form notLike(FieldMapping key, String value) {
        return this.where(key.name, FormSqlOp.OP_NOT_LIKE, value);
    }

    public Form notLike(String key, String value) {
        return this.where(key, FormSqlOp.OP_NOT_LIKE, value);
    }

    public Form between(FieldMapping key, Object min, Object max) {
        MybatisUtil.assertNotNull("min", min);
        MybatisUtil.assertNotNull("max", max);
        return this.where(key.name, FormSqlOp.OP_BETWEEN, min, max);
    }

    public Form between(String key, Object min, Object max) {
        MybatisUtil.assertNotNull("min", min);
        MybatisUtil.assertNotNull("max", max);
        return this.where(key, FormSqlOp.OP_BETWEEN, min, max);
    }

    public Form notBetween(FieldMapping key, Object min, Object max) {
        MybatisUtil.assertNotNull("min", min);
        MybatisUtil.assertNotNull("max", max);
        return this.where(key.name, FormSqlOp.OP_NOT_BETWEEN, min, max);
    }

    public Form notBetween(String key, Object min, Object max) {
        MybatisUtil.assertNotNull("min", min);
        MybatisUtil.assertNotNull("max", max);
        return this.where(key, FormSqlOp.OP_NOT_BETWEEN, min, max);
    }

    public Form in(FieldMapping key, Object... value) {
        MybatisUtil.assertNotEmpty("items", value);
        return this.where(key.name, FormSqlOp.OP_BETWEEN, value);
    }

    public Form in(String key, Object... value) {
        MybatisUtil.assertNotEmpty("items", value);
        return this.where(key, FormSqlOp.OP_BETWEEN, value);
    }

    public Form notIn(FieldMapping key, Object... value) {
        MybatisUtil.assertNotEmpty("items", value);
        return this.where(key.name, FormSqlOp.OP_NOT_IN, value);
    }

    public Form notIn(String key, Object... values) {
        MybatisUtil.assertNotEmpty("items", values);
        return this.where(key, FormSqlOp.OP_NOT_IN, values);
    }

    public Form isNull(FieldMapping key) {
        return this.where(key.name, FormSqlOp.OP_IS_NULL);
    }

    public Form isNull(String key) {
        return this.where(key, FormSqlOp.OP_IS_NULL);
    }

    public Form notNull(FieldMapping key) {
        return this.where(key.name, FormSqlOp.OP_NOT_NULL);
    }

    public Form notNull(String key) {
        return this.where(key, FormSqlOp.OP_NOT_NULL);
    }
}