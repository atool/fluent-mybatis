package cn.org.atool.fluent.mybatis.base.crud;

import cn.org.atool.fluent.mybatis.base.IEntity;

import java.util.function.Supplier;

/**
 * 设置Entity, Query, Updater默认值
 *
 * @author darui.wu
 */
@SuppressWarnings({"rawtypes", "unused"})
public interface IDefaultSetter {
    /**
     * entityKlass主键生成器
     *
     * @param entityKlass 实例类型
     */
    default Supplier<Object> pkGenerator(Class entityKlass) {
        return () -> null;
    }

    /**
     * 对保存的entity类设置默认值
     * 比如: 数据隔离的环境值, 租户值等等
     *
     * @param entity 实例
     */
    default void setInsertDefault(IEntity entity) {
    }

    /**
     * 通过query()方法构造的动态SQL默认添加的where条件
     * 比如追加 env的环境变量
     *
     * @param query 查询器
     */
    default void setQueryDefault(IQuery query) {
    }

    /**
     * 通过updater()方法构造的动态SQL默认添加的where条件
     * 比如追加 env的环境变量
     *
     * @param updater 更新器
     */
    default void setUpdateDefault(IUpdate updater) {
    }
}