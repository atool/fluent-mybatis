package cn.org.atool.fluent.mybatis.interfaces;

import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static cn.org.atool.fluent.mybatis.method.model.XmlConstant.*;

/**
 * IEntityMapper: 实例Mapper基类，Mapper 继承该接口后，无需编写 mapper.xml 文件，即可获得CRUD功能
 *
 * @Author darui.wu
 * @Date 2019-06-25 14:00
 */
public interface IEntityMapper<T> extends IMapper {
    /**
     * 插入一条记录
     *
     * @param entity
     * @return
     */
    int insert(T entity);

    /**
     * 批量插入数据，实例的主键必须全部赋值或全部未赋值
     *
     * @param entities
     * @return
     */
    int insertBatch(List<T> entities);

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
     * @param columnMap
     * @return
     */
    int deleteByMap(@Param(COLUMN_MAP) Map<String, Object> columnMap);

    /**
     * 根据wrapper删除记录
     *
     * @param wrapper 实体对象封装操作类（属性条件可以为null）
     * @return
     */
    int delete(@Param(WRAPPER) IQuery wrapper);

    /**
     * 根据id列表批量删除
     *
     * @param idList id列表（值不能为null或者empty）
     * @return
     */
    int deleteByIds(@Param(COLLECTION) Collection<? extends Serializable> idList);

    /**
     * 根据id修改
     *
     * @param entity 实体对象
     * @return
     */
    int updateById(@Param(ENTITY) T entity);

    /**
     * 根据update对象更新记录
     *
     * @param update
     * @return
     */
    int updateBy(@Param(WRAPPER) IUpdate update);

    /**
     * 根据 ID 查询
     *
     * @param id 主键ID
     * @return
     */
    T selectById(Serializable id);

    /**
     * 查询（根据ID 批量查询）
     *
     * @param idList 主键ID列表(不能为 null 以及 empty)
     * @return
     */
    List<T> selectByIds(@Param(COLLECTION) Collection<? extends Serializable> idList);

    /**
     * 查询（根据 columnMap 条件）
     *
     * @param columnMap 表字段 map 对象
     * @return
     */
    List<T> selectByMap(@Param(COLUMN_MAP) Map<String, Object> columnMap);

    /**
     * 根据 query 条件，查询一条记录
     *
     * @param query 实体对象封装操作类（可以为 null）
     * @return
     */
    T selectOne(@Param(WRAPPER) IQuery query);

    /**
     * 根据 query 条件，查询总记录数
     *
     * @param query 实体对象封装操作类（可以为 null）
     * @return
     */
    Integer selectCount(@Param(WRAPPER) IQuery query);

    /**
     * 根据 query 条件(如果有pageOffset, 去掉pageOffset限制部分)，查询总记录数
     *
     * @param query 实体对象封装操作类（可以为 null）
     * @return
     */
    Integer selectCountNoLimit(@Param(WRAPPER) IQuery query);

    /**
     * 根据 query 条件，查询全部记录
     *
     * @param query 实体对象封装操作类（可以为 null）
     * @return
     */
    List<T> selectList(@Param(WRAPPER) IQuery query);

    /**
     * 根据 query 条件，查询全部记录
     *
     * @param query 实体对象封装操作类（可以为 null）
     * @return
     */
    List<Map<String, Object>> selectMaps(@Param(WRAPPER) IQuery query);

    /**
     * <p>
     * 根据 query 条件，查询全部记录
     * 注意： 只返回第一个字段的值
     * </p>
     *
     * @param query 实体对象封装操作类（可以为 null）
     * @return
     */
    <O> List<O> selectObjs(@Param(WRAPPER) IQuery query);
}