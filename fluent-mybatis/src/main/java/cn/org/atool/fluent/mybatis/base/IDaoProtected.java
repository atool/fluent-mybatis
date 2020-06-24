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
public interface IDaoProtected<E extends IEntity, Q extends IQuery<E, Q>, U extends IUpdate<E, U, Q>> {
    /**
     * 根据条件query删除记录
     *
     * @param query
     * @return
     */
    int deleteByQuery(IQuery query);

    /**
     * 根据update设置更新记录
     *
     * @param update
     * @return
     */
    int update(IUpdate update);

    /**
     * 返回符合条件的记录数
     *
     * @param query
     * @return
     */
    int count(IQuery query);

    /**
     * 根据query查询记录列表, 返回Map对象列表
     *
     * @param query 查询条件
     * @return map list
     */
    List<Map> selectObjs(IQuery query);

    /**
     * 根据query查询记录列表，并根据function将记录转换成需要的对象F
     *
     * @param query    查询条件
     * @param function 从Map记录对实体F的转换函数
     * @param <F>
     * @return object list
     */
    <F> List<F> selectObjs(IQuery query, Function<Map<String, Object>, F> function);

    /**
     * 根据query查询满足条件的第一条记录，并根据function解析出对应字段
     *
     * @param query
     * @param function 获取entity字段值函数
     * @param <F>
     * @return
     */
    <F> F selectOne(IQuery query, Function<E, F> function);

    /**
     * 根据query查询记录列表，并根据function解析出对应字段
     *
     * @param query
     * @param function 获取entity字段值函数
     * @param <F>
     * @return
     */
    <F> List<F> selectFields(IQuery query, Function<E, F> function);

    /**
     * 根据query查询满足条件的第一条记录
     *
     * @param query
     * @return
     */
    E selectOne(IQuery query);

    /**
     * 分页查询
     *
     * @param query 查询条件
     * @return 分页查询结果
     */
    PagedList<E> selectPagedList(IQuery query);

    /**
     * 分页查询
     *
     * @param query 查询条件
     * @return 分页查询结果
     */
    MarkerList<E> selectMarkerList(IQuery query);

    /**
     * 根据query查询记录
     *
     * @param query
     * @return
     */
    List<E> selectList(IQuery query);

    PagedList<Map> selectPagedMaps(IQuery query);

    MarkerList<Map> selectMarkerMaps(IQuery query);
}