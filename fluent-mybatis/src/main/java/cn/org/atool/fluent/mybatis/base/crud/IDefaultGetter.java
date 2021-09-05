package cn.org.atool.fluent.mybatis.base.crud;

/**
 * 进行默认设置
 *
 * @author wudarui
 */
@SuppressWarnings({"rawtypes"})
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
    <Q extends IQuery> Q emptyQuery();

    <Q extends IQuery> Q emptyQuery(String alias);

    /**
     * 实例化查询构造器
     * o - 设置默认查询条件
     *
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
    <Q extends IQuery> Q aliasQuery();

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
    <U extends IUpdate> U updater();

    /**
     * 默认值设置器
     *
     * @return IDefaultSetter
     */
    IDefaultSetter defaultSetter();
}