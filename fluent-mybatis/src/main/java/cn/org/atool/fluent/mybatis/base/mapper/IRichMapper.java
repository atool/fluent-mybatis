package cn.org.atool.fluent.mybatis.base.mapper;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.IDefaultSetter;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.model.Column;
import cn.org.atool.fluent.mybatis.functions.MapFunction;
import cn.org.atool.fluent.mybatis.model.StdPagedList;
import cn.org.atool.fluent.mybatis.model.TagPagedList;
import cn.org.atool.fluent.mybatis.utility.PoJoHelper;
import lombok.NonNull;

import java.io.Serializable;
import java.util.*;

import static cn.org.atool.fluent.mybatis.base.model.SqlOp.EQ;
import static java.util.stream.Collectors.toList;

/**
 * IDaoMapper: 非mybatis mapper原子接口, 属于组装方法
 *
 * @author darui.wu
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public interface IRichMapper<E extends IEntity> extends IEntityMapper<E> {
    /**
     * 判断主键id记录是否已经存在
     * 只设置id，不添加默认值
     *
     * @param id 主键值
     * @return true: 记录存在; false: 记录不存在
     */
    default boolean existPk(Serializable id) {
        Column pk = Column.column(((IWrapperMapper<E>) this).primaryField(), null);
        IQuery query = ((IWrapperMapper<E>) this).query().where().apply(pk, EQ, id).end().limit(1);
        Integer count = this.count(query);
        return count != null && count > 0;
    }

    /**
     * <p>
     * 根据主键判断记录是否已经存在
     * o 是：更新记录
     * o 否：插入记录
     * </p>
     *
     * @param entity 实体对象
     * @return 更新或者插入成功
     */
    default boolean saveOrUpdate(E entity) {
        if (entity.findPk() == null) {
            return this.insert(entity) > 0;
        } else if (this.existPk(entity.findPk())) {
            return this.updateById(entity) > 0;
        } else {
            return this.insertWithPk(entity) > 0;
        }
    }

    /**
     * 将entity补充设置默认值后保存到数据库
     * 默认值设置{@link FluentMybatis#defaults()}, 具体定义继承 {@link IDefaultSetter#setInsertDefault(IEntity)}
     *
     * @param entity 实例
     * @param <PK>   主键
     * @return 主键
     */
    default <PK extends Serializable> PK save(E entity) {
        if (entity.findPk() == null) {
            this.insert(entity);
        } else {
            this.insertWithPk(entity);
        }
        return (PK) entity.findPk();
    }

    /**
     * 批量插入, 列表实例的主键必须全赋值，或者全不赋值
     * 默认值设置{@link FluentMybatis#defaults()}, 具体定义继承 {@link IDefaultSetter#setInsertDefault(IEntity)}
     *
     * @param list 实体对象列表
     * @return 插入记录数
     */
    default int save(Collection<E> list) {
        boolean hasPk = false;
        for (E entity : list) {
            if (entity.findPk() != null) {
                hasPk = true;
                break;
            }
        }
        if (hasPk) {
            return this.insertBatchWithPk(list);
        } else {
            return this.insertBatch(list);
        }
    }

    /**
     * 根据query查询满足条件的第一条记录，并根据mapFunction从map转换为Object实例
     *
     * @param query       查询条件
     * @param mapFunction 从map转换为Object实例
     * @param <POJO>      Object类型
     * @return Object实例
     */
    default <POJO> Optional<POJO> findOne(IQuery query, MapFunction<POJO> mapFunction) {
        Optional<Map<String, Object>> optional = this.findOneMap(query);
        return optional.map(m -> PoJoHelper.toPoJo(m, mapFunction));
    }

    /**
     * 根据query查询满足条件的第一条记录，并转换下划线转驼峰方式转换为PoJo实例
     *
     * @param clazz  PoJo类型
     * @param query  查询条件
     * @param <POJO> PoJo类型
     * @return PoJo实例
     */
    default <POJO> Optional<POJO> findOne(Class<POJO> clazz, IQuery query) {
        Optional<Map<String, Object>> optional = this.findOneMap(query);
        return optional.map(m -> PoJoHelper.toPoJo(clazz, m));
    }


    /**
     * 根据query查询满足条件的第一条记录
     * 当有多条记录符合条件时，只取第一条记录
     *
     * @param query 查询条件
     * @return 满足条件的一条记录
     */
    default Optional<Map<String, Object>> findOneMap(IQuery query) {
        List<Map<String, Object>> list = this.listMaps(query);
        if (list != null && list.size() > 1) {
            throw new RuntimeException("The expected result is one, but the returned result is multiple.");
        }
        Map<String, Object> map = list == null || list.size() == 0 ? null : list.get(0);
        return Optional.ofNullable(map);
    }

    /**
     * 根据query查询记录列表，并根据function将记录转换成需要的对象F
     *
     * @param query       查询条件
     * @param mapFunction 从Map记录对实体POJO的转换函数
     * @param <POJO>      POJO实体类型
     * @return POJO list
     */
    default <POJO> List<POJO> listPoJo(IQuery query, MapFunction<POJO> mapFunction) {
        List<Map<String, Object>> list = this.listMaps(query);
        return PoJoHelper.toPoJoList(list, mapFunction);
    }

    /**
     * 根据query查询记录列表, 并将数据结果转换PoJo对象
     * 转换规则是下划线转驼峰
     * 如果不符合这个规则, 请使用方法手动映射: listPoJos(IQuery query, MapFunction<POJO> mapFunction)
     *
     * @param clazz  PoJo对象类型
     * @param query  查询条件
     * @param <POJO> PoJo对象类型
     * @return PoJo列表
     */
    default <POJO> List<POJO> listPoJo(Class<POJO> clazz, IQuery query) {
        List<Map<String, Object>> list = this.listMaps(query);
        return PoJoHelper.toPoJoList(clazz, list);
    }

    /**
     * 根据where key值 + 和默认条件构造条件查询
     *
     * @param where 条件，忽略null值
     * @return 结果列表
     */
    default List<E> listByMapAndDefault(Map<String, Object> where) {
        IQuery query = ((IWrapperMapper<E>) this).defaultQuery().where().eqNotNull(where).end();
        return this.listEntity(query);
    }

    /**
     * 按标准分页查询实例列表
     *
     * @param query 查询条件
     * @return 标准分页查询结果
     */
    default StdPagedList<E> stdPagedEntity(IQuery query) {
        int total = this.countNoLimit(query);
        List list = this.listEntity(query);
        return new StdPagedList<>(total, list);
    }

    /**
     * 按标准分页查询数据（结果集为Map对象）
     *
     * @param query 查询条件
     * @return 按标准分页查询结果
     */
    default StdPagedList<Map<String, Object>> stdPagedMap(IQuery query) {
        int total = this.countNoLimit(query);
        List<Map<String, Object>> list = this.listMaps(query);
        return new StdPagedList<>(total, list);
    }

    /**
     * 按标准分页查询数据（结果集为按converter转换后的PoJo对象）
     *
     * @param query       查询条件
     * @param mapFunction 从Map记录对实体POJO的转换函数
     * @param <POJO>      PoJo实体类型
     * @return 按标准分页查询结果
     */
    default <POJO> StdPagedList<POJO> stdPagedPoJo(IQuery query, @NonNull MapFunction<POJO> mapFunction) {
        StdPagedList<Map<String, Object>> paged = this.stdPagedMap(query);
        if (paged == null || paged.getData() == null) {
            return (StdPagedList<POJO>) paged;
        } else {
            List<POJO> list = PoJoHelper.toPoJoList(paged.getData(), mapFunction);
            return new StdPagedList<>(paged.getTotal(), list);
        }
    }

    /**
     * 分页查询数据（结果集为PoJo对象, 查询结果按照下划线转驼峰规则）
     *
     * @param clazz  PoJo类型
     * @param query  查询条件
     * @param <POJO> PoJo类型
     * @return 分页查询结果
     */
    default <POJO> StdPagedList<POJO> stdPagedPoJo(Class<POJO> clazz, IQuery query) {
        StdPagedList<Map<String, Object>> paged = this.stdPagedMap(query);
        List<POJO> list = PoJoHelper.toPoJoList(clazz, paged.getData());
        return new StdPagedList<>(paged.getTotal(), list);
    }

    /**
     * 按tag>=next标识分页查询
     *
     * @param query 查询条件
     * @return 分页查询结果
     */
    default TagPagedList<E> tagPagedEntity(IQuery query) {
        int size = PoJoHelper.validateTagPaged(query);
        query.limit(size + 1);
        List<E> list = this.listEntity(query);
        E next = null;
        if (list.size() > size) {
            next = list.remove(size);
        }
        return new TagPagedList<>(list, next);
    }

    /**
     * 按tag>=next标识分页查询（结果集为Map对象）
     *
     * @param query 查询条件
     * @return 分页查询结果
     */
    default TagPagedList<Map<String, Object>> tagPagedMap(IQuery query) {
        int size = PoJoHelper.validateTagPaged(query);
        query.limit(size + 1);
        List list = this.listMaps(query);
        Map next = null;
        if (list.size() > size) {
            next = (Map) list.remove(size);
        }
        return new TagPagedList<>(list, next);
    }

    /**
     * 按tag>=next标识分页查询（结果集为PoJo对象）
     *
     * @param query       查询条件
     * @param mapFunction 从Map记录对实体POJO的转换函数
     * @param <POJO>      PoJo实体类型
     * @return 分页查询结果
     */
    default <POJO> TagPagedList<POJO> tagPagedPoJo(IQuery query, MapFunction<POJO> mapFunction) {
        TagPagedList<Map<String, Object>> paged = this.tagPagedMap(query);
        List<POJO> list = PoJoHelper.toPoJoList(paged.getData(), mapFunction);
        POJO next = PoJoHelper.toPoJo(paged.getNext(), mapFunction);
        return new TagPagedList<>(list, next);
    }

    /**
     * 按tag>=next标识分页查询（结果集为PoJo对象）
     *
     * @param clazz  PoJo类型
     * @param query  查询条件
     * @param <POJO> PoJo类型
     * @return 分页查询结果
     */
    default <POJO> TagPagedList<POJO> tagPagedPoJo(Class<POJO> clazz, IQuery query) {
        TagPagedList<Map<String, Object>> paged = this.tagPagedMap(query);
        List<POJO> list = PoJoHelper.toPoJoList(clazz, paged.getData());
        POJO next = PoJoHelper.toPoJo(clazz, paged.getNext());
        return new TagPagedList<>(list, next);
    }

    /**
     * 根据where key值 + 和默认条件删除数据
     *
     * @param where k-v条件
     * @return 删除记录数
     */
    default int deleteByMapAndDefault(Map<String, Object> where) {
        IQuery query = ((IWrapperMapper<E>) this).defaultQuery().where().eqNotNull(where).end();
        return this.delete(query);
    }

    /**
     * 根据where key值 + 和默认条件删除数据
     *
     * @param where k-v条件
     * @return 逻辑删除记录数
     */
    default int logicDeleteByMapAndDefault(Map<String, Object> where) {
        IQuery query = ((IWrapperMapper<E>) this).defaultQuery().where().eqNotNull(where).end();
        return this.logicDelete(query);
    }

    /**
     * 根据entities中的id值，批量删除记录
     *
     * @param entities 实例列表
     * @return 被执行的记录数
     */
    default int deleteByEntityIds(Collection<E> entities) {
        List<Serializable> ids = entities.stream()
            .map(IEntity::findPk)
            .collect(toList());
        return this.deleteByIds(ids);
    }

    /**
     * 根据entities中的id值，批量逻辑删除记录
     *
     * @param entities 实例列表
     * @return 被执行的记录数
     */
    default int logicDeleteByEntityIds(Collection<E> entities) {
        List<Serializable> ids = entities.stream()
            .map(IEntity::findPk)
            .collect(toList());
        return this.logicDeleteByIds(ids);
    }

    /**
     * 根据entities中的id值，批量删除记录
     *
     * @param entities 实例列表
     * @return 被执行的记录数
     */
    default int deleteByEntityIds(E... entities) {
        return this.deleteByEntityIds(Arrays.asList(entities));
    }

    /**
     * 根据entities中的id值，批量删除记录
     *
     * @param entities 实例列表
     * @return 被执行的记录数
     */
    default int logicDeleteByEntityIds(E... entities) {
        return this.logicDeleteByEntityIds(Arrays.asList(entities));
    }
}