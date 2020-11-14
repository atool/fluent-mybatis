package cn.org.atool.fluent.mybatis.base;

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
    void setEntityByDefault(IEntity entity);

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
     * 自动分配表别名查询构造器(join查询的时候需要定义表别名)
     * 如果要自定义别名, 使用方法 {@link #defaultQuery(String)}
     *
     * @param <Q>
     * @return
     */
    <Q extends IQuery> Q defaultAliasQuery();

    /**
     * @deprecated use {@link #joinFrom(String, BaseQuery)}
     */
    @Deprecated
    <Q extends IQuery> Q defaultQuery(String alias, BaseQuery joinFrom);

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
    <Q extends IQuery> Q joinFrom(String alias, BaseQuery joinFrom);

    /**
     * 关联查询
     * o - 设置默认查询条件
     * o - 自动别名alias
     * o - 设置变量实例来自From查询实例
     *
     * @param joinFrom 关联查询时,from表查询对象
     * @param <Q>
     * @return
     */
    <Q extends IQuery> Q joinFrom(BaseQuery joinFrom);

    /**
     * 实例化更新构造器
     * o - 设置默认更新条件
     *
     * @return 更新构造器
     */
    <U extends IUpdate> U defaultUpdater();
}