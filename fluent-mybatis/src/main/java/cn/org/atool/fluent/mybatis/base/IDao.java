package cn.org.atool.fluent.mybatis.base;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * IBaseDao Dao基本操作方法
 *
 * @param <E> 实体类
 * @Author darui.wu
 * @Date 2019-06-25 12:00
 */
public interface IDao<E extends IEntity> {
    /**
     * 插入一条记录
     *
     * @param entity 实体对象
     * @param <PK>   主键类型
     * @return 返回记录主键
     */
    <PK extends Serializable> PK save(E entity);

    /**
     * 批量插入
     * 列表实例的主键必须全赋值，或者全不赋值
     *
     * @param list 实体对象列表
     * @return 插入记录数
     */
    int save(List<E> list);

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
    boolean saveOrUpdate(E entity);

    /**
     * 根据id查询
     *
     * @param id 主键值
     * @return 结果对象
     */
    E selectById(Serializable id);

    /**
     * 根据id列表查询
     *
     * @param ids 主键列表
     * @return 结果列表
     */
    List<E> selectByIds(Collection<? extends Serializable> ids);

    /**
     * 根据where key值构造条件查询
     *
     * @param where 条件，忽略null值
     * @return 结果列表
     */
    List<E> selectByMap(Map<String, Object> where);

    /**
     * 判断主键id记录是否已经存在
     *
     * @param id 主键值
     * @return true: 记录存在; false: 记录不存在
     */
    boolean existPk(Serializable id);

    /**
     * 根据entity的主键修改entity中非null属性
     *
     * @param entity 实体对象
     * @return 是否更新成功
     */
    boolean updateById(E entity);

    /**
     * 根据entities中的id值，批量删除记录
     *
     * @param entities
     * @return 被执行的记录数
     */
    int deleteByEntityIds(Collection<E> entities);

    /**
     * 根据ids列表批量删除记录
     *
     * @param ids 主键列表
     * @return 被执行的记录数
     */
    int deleteByIds(Collection<? extends Serializable> ids);

    /**
     * 根据id删除记录
     *
     * @param id 主键值
     * @return 是否删除成功
     */
    boolean deleteById(Serializable id);

    /**
     * 根据map构造条件删除记录
     *
     * @param map 条件, 忽略null值
     * @return 被执行的记录数
     */
    int deleteByMap(Map<String, Object> map);
}