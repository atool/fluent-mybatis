package cn.org.atool.fluent.mybatis.base;

/**
 * 设置Entity, Query, Updater默认值
 *
 * @author darui.wu
 */
public interface IDefault {
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
     * @return 返回query本身
     */
    default IQuery setQueryDefault(IQuery query) {
        return query;
    }

    /**
     * 通过updater()方法构造的动态SQL默认添加的where条件
     * 比如追加 env的环境变量
     *
     * @param updater
     * @return 返回updater本身
     */
    default IUpdate setUpdateDefault(IUpdate updater) {
        return updater;
    }
}