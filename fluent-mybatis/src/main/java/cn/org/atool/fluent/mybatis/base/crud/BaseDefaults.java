package cn.org.atool.fluent.mybatis.base.crud;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.segment.model.Parameters;

/**
 * 默认行为接口
 *
 * @author wudarui
 */
@SuppressWarnings("unchecked")
public abstract class BaseDefaults<E extends IEntity, Q extends IQuery<E>, U extends IUpdate<E>>
    implements IDefaultGetter {

    @Override
    public Q defaultQuery() {
        Q query = this.query();
        this.defaultSetter().setQueryDefault(query);
        return query;
    }

    @Override
    public U defaultUpdater() {
        U updater = this.updater();
        this.defaultSetter().setUpdateDefault(updater);
        return updater;
    }

    /**
     * 自动分配表别名查询构造器(join查询的时候需要定义表别名)
     * 如果要自定义别名, 使用方法 {@link #aliasQuery(String)}
     */
    @Override
    public Q aliasQuery() {
        Q query = this.aliasQuery(Parameters.alias(), new Parameters());
        this.defaultSetter().setQueryDefault(query);
        return query;
    }

    /**
     * 显式指定表别名(join查询的时候需要定义表别名)
     */
    @Override
    public Q aliasQuery(String alias) {
        Q query = this.aliasQuery(alias, new Parameters());
        this.defaultSetter().setQueryDefault(query);
        return query;
    }

    /**
     * 关联查询, 根据fromQuery自动设置别名和关联?参数
     * 如果要自定义别名, 使用方法 {@link #aliasWith(String, BaseQuery)}
     */
    @Override
    public Q aliasWith(BaseQuery fromQuery) {
        Parameters parameters = fromQuery.getWrapperData().getParameters();
        Q query = this.aliasQuery(Parameters.alias(), parameters);
        this.defaultSetter().setQueryDefault(query);
        return query;
    }

    /**
     * 关联查询, 显式设置别名, 根据fromQuery自动关联?参数
     */
    @Override
    public Q aliasWith(String alias, BaseQuery fromQuery) {
        Q query = this.aliasQuery(alias, fromQuery.getWrapperData().getParameters());
        this.defaultSetter().setQueryDefault(query);
        return query;
    }

    protected abstract Q aliasQuery(String alias, Parameters parameters);

    public abstract IDefaultSetter defaultSetter();
}