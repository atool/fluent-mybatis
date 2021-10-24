package cn.org.atool.fluent.mybatis.base.crud;

import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.model.form.FormApply;
import cn.org.atool.fluent.mybatis.model.form.IFormApply;

import java.util.function.Consumer;

/**
 * 字段设置
 *
 * @author darui.wu
 */
@SuppressWarnings({"rawtypes"})
public abstract class BaseFormSetter {

    protected FormApply formApply;

    protected Consumer<FieldMapping> apply;

    protected BaseFormSetter() {
    }

    public void set(Consumer<FieldMapping> apply) {
        this.apply = apply;
    }

    /**
     * 设置当前操作字段
     *
     * @param field 字段
     * @return ignore
     */
    public IFormApply set(FieldMapping field) {
        this.formApply.addWhere(field);
        return this.formApply;
    }

    public abstract Class entityClass();

    public abstract IMapping _mapping();
}