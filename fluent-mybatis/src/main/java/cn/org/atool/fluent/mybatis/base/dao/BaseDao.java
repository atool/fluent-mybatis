package cn.org.atool.fluent.mybatis.base.dao;

import cn.org.atool.fluent.mybatis.base.IBaseDao;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.crud.IUpdate;

import java.util.ArrayList;
import java.util.List;

/**
 * BaseDaoImpl
 *
 * @param <E> 实体类
 * @author darui.wu
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public abstract class BaseDao<E extends IEntity> implements IBaseDao<E>, IProtectedDao<E> {

    /**
     * 无任何条件的查询
     *
     * @return IQuery
     */
    protected abstract IQuery<E> query();

    /**
     * 无任何设置的更新器
     *
     * @return IUpdate
     */
    protected abstract IUpdate<E> updater();

    /**
     * 构造默认查询条件
     *
     * @return IQuery
     */
    protected abstract <Q extends IQuery<E>> Q defaultQuery();

    /**
     * 构造默认更新条件
     *
     * @return IUpdate
     */
    protected abstract <U extends IUpdate<E>> U defaultUpdater();

    @Override
    public boolean updateEntityByIds(E... entities) {
        List<IUpdate> updates = new ArrayList<>(entities.length);
        for (IEntity entity : entities) {
            IUpdate update = DaoHelper.buildUpdateEntityById(this::updater, entity);
            updates.add(update);
        }
        int count = this.mapper().updateBy(updates.toArray(new IUpdate[0]));
        return count > 0;
    }

    @Override
    public int updateBy(E updateNoN, E whereNoN) {
        IUpdate updater = DaoHelper.buildUpdateByEntityNoN(this::defaultUpdater, updateNoN, whereNoN);
        return this.updateBy(updater);
    }
}