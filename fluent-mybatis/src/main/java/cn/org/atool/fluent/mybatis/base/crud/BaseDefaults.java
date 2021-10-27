package cn.org.atool.fluent.mybatis.base.crud;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.functions.StringSupplier;
import cn.org.atool.fluent.mybatis.segment.model.Parameters;

/**
 * 默认行为接口
 *
 * @author wudarui
 */
@SuppressWarnings({"unchecked"})
public abstract class BaseDefaults<E extends IEntity, Q extends IQuery<E>, U extends IUpdate<E>>
    implements IDefaultGetter {

    protected abstract Q query(boolean defaults, StringSupplier table, StringSupplier alias, Parameters parameters);

    protected abstract U updater(boolean defaults, StringSupplier table, StringSupplier alias, Parameters parameters);

    /* =======query method======= */

    @Override
    public Q emptyQuery() {
        return this.query(false, null, null, null);
    }

    @Override
    public Q emptyQuery(String alias) {
        return this.query(false, null, () -> alias, null);
    }

    @Override
    public Q emptyQuery(StringSupplier alias) {
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
        return this.query(true, null, () -> alias, null);
    }

    @Override
    public Q query(StringSupplier alias) {
        return this.query(true, null, alias, null);
    }

    /**
     * 自动分配表别名查询构造器(join查询的时候需要定义表别名)
     * 如果要自定义别名, 使用方法 {@link #query(String)}
     */
    @Override
    public Q alias() {
        String alias = Parameters.alias();
        return this.query(true, null, () -> alias, null);
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