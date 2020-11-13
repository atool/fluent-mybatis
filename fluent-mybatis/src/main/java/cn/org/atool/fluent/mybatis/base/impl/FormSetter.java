package cn.org.atool.fluent.mybatis.base.impl;

import cn.org.atool.fluent.mybatis.base.model.FieldMapping;

import java.util.function.Consumer;

/**
 * 字段设置
 *
 * @param <Q>
 * @author darui.wu
 */
public class FormSetter<Q> {
    /**
     * IFormQuery
     */
    private final Q query;

    private Consumer<FieldMapping> apply;

    protected FormSetter(Q query) {
        this.query = query;
    }

    public void set(Consumer<FieldMapping> apply) {
        this.apply = apply;
    }

    /**
     * 设置当前操作字段
     *
     * @param field
     * @return
     */
    public Q set(FieldMapping field) {
        this.apply.accept(field);
        return this.query;
    }
}