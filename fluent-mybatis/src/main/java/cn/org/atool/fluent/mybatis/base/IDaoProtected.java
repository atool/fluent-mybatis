package cn.org.atool.fluent.mybatis.base;

import cn.org.atool.fluent.mybatis.base.model.TagList;
import cn.org.atool.fluent.mybatis.base.model.PagedList;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * IDaoProtected: 被限制在Dao基类中使用的方法
 *
 * @param <E> 实体类
 * @author:darui.wu Created by darui.wu on 2020/6/24.
 */
public interface IDaoProtected<E extends IEntity> {
    /**
     * 根据条件query删除记录
     *
     * @param query 条件
     * @return 删除记录数
     */
    int deleteBy(IQuery query);

    /**
     * 根据update设置更新记录
     *
     * @param update 更新条件
     * @return 更新成功记录数
     */
    int updateBy(IUpdate update);

    /**
     * 根据query查询对应实例列表
     *
     * @param query 查询条件
     * @return 实例列表
     */
    List<E> listEntity(IQuery query);

    /**
     * 根据query查询记录列表, 返回Map对象列表
     *
     * @param query 查询条件
     * @return map list
     */
    List<Map<String, Object>> listMaps(IQuery query);

    /**
     * 根据query查询记录列表，并根据function将记录转换成需要的对象F
     *
     * @param query     查询条件
     * @param converter 从Map记录对实体POJO的转换函数
     * @param <POJO>    POJO实体类型
     * @return POJO list
     */
    <POJO> List<POJO> listPoJos(IQuery query, Function<Map<String, Object>, POJO> converter);

    /**
     * 根据query查询记录列表, 并将数据结果转换PoJo对象
     * 转换规则是下划线转驼峰
     * 如果不符合这个规则, 请使用方法手动映射: listPoJos(IQuery query, Function<Map<String, Object>, POJO> converter)
     *
     * @param clazz  PoJo对象类型
     * @param query  查询条件
     * @param <POJO> PoJo对象类型
     * @return PoJo列表
     */
    <POJO> List<POJO> listPoJos(Class<POJO> clazz, IQuery query);

    /**
     * 分页查询实例
     *
     * @param query 查询条件
     * @return 分页查询结果
     */
    PagedList<E> pagedEntity(IQuery query);

    /**
     * 分页查询数据（结果集为Map对象）
     *
     * @param query 查询条件
     * @return 分页查询结果
     */
    PagedList<Map<String, Object>> pagedMaps(IQuery query);

    /**
     * 分页查询数据（结果集为Object对象）
     *
     * @param query     查询条件
     * @param converter 从Map记录对实体POJO的转换函数
     * @param <POJO>    Object实体类型
     * @return 分页查询结果
     */
    <POJO> PagedList<POJO> pagedPoJos(IQuery query, Function<Map<String, Object>, POJO> converter);

    /**
     * 分页查询数据（结果集为Object对象）
     *
     * @param klass  Object类型
     * @param query  查询条件
     * @param <POJO> Object类型
     * @return 分页查询结果
     */
    <POJO> PagedList<POJO> pagedPoJos(Class<POJO> klass, IQuery query);

    /**
     * 按Marker标识分页查询
     *
     * @param query 查询条件
     * @return 分页查询结果
     */
    TagList<E> tagPagedEntity(IQuery query);

    /**
     * 按Marker标识分页查询（结果集为Map对象）
     *
     * @param query 查询条件
     * @return 分页查询结果
     */
    TagList<Map<String, Object>> tagPagedMaps(IQuery query);

    /**
     * 按Marker标识分页查询（结果集为Object对象）
     *
     * @param query     查询条件
     * @param converter 从Map记录对实体POJO的转换函数
     * @param <POJO>    Object实体类型
     * @return 分页查询结果
     */
    <POJO> TagList<POJO> tagPagedPoJos(IQuery query, Function<Map<String, Object>, POJO> converter);

    /**
     * 按Marker标识分页查询（结果集为Object对象）
     *
     * @param klass  Object类型
     * @param query  查询条件
     * @param <POJO> Object类型
     * @return 分页查询结果
     */
    <POJO> TagList<POJO> tagPagedPoJos(Class<POJO> klass, IQuery query);

    /**
     * 根据query查询满足条件的第一条记录
     * 当有多条记录符合条件时，只取第一条记录
     *
     * @param query 查询条件
     * @return 满足条件的一条记录
     */
    Optional<E> findOne(IQuery query);

    /**
     * 根据query查询满足条件的第一条记录
     * 当有多条记录符合条件时，只取第一条记录
     *
     * @param query 查询条件
     * @return 满足条件的一条记录
     */
    Optional<Map<String, Object>> findOneResult(IQuery query);

    /**
     * 根据query查询满足条件的第一条记录，并根据converter从map转换为Object实例
     *
     * @param query     查询条件
     * @param converter 从map转换为Object实例
     * @param <POJO>    Object类型
     * @return Object实例
     */
    <POJO> Optional<POJO> findOnePoJo(IQuery query, Function<Map<String, Object>, POJO> converter);

    /**
     * 根据query查询满足条件的第一条记录，并转换为Object实例
     *
     * @param klass  Object类型
     * @param query  查询条件
     * @param <POJO> Object类型
     * @return Object实例
     */
    <POJO> Optional<POJO> findOnePoJo(Class<POJO> klass, IQuery query);

    /**
     * 返回符合条件的记录数
     *
     * @param query 查询条件
     * @return 符合条件的记录数
     */
    int count(IQuery query);
}