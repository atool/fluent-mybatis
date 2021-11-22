package cn.org.atool.fluent.mybatis.base;

import cn.org.atool.fluent.mybatis.base.mapper.IRichMapper;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * IBaseDao Dao基本操作方法
 *
 * @param <E> 实体类
 * @author darui.wu 2019-06-25 12:00
 */
@SuppressWarnings({"rawtypes", "unchecked", "UnusedReturnValue"})
public interface IBaseDao<E extends IEntity> {
    /**
     * 插入一条记录
     *
     * @param entity 实体对象
     * @param <PK>   主键类型
     * @return 返回记录主键
     */
    default <PK> PK save(E entity) {
        return (PK) this.mapper().save(entity);
    }

    /**
     * 批量插入
     * 列表实例的主键必须全赋值，或者全不赋值
     *
     * @param list 实体对象列表
     * @return 插入记录数
     */
    default int save(Collection<E> list) {
        return this.mapper().save(list);
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
        return this.mapper().saveOrUpdate(entity);
    }

    /**
     * 根据id查询
     *
     * @param id 主键值
     * @return 结果对象
     */
    default E selectById(Object id) {
        Object obj = this.mapper().findById(id);
        return (E) obj;
    }

    /**
     * 根据id列表查询
     *
     * @param ids 主键列表
     * @return 对象列表
     */
    default List<E> selectByIds(Object... ids) {
        return (List<E>) this.mapper().listByIds(Arrays.asList(ids));
    }

    /**
     * 根据id列表查询
     *
     * @param ids 主键列表
     * @return 结果列表
     */
    default List<E> selectByIds(Collection ids) {
        return (List<E>) this.mapper().listByIds(ids);
    }

    /**
     * 根据where key值构造条件查询
     *
     * @param where 条件，忽略null值
     * @return 结果列表
     */
    default List<E> selectByMap(Map<String, Object> where) {
        return (List<E>) this.mapper().listByMapAndDefault(where);
    }

    /**
     * 判断主键id记录是否已经存在
     *
     * @param id 主键值
     * @return true: 记录存在; false: 记录不存在
     */
    default boolean existPk(Object id) {
        return this.mapper().existPk(id);
    }

    /**
     * 根据entity的主键修改entity中非null属性
     *
     * @param entity 实体对象
     * @return 是否更新成功
     */
    default boolean updateById(E entity) {
        return this.mapper().updateById(entity) > 0;
    }

    /**
     * 根据entity的主键批量修改entity中非null属性
     *
     * @param entities 实体对象列表
     * @return 是否更新成功
     */
    boolean updateEntityByIds(E... entities);

    /**
     * 根据entity的主键批量修改entity中非null属性
     *
     * @param entities 实体对象列表
     * @return 是否更新成功
     */
    default boolean updateEntityByIds(Collection<E> entities) {
        return this.updateEntityByIds((E[]) entities.toArray(new IEntity[0]));
    }

    /**
     * 根据whereNoN非空属性作为相等条件, 更新updateNoN非空属性字段
     *
     * @param updateNoN k-v更新字段
     * @param whereNoN  k-v更新条件
     * @return 更新记录数
     */
    int updateBy(E updateNoN, E whereNoN);

    /**
     * 根据entities中的id值，批量删除记录
     *
     * @param entities 删除实例列表
     * @return 被执行的记录数
     */
    default int deleteByEntityIds(Collection<E> entities) {
        return this.mapper().deleteByEntityIds(entities);
    }

    /**
     * 根据entities中的id值，批量删除记录
     *
     * @param entities 逻辑删除实例
     * @return 被执行的记录数
     */
    default int logicDeleteByEntityIds(Collection<E> entities) {
        return this.mapper().logicDeleteByEntityIds(entities);
    }

    /**
     * 根据entities中的id值，批量删除记录
     *
     * @param entities 逻辑删除实例
     * @return 被执行的记录数
     */
    default int deleteByEntityIds(E... entities) {
        return this.mapper().deleteByEntityIds(entities);
    }

    /**
     * 根据entities中的id值，批量逻辑删除记录
     *
     * @param entities 逻辑删除实例
     * @return 被执行的记录数
     */
    default int logicDeleteByEntityIds(E... entities) {
        return this.mapper().logicDeleteByEntityIds(entities);
    }

    /**
     * 根据ids列表批量删除记录
     *
     * @param ids 主键列表
     * @return 被执行的记录数
     */
    default int deleteByIds(Collection ids) {
        return this.mapper().deleteByIds(ids);
    }

    /**
     * 根据ids列表批量逻辑删除记录
     *
     * @param ids 主键列表
     * @return 被执行的记录数
     */
    default int logicDeleteByIds(Collection ids) {
        return this.mapper().logicDeleteByIds(ids);
    }

    /**
     * 根据id删除记录
     *
     * @param ids 主键值
     * @return 是否删除成功
     */
    default boolean deleteById(Object... ids) {
        int count = this.mapper().deleteById(ids);
        return count > 0;
    }

    /**
     * 根据id列表逻辑删除记录
     *
     * @param ids 主键值
     * @return 是否删除成功
     */
    default boolean logicDeleteById(Object... ids) {
        int count = this.mapper().logicDeleteById(ids);
        return count > 0;
    }

    /**
     * 根据map构造条件删除记录
     *
     * @param map 条件, 忽略null值
     * @return 被执行的记录数
     */
    default int deleteByMap(Map<String, Object> map) {
        return this.mapper().deleteByMapAndDefault(map);
    }


    /**
     * 根据map构造条件逻辑删除记录
     *
     * @param map 条件, 忽略null值
     * @return 被执行的记录数
     */
    default int logicDeleteByMap(Map<String, Object> map) {
        return this.mapper().logicDeleteByMapAndDefault(map);
    }

    /**
     * 获取对应entity的BaseMapper
     *
     * @return IRichMapper
     */
    IRichMapper mapper();
}