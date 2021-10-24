package cn.org.atool.fluent.mybatis.form.setter;

import cn.org.atool.fluent.mybatis.base.entity.IMapping;

/**
 * EmptyFormSetter
 *
 * @author wudarui
 */
@SuppressWarnings("rawtypes")
public class EmptyFormSetter extends BaseFormSetter {
    private final IMapping mapping;

    public EmptyFormSetter(IMapping mapping) {
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