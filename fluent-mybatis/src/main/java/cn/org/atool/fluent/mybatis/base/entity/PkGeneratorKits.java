package cn.org.atool.fluent.mybatis.base.entity;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.BaseDefaults;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.utility.RefKit;

import java.util.function.Supplier;

/**
 * 实例主键生成器
 *
 * @author wudarui
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class PkGeneratorKits {
    /**
     * 设置主键值
     *
     * @param entity IEntity
     */
    public static void setPkByGenerator(IEntity entity) {
        if (entity == null || entity.findPk() != null) {
            return;
        }
        FieldMapping primary = RefKit.byEntity(entity.entityClass()).primaryMapping();
        if (primary == null) {
            return;
        }
        Class klass = entity.entityClass();
        BaseDefaults defaults = RefKit.byEntity(klass);
        if (defaults == null) {
            return;
        }
        Supplier pkSupplier = defaults.defaultSetter().pkGenerator(entity);
        if (pkSupplier != null) {
            primary.setter.set(entity, pkSupplier.get());
        }
    }
}