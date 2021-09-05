package cn.org.atool.fluent.mybatis.base.crud;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.segment.model.Parameters;

import java.util.function.Supplier;

/**
 * 默认行为接口
 *
 * @author wudarui
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public abstract class BaseDefaults<E extends IEntity, Q extends IQuery<E>, U extends IUpdate<E>>
    implements IDefaultGetter {

    protected abstract Q query(boolean defaults, Supplier<String> table, String alias, Parameters parameters);

    protected abstract U updater(boolean defaults, Supplier<String> table, String alias, Parameters parameters);

    /* =======query method======= */

    @Override
    public Q emptyQuery() {
        return this.query(false, null, null, null);
    }

    @Override
    public Q emptyQuery(String alias) {
        return this.query(false, null, alias, null);
    }

    @Override
    public Q query() {
        return this.query(true, null, null, null);
    }

    /**
     * 显式指定表别名(join查询的时候需要定义表别名)
     */
    @Override
    public Q query(String alias) {
        return this.query(true, null, alias, null);
    }

    /**
     * 自动分配表别名查询构造器(join查询的时候需要定义表别名)
     * 如果要自定义别名, 使用方法 {@link #query(String)}
     */
    @Override
    public Q alias() {
        return this.query(true, null, Parameters.alias(), null);
    }

    /**
     * 关联查询, 显式设置别名, 根据fromQuery自动关联?参数
     */
    @Override
    public Q aliasWith(String alias, BaseQuery fromQuery) {
        return this.query(true, null, alias, fromQuery.getWrapperData().getParameters());
    }

    /* ========updater method======= */

    @Override
    public U emptyUpdater() {
        return this.updater(false, null, null, null);
    }

    @Override
    public U updater() {
        return this.updater(true, null, null, null);
    }
}