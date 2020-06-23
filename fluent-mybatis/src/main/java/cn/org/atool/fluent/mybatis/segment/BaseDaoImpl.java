package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;
import cn.org.atool.fluent.mybatis.interfaces.*;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;

import static cn.org.atool.fluent.mybatis.segment.model.SqlOp.EQ;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

/**
 * @param <E>
 * @author darui.wu
 */
public abstract class BaseDaoImpl<E extends IEntity, Q extends IQuery<E, Q>, U extends IUpdate<E, U, Q>>
    implements IDao<E>, IBaseDao<E, Q, U> {
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
        return this.mapper().selectById(id);
    }

    @Override
    public List<E> selectByIds(Collection<? extends Serializable> ids) {
        return this.mapper().selectByIds(ids);
    }

    @Override
    public List<E> selectByMap(Map<String, Object> where) {
        IQuery query = (IQuery) this.query().where().eqByNotNull((Map) where).end();
        return this.mapper().selectList(query);
    }

    /**
     * 根据query查询满足条件的第一条记录
     *
     * @param query
     * @return
     */
    public E selectOne(IQuery query) {
        query.limit(1);
        return (E) this.mapper().selectOne(query);
    }

    /**
     * 根据query查询满足条件的第一条记录，并根据function解析出对应字段
     *
     * @param query
     * @param function 获取entity字段值函数
     * @param <F>
     * @return
     */
    public <F> F selectOne(IQuery query, Function<E, F> function) {
        query.limit(1);
        E obj = (E) this.mapper().selectOne(query);
        return obj == null ? null : (F) function.apply(obj);
    }

    /**
     * 根据query查询记录列表，并根据function解析出对应字段
     *
     * @param query
     * @param function 获取entity字段值函数
     * @param <F>
     * @return
     */
    public <F> List<F> selectFields(IQuery query, Function<E, F> function) {
        return this.mapper().selectList(query).stream()
            .map(function::apply)
            .collect(toList());
    }

    /**
     * 根据query查询记录列表，并根据function将记录转换成需要的对象F
     *
     * @param query
     * @param function 从Map记录对实体F的转换函数
     * @param <F>
     * @return
     */
    public <F> List<F> selectObjs(IQuery query, Function<Map<String, Object>, F> function) {
        List<Map<String, Object>> list = this.mapper().selectMaps(query);
        return list.stream()
            // mybatis有个bug，当所有字段值为null时, 不会返回Map对象, 而是返回一个null
            .map(map -> map == null ? new HashMap<String, Object>() : map)
            .map(function::apply)
            .collect(toList());
    }

    /**
     * 根据query查询记录
     *
     * @param query
     * @return
     */
    public List<E> selectList(IQuery query) {
        return this.mapper().selectList(query);
    }

    /**
     * 返回符合条件的记录数
     *
     * @param query
     * @return
     */
    public int count(IQuery query) {
        return this.mapper().selectCount(query);
    }

    @Override
    public boolean existPk(Serializable id) {
        Q query = this.query().where().and(this.findPkColumn(), EQ, id).end();
        Integer count = this.mapper().selectCount(query);
        return count != null && count > 0;
    }

    @Override
    public boolean updateById(E entity) {
        return this.mapper().updateById(entity) > 0;
    }

    /**
     * 根据update设置更新记录
     *
     * @param update
     * @return
     */
    public int update(IUpdate update) {
        return this.mapper().updateBy(update);
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

    /**
     * 根据条件query删除记录
     *
     * @param query
     * @return
     */
    public int deleteByQuery(IQuery query) {
        return this.mapper().delete(query);
    }

    @Override
    public int deleteByMap(Map<String, Object> map) {
        IQuery query = (IQuery) this.query().where().eqByNotNull((Map) map).end();
        return this.mapper().delete(query);
    }
}