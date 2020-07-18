package cn.org.atool.fluent.mybatis.base.impl;

import cn.org.atool.fluent.mybatis.base.IDao;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.IQuery;
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
    extends DaoProtectedImpl<E> implements IDao<E> {

    @Override
    public <PK extends Serializable> PK save(E entity) {
        this.mapper().insert(entity);
        return (PK) entity.findPk();
    }

    @Override
    public int save(List<E> list) {
        Set pks = list.stream().map(E::findPk).filter(pk -> pk != null).collect(toSet());
        if (!pks.isEmpty() && pks.size() != list.size()) {
            throw FluentMybatisException.instance("The primary key of the list instance must be assigned to all or none");
        }
        return this.mapper().insertBatch(list);
    }

    @Override
    public boolean saveOrUpdate(E entity) {
        if (entity.findPk() == null) {
            return this.mapper().insert(entity) > 0;
        } else if (this.existPk(entity.findPk())) {
            return this.mapper().updateById(entity) > 0;
        } else {
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
        IQuery query = this.query().where().eqNotNull(where).end();
        return this.mapper().listEntity(query);
    }

    @Override
    public boolean existPk(Serializable id) {
        IQuery query = this.query().where().apply(this.findPkColumn(), EQ, id).end();
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
        IQuery query = (IQuery) this.query().where().eqNotNull((Map) map).end();
        return this.mapper().delete(query);
    }
}