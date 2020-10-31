package cn.org.atool.fluent.mybatis.base;

import cn.org.atool.fluent.mybatis.base.model.FieldMapping;

/**
 * dao自定义接口继承类, 定义同 IMapperDao
 * 自定义接口泛型参数需要严格按照&lt;E,Q,U>顺序定义
 * 否则会有编译错误或运行时类型转换错误
 *
 * @param <E> 表示Entity类
 * @param <Q> 表示对应的查询器
 * @param <U> 表示对应的更新器
 * @author wudarui
 */
public interface IDao<E, Q, U> {
    /**
     * 获取对应entity的BaseMapper
     *
     * @return
     */
    IEntityMapper mapper();

    /**
     * 构造空白查询条件
     *
     * @return
     */
    IQuery query();

    /**
     * 构造空白更新条件
     *
     * @return
     */
    IUpdate updater();

    /**
     * 对保存的entity类设置默认值
     * 比如: 数据隔离的环境值, 租户值等等
     *
     * @param entity
     */
    default void setInsertDefault(E entity) {
    }

    /**
     * 通过query()方法构造的动态SQL默认添加的where条件
     * 比如追加 env的环境变量
     *
     * @param query
     * @return 返回query本身
     */
    default IWrapper setQueryDefault(Q query) {
        return (IWrapper) query;
    }

    /**
     * 通过updater()方法构造的动态SQL默认添加的where条件
     * 比如追加 env的环境变量
     *
     * @param updater
     * @return 返回updater本身
     */
    default IWrapper setUpdateDefault(U updater) {
        return (IWrapper) updater;
    }

    /**
     * 返回主键字段
     *
     * @return
     */
    default FieldMapping primaryField() {
        throw new RuntimeException("无需实现, 在各EntityDaoImpl有具体实现.");
    }
}