package cn.org.atool.fluent.mybatis.model.form;

import cn.org.atool.fluent.mybatis.base.crud.BaseFormSetter;
import cn.org.atool.fluent.mybatis.base.entity.IMapping;

class EmptyFormSetter extends BaseFormSetter {
    private final IMapping mapping;

    EmptyFormSetter(IMapping mapping) {
        this.mapping = mapping;
    }

    @Override
    public Class entityClass() {
        return mapping.entityClass();
    }

    @Override
    public IMapping _mapping() {
        return mapping;
    }
}