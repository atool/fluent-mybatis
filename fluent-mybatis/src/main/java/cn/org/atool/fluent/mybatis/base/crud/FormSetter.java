package cn.org.atool.fluent.mybatis.base.crud;

import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.functions.FormApply;
import cn.org.atool.fluent.mybatis.model.IFormApply;

import java.util.function.Consumer;

/**
 * 字段设置
 *
 * @author darui.wu
 */
@SuppressWarnings({"rawtypes"})
public abstract class FormSetter {

    protected FormApply formApply;

    protected Consumer<FieldMapping> apply;

    protected FormSetter() {
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
        this.formApply.set(field);
        return this.formApply;
    }

    public abstract Class entityClass();
}