package cn.org.atool.fluent.mybatis.condition;

import cn.org.atool.fluent.mybatis.condition.interfaces.*;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

/**
 * @param <E>
 * @author darui.wu
 */
public abstract class BaseDaoImpl<E extends IEntity, Q extends IEntityQuery<Q, E>, U extends IEntityUpdate<U>>
        implements IBaseDao<E>, IMapperDao<E, Q, U> {
    @Override
    public <PK extends Serializable> PK save(E entity) {
        this.mapper().insert(entity);
        return (PK) entity.findPk();
    }

    @Override
    public int saveWithPk(List<E> list) {
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
        IEntityQuery<IEntityQuery, E> query = this.query().allEq((Map) where);
        return this.mapper().selectList((Wrapper) query);
    }

    /**
     * 根据query查询满足条件的第一条记录
     *
     * @param query
     * @return
     */
    public E selectOne(IEntityQuery query) {
        query.limit(1);
        return (E) this.mapper().selectOne((Wrapper) query);
    }

    /**
     * 根据query查询满足条件的第一条记录，并根据function解析出对应字段
     *
     * @param query
     * @param function 获取entity字段值函数
     * @param <F>
     * @return
     */
    public <F> F selectOne(IEntityQuery query, Function<E, F> function) {
        query.limit(1);
        E obj = (E) this.mapper().selectOne((Wrapper) query);
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
    public <F> List<F> selectFields(IEntityQuery query, Function<E, F> function) {
        return this.mapper().selectList((Wrapper<E>) query).stream()
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
    public <F> List<F> selectObjs(IEntityQuery query, Function<Map<String, Object>, F> function) {
        List<Map<String, Object>> list = this.mapper().selectMaps((Wrapper<E>) query);
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
    public List<E> selectList(IEntityQuery query) {
        return this.mapper().selectList((Wrapper) query);
    }

    /**
     * 返回符合条件的记录数
     *
     * @param query
     * @return
     */
    public int count(IEntityQuery query) {
        return this.mapper().selectCount((Wrapper) query);
    }

    @Override
    public boolean existPk(Serializable id) {
        Q query = this.query().eq(this.findPkColumn(), id);
        Integer count = this.mapper().selectCount((Wrapper<E>) query);
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
    public int update(IEntityUpdate update) {
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
    public int deleteByQuery(IEntityQuery query) {
        return this.mapper().delete((Wrapper) query);
    }

    @Override
    public int deleteByMap(Map<String, Object> map) {
        IEntityQuery<IEntityQuery, E> query = this.query().allEq((Map) map);
        return this.mapper().delete((Wrapper) query);
    }
}