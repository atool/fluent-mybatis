package cn.org.atool.fluent.mybatis.base.crud;

import cn.org.atool.fluent.mybatis.base.IEntity;

/**
 * 设置Entity, Query, Updater默认值
 *
 * @author darui.wu
 */
public interface IDefaultSetter {
    /**
     * 对保存的entity类设置默认值
     * 比如: 数据隔离的环境值, 租户值等等
     *
     * @param entity
     */
    default void setInsertDefault(IEntity entity) {
    }

    /**
     * 通过query()方法构造的动态SQL默认添加的where条件
     * 比如追加 env的环境变量
     *
     * @param query
     */
    default void setQueryDefault(IQuery query) {
    }

    /**
     * 通过updater()方法构造的动态SQL默认添加的where条件
     * 比如追加 env的环境变量
     *
     * @param updater
     */
    default void setUpdateDefault(IUpdate updater) {
    }
}