package cn.org.atool.fluent.mybatis.base.crud;

import cn.org.atool.fluent.mybatis.base.IEntity;

/**
 * 进行默认设置
 *
 * @author wudarui
 */
@SuppressWarnings({"rawtypes"})
public interface IDefaultGetter {
    /**
     * 对entity设置默认值
     * 默认值行为根据 {@link IDefaultSetter#setInsertDefault(IEntity)}来
     *
     * @param entity 实例
     */
    void setEntityByDefault(IEntity entity);

    /**
     * 创建一个空查询器(不包括{@link IDefaultSetter#setQueryDefault(IQuery)} 设置的默认条件)
     *
     * @param <Q> IQuery类型
     * @return IQuery
     */
    <Q extends IQuery> Q query();

    /**
     * 实例化查询构造器
     * o - 设置默认查询条件
     *
     * @return 查询构造器
     */
    <Q extends IQuery> Q defaultQuery();

    /**
     * 创建一个更新器(不包括{@link IDefaultSetter#setUpdateDefault(IUpdate)} 设置的默认条件)
     *
     * @param <U> IUpdate类型
     * @return IUpdate
     */
    <U extends IUpdate> U updater();

    /**
     * 实例化更新构造器
     * o - 设置默认更新条件
     *
     * @return 更新构造器
     */
    <U extends IUpdate> U defaultUpdater();

    /**
     * 自动分配表别名查询构造器(join查询的时候需要定义表别名)
     * 如果要自定义别名, 使用方法 {@link #aliasQuery(String)}
     *
     * @param <Q> IQuery类型
     * @return IQuery
     */
    <Q extends IQuery> Q aliasQuery();

    /**
     * 实例化查询构造器
     * o - 设置默认查询条件
     * o - 设置别名alias
     *
     * @param alias 别名
     * @return 查询构造器
     */
    <Q extends IQuery> Q aliasQuery(String alias);

    /**
     * 关联查询
     * o - 设置默认查询条件
     * o - 自动别名alias
     * o - 设置变量实例来自From查询实例
     *
     * @param fromQuery 关联查询时,from表查询对象
     * @param <Q>       IQuery类型
     * @return IQuery
     */
    <Q extends IQuery> Q aliasWith(BaseQuery fromQuery);

    /**
     * 实例化查询构造器
     * o - 设置默认查询条件
     * o - 设置别名alias
     * o - 设置变量实例来自From查询实例
     *
     * @param alias     别名
     * @param fromQuery 关联查询时,from表查询对象
     * @return 查询构造器
     */
    <Q extends IQuery> Q aliasWith(String alias, BaseQuery fromQuery);
}