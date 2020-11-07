package cn.org.atool.fluent.mybatis.base.impl;

import cn.org.atool.fluent.mybatis.base.IBaseDao;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.IQuery;
import cn.org.atool.fluent.mybatis.base.IUpdate;
import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static cn.org.atool.fluent.mybatis.base.model.SqlOp.EQ;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

/**
 * BaseDaoImpl
 *
 * @param <E> 实体类
 * @author darui.wu
 */
public abstract class BaseDaoImpl<E extends IEntity>
    extends DaoProtectedImpl<E> implements IBaseDao<E> {

    @Override
    public <PK extends Serializable> PK save(E entity) {
        this.setEntityDefault(entity);
        this.mapper().insert(entity);
        return (PK) entity.findPk();
    }

    @Override
    public int save(List<E> list) {
        Set pks = list.stream().map(E::findPk).filter(pk -> pk != null).collect(toSet());
        if (!pks.isEmpty() && pks.size() != list.size()) {
            throw FluentMybatisException.instance("The primary key of the list instance must be assigned to all or none");
        }
        list.forEach(this::setEntityDefault);
        return this.mapper().insertBatch(list);
    }

    @Override
    public boolean saveOrUpdate(E entity) {
        if (entity.findPk() == null) {
            this.setEntityDefault(entity);
            return this.mapper().insert(entity) > 0;
        } else if (this.existPk(entity.findPk())) {
            return this.mapper().updateById(entity) > 0;
        } else {
            this.setEntityDefault(entity);
            return this.mapper().insert(entity) > 0;
        }
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
        IQuery query = this.defaultQuery().where().eqNotNull(where).end();
        return this.mapper().listEntity(query);
    }

    @Override
    public boolean existPk(Serializable id) {
        /**
         * 只设置id，不添加默认值
         */
        IQuery query = this.query().where().apply(this.primaryField(), EQ, id).end();
        Integer count = this.mapper().count(query);
        return count != null && count > 0;
    }

    @Override
    public boolean updateById(E entity) {
        return this.mapper().updateById(entity) > 0;
    }

    @Override
    public int deleteByEntityIds(Collection<E> entities) {
        List<Serializable> ids = entities.stream()
            .map(IEntity::findPk)
            .collect(toList());
        return this.mapper().deleteByIds(ids);
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
        IQuery query = (IQuery) this.defaultQuery().where().eqNotNull((Map) map).end();
        return this.mapper().delete(query);
    }


    /**
     * 插入实例的默认值字段设置
     *
     * @param entity
     */
    protected abstract void setEntityDefault(E entity);

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