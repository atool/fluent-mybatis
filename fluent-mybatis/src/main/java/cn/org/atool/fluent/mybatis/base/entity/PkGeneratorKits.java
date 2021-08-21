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
@SuppressWarnings("rawtypes")
public class PkGeneratorKits {
    public static void setIdByIdGenerator(IEntity entity) {
        Class klass = entity.entityClass();
        IDefault defaults = IRefs.instance().findDefault(klass);
        Consumer consumer = entity.pkSetter();
        if (defaults != null && consumer != null) {
            consumer.accept(defaults.idGenerator(klass));
        }
    }
}
