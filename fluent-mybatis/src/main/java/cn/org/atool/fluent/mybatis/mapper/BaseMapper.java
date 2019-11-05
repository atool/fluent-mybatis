package cn.org.atool.fluent.mybatis.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @ClassName BaseMapper
 * @Description Mapper方法基础接口类, Mapper 继承该接口后，无需编写 mapper.xml 文件，即可获得CRUD功能
 * @Author darui.wu
 * @Date 2019-06-25 14:00
 */
public interface BaseMapper<T> {
    /**
     * 插入一条记录
     *
     * @param entity
     * @return
     */
    int insert(T entity);

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
    int deleteByMap(@Param(Constants.COLUMN_MAP) Map<String, Object> columnMap);

    /**
     * 根据wrapper删除记录
     *
     * @param wrapper 实体对象封装操作类（属性条件可以为null）
     * @return
     */
    int delete(@Param(Constants.WRAPPER) Wrapper<T> wrapper);

    /**
     * 根据id列表批量删除
     *
     * @param idList id列表（值不能为null或者empty）
     * @return
     */
    int deleteBatchIds(@Param(Constants.COLLECTION) Collection<? extends Serializable> idList);

    /**
     * 根据id修改
     *
     * @param entity 实体对象
     * @return
     */
    int updateById(@Param(Constants.ENTITY) T entity);

//    /**
//     * <p>
//     * 根据 whereEntity 条件，更新记录
//     * </p>
//     *
//     * @param entity        实体对象 (set 条件值,可以为 null)
//     * @param updateWrapper 实体对象封装操作类（可以为 null,里面的 entity 用于生成 where 语句）
//     */
    // int update(@Param(Constants.ENTITY) T entity, @Param(Constants.WRAPPER) Wrapper<T> updateWrapper);

    /**
     * 根据 ID 查询
     *
     * @param id 主键ID
     */
    T selectById(Serializable id);

    /**
     * 查询（根据ID 批量查询）
     *
     * @param idList 主键ID列表(不能为 null 以及 empty)
     */
    List<T> selectBatchIds(@Param(Constants.COLLECTION) Collection<? extends Serializable> idList);

    /**
     * 查询（根据 columnMap 条件）
     *
     * @param columnMap 表字段 map 对象
     */
    List<T> selectByMap(@Param(Constants.COLUMN_MAP) Map<String, Object> columnMap);

    /**
     * 根据 query 条件，查询一条记录
     *
     * @param query 实体对象封装操作类（可以为 null）
     */
    T selectOne(@Param(Constants.WRAPPER) Wrapper<T> query);

    /**
     * 根据 query 条件，查询总记录数
     *
     * @param query 实体对象封装操作类（可以为 null）
     */
    Integer selectCount(@Param(Constants.WRAPPER) Wrapper<T> query);

    /**
     * 根据 query 条件，查询全部记录
     *
     * @param query 实体对象封装操作类（可以为 null）
     */
    List<T> selectList(@Param(Constants.WRAPPER) Wrapper<T> query);

    /**
     * 根据 query 条件，查询全部记录
     *
     * @param query 实体对象封装操作类（可以为 null）
     */
    List<Map<String, Object>> selectMaps(@Param(Constants.WRAPPER) Wrapper<T> query);

    /**
     * <p>
     * 根据 query 条件，查询全部记录
     * 注意： 只返回第一个字段的值
     * </p>
     *
     * @param query 实体对象封装操作类（可以为 null）
     */
    List<Object> selectObjs(@Param(Constants.WRAPPER) Wrapper<T> query);

    /**
     * <p>
     * 根据 query 条件，查询全部记录（并翻页）
     * </p>
     *
     * @param page  分页查询条件（可以为 RowBounds.DEFAULT）
     * @param query 实体对象封装操作类（可以为 null）
     */
    IPage<T> selectPage(IPage<T> page, @Param(Constants.WRAPPER) Wrapper query);

    /**
     * <p>
     * 根据 query 条件，查询全部记录（并翻页）
     * </p>
     *
     * @param page  分页查询条件
     * @param query 实体对象封装操作类
     */
    IPage<Map<String, Object>> selectMapsPage(IPage<T> page, @Param(Constants.WRAPPER) Wrapper<T> query);
}
