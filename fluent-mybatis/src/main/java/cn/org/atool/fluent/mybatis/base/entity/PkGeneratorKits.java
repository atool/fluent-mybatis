package cn.org.atool.fluent.mybatis.base.entity;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.IRefs;
import cn.org.atool.fluent.mybatis.base.crud.IDefault;

import java.util.function.Consumer;

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
        if (entity.findPk() != null) {
            return;
        }
        Consumer consumer = entity.pkSetter();
        if (consumer == null) {
            return;
        }
        Class klass = entity.entityClass();
        IDefault defaults = IRefs.instance().findDefault(klass);
        if (defaults == null) {
            return;
        }
        consumer.accept(defaults.pkGenerator(klass));
    }
}
