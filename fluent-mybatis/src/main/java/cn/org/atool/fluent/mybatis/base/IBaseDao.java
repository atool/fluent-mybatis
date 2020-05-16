package cn.org.atool.fluent.mybatis.base;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @param <E>
 * @ClassName IBaseDao
 * @Description BaseDao基本操作方法定义
 * @Author darui.wu
 * @Date 2019-06-25 12:00
 */
public interface IBaseDao<E extends IEntity> {
    /**
     * 插入一条记录
     *
     * @param entity
     * @param <PK>
     * @return 返回记录主键
     */
    <PK extends Serializable> PK save(E entity);

    /**
     * 批量插入
     *
     * @param list
     * @return 插入记录数
     */
    int saveWithPk(List<E> list);

    /**
     * <p>
     * 根据主键判断记录是否已经存在
     * o 是：更新记录
     * o 否：插入记录
     * </p>
     *
     * @param entity
     * @return 更新或者插入成功
     */
    boolean saveOrUpdate(E entity);

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    E selectById(Serializable id);

    /**
     * 根据id列表查询
     *
     * @param ids
     * @return
     */
    List<E> selectByIds(Collection<? extends Serializable> ids);

    /**
     * 根据where key值构造条件查询
     *
     * @param where
     * @return
     */
    List<E> selectByMap(Map<String, Object> where);

    /**
     * 判断主键id记录是否已经存在
     *
     * @param id
     * @return
     */
    boolean existPk(Serializable id);

    /**
     * 根据entity的主键修改entity中非null属性
     *
     * @param entity
     * @return
     */
    boolean updateById(E entity);

    /**
     * 根据entities中的id值，批量删除记录
     *
     * @param entities
     * @return
     */
    int deleteByEntityIds(Collection<E> entities);

    /**
     * 根据ids列表批量删除记录
     *
     * @param ids
     * @return
     */
    int deleteByIds(Collection<? extends Serializable> ids);

    /**
     * 根据id删除记录
     *
     * @param id
     * @return
     */
    boolean deleteById(Serializable id);

    /**
     * 根据map构造条件删除记录
     *
     * @param map
     * @return
     */
    int deleteByMap(Map<String, Object> map);
}
