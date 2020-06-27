package cn.org.atool.fluent.mybatis.base;

import cn.org.atool.fluent.mybatis.base.model.MarkerList;
import cn.org.atool.fluent.mybatis.base.model.PagedList;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * IDaoProtected: 被限制在Dao基类中使用的方法
 *
 * @param <E> 实体类
 * @param <Q> 查询器
 * @param <U> 更新器
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
     * 根据query查询对应实例列表
     *
     * @param query     查询条件
     * @param converter 从Entity对象转换为POJO对象
     * @param <POJO>    POJO实体类型
     * @return POJO list
     */
    <POJO> List<POJO> listObjs(IQuery query, Function<E, POJO> converter);

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
     * 分页查询数据（结果集为PoJo对象）
     *
     * @param query     查询条件
     * @param converter 从Map记录对实体POJO的转换函数
     * @param <POJO>    POJO实体类型
     * @return 分页查询结果
     */
    <POJO> PagedList<POJO> pagedPoJos(IQuery query, Function<Map<String, Object>, POJO> converter);

    /**
     * 按Marker标识分页查询
     *
     * @param query 查询条件
     * @return 分页查询结果
     */
    MarkerList<E> markerPagedEntity(IQuery query);

    /**
     * 按Marker标识分页查询（结果集为Map对象）
     *
     * @param query 查询条件
     * @return 分页查询结果
     */
    MarkerList<Map<String, Object>> markerPagedMaps(IQuery query);

    /**
     * 按Marker标识分页查询（结果集为PoJo对象）
     *
     * @param query     查询条件
     * @param converter 从Map记录对实体POJO的转换函数
     * @param <POJO>    POJO实体类型
     * @return 分页查询结果
     */
    <POJO> MarkerList<POJO> markerPagedPoJos(IQuery query, Function<Map<String, Object>, POJO> converter);

    /**
     * 根据query查询满足条件的第一条记录
     * 当有多条记录符合条件时，只取第一条记录
     *
     * @param query 查询条件
     * @return 满足条件的一条记录
     */
    E findOne(IQuery query);

    /**
     * 根据query查询满足条件的第一条记录，并根据function解析出对应字段
     *
     * @param query     查询条件
     * @param converter 获取entity字段值函数
     * @param <F>
     * @return
     */
    <F> F findOne(IQuery query, Function<E, F> converter);

    /**
     * 返回符合条件的记录数
     *
     * @param query 查询条件
     * @return 符合条件的记录数
     */
    int count(IQuery query);
}