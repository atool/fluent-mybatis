package cn.org.atool.fluent.mybatis.base.dao;

import cn.org.atool.fluent.mybatis.base.IBaseDao;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.crud.IUpdate;

/**
 * BaseDaoImpl
 *
 * @param <E> 实体类
 * @author darui.wu
 */
public abstract class BaseDao<E extends IEntity> implements IBaseDao<E>, IProtectedDao<E> {

    /**
     * 无任何条件的查询
     *
     * @return
     */
    protected abstract IQuery<E> query();

    /**
     * 无任何设置的更新器
     *
     * @return
     */
    protected abstract IUpdate<E> updater();

    /**
     * 构造默认查询条件
     *
     * @return
     */
    protected abstract <Q extends IQuery<E>> Q defaultQuery();

    /**
     * 构造默认更新条件
     *
     * @return
     */
    protected abstract <U extends IUpdate<E>> U defaultUpdater();
}