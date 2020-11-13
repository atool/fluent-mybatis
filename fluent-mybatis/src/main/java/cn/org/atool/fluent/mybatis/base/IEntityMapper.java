package cn.org.atool.fluent.mybatis.base;

import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static cn.org.atool.fluent.mybatis.mapper.FluentConst.*;

/**
 * IEntityMapper: 实例Mapper基类，Mapper 继承该接口后，无需编写 mapper.xml 文件，即可获得CRUD功能
 *
 * @Author darui.wu
 * @Date 2019-06-25 14:00
 */
public interface IEntityMapper<E extends IEntity> extends IMapper {
    /**
     * 插入一条记录
     * 和 {@link IDaoMapper#save(IEntity)}区别，insert方法不会根据{@link IDefaultSetter#setInsertDefault(IEntity)}设置默认值
     *
     * @param entity
     * @return
     */
    int insert(E entity);

    /**
     * 批量插入数据，实例的主键必须全部赋值
     *
     * @param entities
     * @return
     */
    int insertBatch(List<E> entities);

    /**
     * 根据id删除记录
     *
     * @param id
     * @return
     */
    int deleteById(Serializable id);

    /**
     * 根据 columnMap key值删除记录
     *
     * @param cm
     * @return
     */
    int deleteByMap(@Param(Param_CM) Map<String, Object> cm);

    /**
     * 根据wrapper删除记录
     *
     * @param wrapper 实体对象封装操作类（属性条件可以为null）
     * @return
     */
    int delete(@Param(Param_EW) IQuery wrapper);

    /**
     * 根据id列表批量删除
     *
     * @param idList id列表（值不能为null或者empty）
     * @return
     */
    int deleteByIds(@Param(Param_Coll) Collection<? extends Serializable> idList);

    /**
     * 根据id修改
     *
     * @param entity 实体对象
     * @return
     */
    int updateById(@Param(Param_ET) E entity);

    /**
     * 根据update对象更新记录
     *
     * @param update
     * @return
     */
    int updateBy(@Param(Param_EW) IUpdate update);

    /**
     * 根据 ID 查询
     *
     * @param id 主键ID
     * @return
     */
    E findById(Serializable id);

    /**
     * 根据 query 条件，查询一条记录
     *
     * @param query 实体对象封装操作类（可以为 null）
     * @return
     */
    E findOne(@Param(Param_EW) IQuery query);

    /**
     * 查询（根据ID 批量查询）
     *
     * @param ids 主键ID列表(不能为 null 以及 empty)
     * @return
     */
    List<E> listByIds(@Param(Param_Coll) Collection ids);

    /**
     * 查询（根据 columnMap 条件）
     *
     * @param columnMap 表字段 map 对象
     * @return
     */
    List<E> listByMap(@Param(Param_CM) Map<String, Object> columnMap);

    /**
     * 根据 query 条件，查询全部记录
     *
     * @param query 实体对象封装操作类（可以为 null）
     * @return
     */
    List<E> listEntity(@Param(Param_EW) IQuery query);

    /**
     * 根据 query 条件，查询全部记录
     *
     * @param query 实体对象封装操作类（可以为 null）
     * @return map列表
     */
    List<Map<String, Object>> listMaps(@Param(Param_EW) IQuery query);

    /**
     * <p>
     * 根据 query 条件，查询全部记录
     * 注意： 只返回第一个字段的值
     * </p>
     *
     * @param query 实体对象封装操作类（可以为 null）
     * @return
     */
    <O> List<O> listObjs(@Param(Param_EW) IQuery query);

    /**
     * 根据 query 条件，查询总记录数
     *
     * @param query 实体对象封装操作类（可以为 null）
     * @return
     */
    Integer count(@Param(Param_EW) IQuery query);

    /**
     * 根据 query 条件(如果有pageOffset, 去掉pageOffset限制部分)，查询总记录数
     *
     * @param query 实体对象封装操作类（可以为 null）
     * @return
     */
    Integer countNoLimit(@Param(Param_EW) IQuery query);
}