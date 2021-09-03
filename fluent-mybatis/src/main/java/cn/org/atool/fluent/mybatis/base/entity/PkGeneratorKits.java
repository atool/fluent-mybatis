package cn.org.atool.fluent.mybatis.base.entity;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.IRefs;
import cn.org.atool.fluent.mybatis.base.crud.BaseDefaults;

import java.util.function.Consumer;
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
        Consumer consumer = entity.pkSetter();
        if (consumer == null) {
            return;
        }
        Class klass = entity.entityClass();
        BaseDefaults defaults = IRefs.instance().findDefault(klass);
        if (defaults == null) {
            return;
        }
        Supplier pkSupplier = defaults.setter().pkGenerator(entity);
        if (pkSupplier != null) {
            consumer.accept(pkSupplier.get());
        }
    }
}