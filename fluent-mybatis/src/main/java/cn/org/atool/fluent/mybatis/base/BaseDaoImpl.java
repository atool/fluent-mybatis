package cn.org.atool.fluent.mybatis.base;

import cn.org.atool.fluent.mybatis.base.*;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * BaseDaoImpl
 *
 * @param <E> 实体类
 * @author darui.wu
 */
public abstract class BaseDaoImpl<E extends IEntity> implements IBaseDao<E>, IDao<E> {
    @Override
    public <PK extends Serializable> PK save(E entity) {
        return this.mapper().save(entity);
    }

    @Override
    public int save(List<E> list) {
        return this.mapper().save(list);
    }

    @Override
    public boolean saveOrUpdate(E entity) {
        return this.mapper().saveOrUpdate(entity);
    }

    @Override
    public E selectById(Serializable id) {
        return this.mapper().findById(id);
    }

    @Override
    public List<E> selectByIds(Collection<? extends Serializable> ids) {
        return this.mapper().listByIds(ids);
    }

    @Override
    public List<E> selectByMap(Map<String, Object> where) {
        return this.mapper().listByMapAndDefault(where);
    }

    @Override
    public boolean existPk(Serializable id) {
        return this.mapper().existPk(id);
    }

    @Override
    public boolean updateById(E entity) {
        return this.mapper().updateById(entity) > 0;
    }

    @Override
    public int deleteByEntityIds(Collection<E> entities) {
        return this.mapper().deleteByEntityIds(entities);
    }

    @Override
    public int deleteByIds(Collection<? extends Serializable> ids) {
        return this.mapper().deleteByIds(ids);
    }

    @Override
    public boolean deleteById(Serializable id) {
        return this.mapper().deleteById(id) > 0;
    }

    @Override
    public int deleteByMap(Map<String, Object> map) {
        return this.mapper().deleteByMapAndDefault(map);
    }

    /**
     * 无任何条件的查询
     *
     * @return
     */
    protected abstract IQuery<E, ?> query();

    /**
     * 无任何设置的更新器
     *
     * @return
     */
    protected abstract IUpdate<E, ?, ?> updater();
}