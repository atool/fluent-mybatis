package cn.org.atool.fluent.mybatis.base;

import cn.org.atool.fluent.mybatis.model.StdPagedList;
import cn.org.atool.fluent.mybatis.model.TagPagedList;
import cn.org.atool.fluent.mybatis.functions.MapFunction;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * IDaoProtected: 被限制在Dao基类中使用的方法, 只允许在子类中调用，不暴露给更高层的Service外部直接访问
 * * <p>
 * * ！！！！！！！！！！！！
 * * 理由：
 * * 不希望在更高层Service中直接构造Query, Update等具体查询条件, 希望对外接口是语义化的
 *
 * @param <E> 实体类
 * @author:darui.wu Created by darui.wu on 2020/6/24.
 */
public interface IDao<E extends IEntity> extends IMapperDao<E> {
    /**
     * 根据条件query删除记录
     *
     * @param query 条件
     * @return 删除记录数
     */
    default int deleteBy(IQuery query) {
        return this.mapper().delete(query);
    }

    /**
     * 根据update设置更新记录
     *
     * @param update 更新条件
     * @return 更新成功记录数
     */
    default int updateBy(IUpdate update) {
        return this.mapper().updateBy(update);
    }

    /**
     * 根据query查询对应实例列表
     *
     * @param query 查询条件
     * @return 实例列表
     */
    default List<E> listEntity(IQuery query) {
        return this.mapper().listEntity(query);
    }

    /**
     * 根据query查询记录列表, 返回Map对象列表
     *
     * @param query 查询条件
     * @return map list
     */
    default List<Map<String, Object>> listMaps(IQuery query) {
        return this.mapper().listMaps(query);
    }

    /**
     * 根据query查询记录列表，并根据function将记录转换成需要的对象
     *
     * @param query       查询条件
     * @param mapFunction 从Map记录对实体POJO的转换函数
     * @param <POJO>      POJO实体类型
     * @return POJO list
     */
    default <POJO> List<POJO> listPoJos(IQuery query, MapFunction<POJO> mapFunction) {
        return this.mapper().listPoJo(query, mapFunction);
    }

    /**
     * 根据query查询记录列表, 并将数据结果转换PoJo对象
     * 转换规则是下划线转驼峰
     * 如果不符合这个规则, 请使用方法手动映射{@link #listPoJos(IQuery, MapFunction)}
     *
     * @param clazz  PoJo对象类型
     * @param query  查询条件
     * @param <POJO> PoJo对象类型
     * @return PoJo列表
     */
    default <POJO> List<POJO> listPoJos(Class<POJO> clazz, IQuery query) {
        return this.mapper().listPoJo(clazz, query);
    }

    /**
     * 分页查询实例
     *
     * @param query 查询条件
     * @return 分页查询结果
     */
    default StdPagedList<E> stdPagedEntity(IQuery query) {
        return this.mapper().stdPagedEntity(query);
    }

    /**
     * 分页查询数据（结果集为Map对象）
     *
     * @param query 查询条件
     * @return 分页查询结果
     */
    default StdPagedList<Map<String, Object>> stdPagedMap(IQuery query) {
        return this.mapper().stdPagedMap(query);
    }

    /**
     * 分页查询数据（结果集为Object对象）
     *
     * @param query       查询条件
     * @param mapFunction 从Map记录对实体POJO的转换函数
     * @param <POJO>      Object实体类型
     * @return 分页查询结果
     */
    default <POJO> StdPagedList<POJO> stdPagedPoJo(IQuery query, MapFunction<POJO> mapFunction) {
        return this.mapper().stdPagedPoJo(query, mapFunction);
    }

    /**
     * 分页查询数据（（结果集为PoJo对象, 查询结果按照下划线转驼峰规则）
     *
     * @param clazz  Object类型
     * @param query  查询条件
     * @param <POJO> Object类型
     * @return 分页查询结果
     */
    default <POJO> StdPagedList<POJO> stdPagedPoJo(Class<POJO> clazz, IQuery query) {
        return this.mapper().stdPagedPoJo(clazz, query);
    }

    /**
     * 按Marker标识分页查询
     *
     * @param query 查询条件
     * @return 分页查询结果
     */
    default TagPagedList<E> tagPagedEntity(IQuery query) {
        return this.mapper().tagPagedEntity(query);
    }

    /**
     * 按Marker标识分页查询（结果集为Map对象）
     *
     * @param query 查询条件
     * @return 分页查询结果
     */
    default TagPagedList<Map<String, Object>> tagPagedMap(IQuery query) {
        return this.mapper().tagPagedMap(query);
    }

    /**
     * 按Marker标识分页查询（结果集为Object对象）
     *
     * @param query       查询条件
     * @param mapFunction 从Map记录对实体POJO的转换函数
     * @param <POJO>      Object实体类型
     * @return 分页查询结果
     */
    default <POJO> TagPagedList<POJO> tagPagedPoJo(IQuery query, MapFunction<POJO> mapFunction) {
        return this.mapper().tagPagedPoJo(query, mapFunction);
    }

    /**
     * 按Marker标识分页查询（结果集为Object对象）
     *
     * @param clazz  Object类型
     * @param query  查询条件
     * @param <POJO> Object类型
     * @return 分页查询结果
     */
    default <POJO> TagPagedList<POJO> tagPagedPoJo(Class<POJO> clazz, IQuery query) {
        return this.mapper().tagPagedPoJo(clazz, query);
    }

    /**
     * 根据query查询满足条件的第一条记录
     * 当有多条记录符合条件时，只取第一条记录
     *
     * @param query 查询条件
     * @return 满足条件的一条记录
     */
    default Optional<E> findOne(IQuery query) {
        E one = this.mapper().findOne(query);
        return Optional.ofNullable(one);
    }

    /**
     * 根据query查询满足条件的第一条记录
     * 当有多条记录符合条件时，只取第一条记录
     *
     * @param query 查询条件
     * @return 满足条件的一条记录
     */
    default Optional<Map<String, Object>> findOneMap(IQuery query) {
        return this.mapper().findOneMap(query);
    }

    /**
     * 根据query查询满足条件的第一条记录，并根据converter从map转换为Object实例
     *
     * @param query       查询条件
     * @param mapFunction 从map转换为Object实例
     * @param <POJO>      Object类型
     * @return Object实例
     */
    default <POJO> Optional<POJO> findOne(IQuery query, MapFunction<POJO> mapFunction) {
        return this.mapper().findOne(query, mapFunction);
    }

    /**
     * 根据query查询满足条件的第一条记录，并转换为Object实例
     *
     * @param clazz  Object类型
     * @param query  查询条件
     * @param <POJO> Object类型
     * @return Object实例
     */
    default <POJO> Optional<POJO> findOne(Class<POJO> clazz, IQuery query) {
        return this.mapper().findOne(clazz, query);
    }

    /**
     * 返回符合条件的记录数
     *
     * @param query 查询条件
     * @return 符合条件的记录数
     */
    default int count(IQuery query) {
        return this.mapper().count(query);
    }
}