package cn.org.atool.fluent.mybatis.base.dao;

import cn.org.atool.fluent.mybatis.base.IBaseDao;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.IDefaultGetter;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.crud.IUpdate;
import cn.org.atool.fluent.mybatis.base.mapper.IMapper;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.kits.IStrEnums;

/**
 * BaseDaoImpl
 *
 * @param <E> 实体类
 * @author darui.wu
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public abstract class BaseDao<E extends IEntity, Q extends IQuery<E>, U extends IUpdate<E>>
    implements IBaseDao<E>, IProtectedDao<E>, IStrEnums {

    /**
     * 实体类class
     *
     * @return Entity class
     */
    protected abstract IDefaultGetter defaults();

    /**
     * 构造默认查询条件
     *
     * @return IQuery
     */
    protected Q query() {
        return this.defaults().query();
    }

    /**
     * 无任何条件的查询
     *
     * @return IQuery
     */
    protected Q emptyQuery() {
        return defaults().emptyQuery();
    }

    /**
     * @deprecated replaced by query()
     */
    @Deprecated
    protected Q defaultQuery() {
        return this.query();
    }

    /**
     * 构造默认更新条件
     *
     * @return IUpdate
     */
    protected U updater() {
        return this.defaults().updater();
    }

    /**
     * 无任何设置的更新器
     *
     * @return IUpdate
     */
    protected U emptyUpdater() {
        return this.defaults().emptyUpdater();
    }

    /**
     * @deprecated replaced by updater()
     */
    @Deprecated
    protected U defaultUpdater() {
        return this.updater();
    }

    @Override
    public boolean updateEntityByIds(E... entities) {
        List<IUpdate> updates = new ArrayList<>(entities.length);
        for (IEntity entity : entities) {
            IUpdate update = DaoHelper.buildUpdateEntityById(this::emptyUpdater, entity);
            updates.add(update);
        }
        int count = this.mapper().updateBy(updates.toArray(new IUpdate[0]));
        return count > 0;
    }

    @Override
    public int updateBy(E updateNoN, E whereNoN) {
        IUpdate updater = DaoHelper.buildUpdateByEntityNoN(this::updater, updateNoN, whereNoN);
        return this.updateBy(updater);
    }

    public abstract void setMapper(IMapper<E> mapper);
}