package cn.org.atool.fluent.mybatis.base.crud;

import cn.org.atool.fluent.mybatis.functions.StringSupplier;

/**
 * 进行默认设置
 *
 * @author wudarui
 */
@SuppressWarnings({ "rawtypes" })
public interface IDefaultGetter {
    /**
     * Entity class
     *
     * @return Entity class
     */
    Class entityClass();

    /**
     * 创建一个空查询器(不包括{@link IDefaultSetter#setQueryDefault(IQuery)} 设置的默认条件)
     *
     * @param <Q> IQuery类型
     * @return IQuery
     */
    /**
     * 创建一个空查询器(不包括{@link IDefaultSetter#setQueryDefault(IQuery)} 设置的默认条件)
     *
     * @param <Q> IQuery类型
     * @return IQuery
     */
    <Q extends IQuery> Q emptyQuery();

    /**
     * 创建一个空查询器
     *
     * @param alias 表别名
     * @param <Q>   IQuery类型
     * @return IQuery
     */
    <Q extends IQuery> Q emptyQuery(String alias);

    /**
     * 创建一个空查询器
     *
     * @param alias 表别名
     * @param <Q>   IQuery类型
     * @return IQuery
     */
    <Q extends IQuery> Q emptyQuery(StringSupplier alias);

    /**
     * 实例化查询构造器
     * o - 设置默认查询条件
     *
     * @return 查询构造器
     */
    /**
     * 实例化查询构造器
     * o - 设置默认查询条件
     *
     * @param <Q> IQuery类型
     * @return 查询构造器
     */
    <Q extends IQuery> Q query();

    /**
     * 自动分配表别名查询构造器(join查询的时候需要定义表别名)
     * 如果要自定义别名, 使用方法 {@link #query(String)}
     *
     * @param <Q> IQuery类型
     * @return IQuery
     */
    <Q extends IQuery> Q alias();

    /**
     * 实例化查询构造器
     * o - 设置默认查询条件
     * o - 设置别名alias
     *
     * @param alias 别名
     * @return 查询构造器
     */
    <Q extends IQuery> Q query(String alias);

    /**
     * 实例化查询构造器
     *
     * @param alias 别名
     * @param <Q>   IQuery类型
     * @return 查询构造器
     */
    <Q extends IQuery> Q query(StringSupplier alias);

    /**
     * 创建一个更新器(不包括{@link IDefaultSetter#setUpdateDefault(IUpdate)} 设置的默认条件)
     *
     * @param <U> IUpdate类型
     * @return IUpdate
     */
    <U extends IUpdate> U emptyUpdater();

    /**
     * 实例化更新构造器
     * o - 设置默认更新条件
     *
     * @return 更新构造器
     */
    /**
     * 实例化更新构造器
     * o - 设置默认更新条件
     *
     * @param <U> IUpdate类型
     * @return 更新构造器
     */
    <U extends IUpdate> U updater();

    /**
     * 默认值设置器
     *
     * @return IDefaultSetter
     */
    IDefaultSetter defaultSetter();
}