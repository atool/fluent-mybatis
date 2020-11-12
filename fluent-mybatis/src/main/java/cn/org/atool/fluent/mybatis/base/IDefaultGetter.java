package cn.org.atool.fluent.mybatis.base;

import cn.org.atool.fluent.mybatis.base.impl.BaseQuery;

/**
 * 进行默认设置
 */
public interface IDefaultGetter {
    /**
     * 对entity设置默认值
     * 默认值行为根据 {@link IDefaultSetter#setInsertDefault(IEntity)}来
     *
     * @param entity
     */
    void initEntityDefault(IEntity entity);

    /**
     * 实例化查询构造器
     * o - 设置默认查询条件
     *
     * @return 查询构造器
     */
    <Q extends IQuery> Q defaultQuery();

    /**
     * 实例化查询构造器
     * o - 设置默认查询条件
     * o - 设置别名alias
     *
     * @param alias 别名
     * @return 查询构造器
     */
    <Q extends IQuery> Q defaultQuery(String alias);

    /**
     * 实例化查询构造器
     * o - 设置默认查询条件
     * o - 设置别名alias
     * o - 设置变量实例来自From查询实例
     *
     * @param alias    别名
     * @param joinFrom 关联查询时,from表查询对象
     * @return 查询构造器
     */
    <Q extends IQuery> Q defaultQuery(String alias, BaseQuery joinFrom);

    /**
     * 实例化更新构造器
     * o - 设置默认更新条件
     *
     * @return 更新构造器
     */
    <U extends IUpdate> U defaultUpdater();
}