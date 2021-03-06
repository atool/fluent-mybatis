package cn.org.atool.fluent.mybatis.base.mapper;

import cn.org.atool.fluent.mybatis.base.BatchCrud;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.BaseSqlProvider;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.crud.IUpdate;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.mapping.StatementType;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static cn.org.atool.fluent.mybatis.mapper.FluentConst.*;

/**
 * IEntityMapper: 实例Mapper基类，Mapper 继承该接口后，无需编写 mapper.xml 文件，即可获得CRUD功能
 *
 * @author wudarui 2019-06-25 14:00
 */
@SuppressWarnings({"rawtypes"})
public interface IEntityMapper<E extends IEntity> extends IMapper<E> {
    /**
     * 调用存储过程
     *
     * @param procedure 存储过程及参数, 比如:
     *                  procedureName(#{p.input1, mode=IN, jdbcType=INTEGER}, #{p.output1, mode=OUT, jdbcType=INTEGER})
     * @param parameter 存储过程引用的入参和出参设置对象, 以前缀 "p." 引用属性
     */
    @Options(statementType = StatementType.CALLABLE)
    @Select("{CALL ${procedure}}")
    void callProcedure(@Param("procedure") String procedure, @Param("p") Object parameter);

    /**
     * 批量执行增删改操作
     *
     * <pre>
     * 传入多个操作时, 需要数据库支持
     * 比如MySql需要在jdbc url链接中附加设置 &allowMultiQueries=true
     * </pre>
     *
     * @param crud 增删改操作
     */
    @UpdateProvider(
        type = BaseSqlProvider.class,
        method = "batchCrud"
    )
    void batchCrud(@Param(Param_EW) BatchCrud crud);

    /**
     * 插入一条记录, 主键字段为空
     *
     * @param entity 实例
     * @return 1: 插入成功
     */
    int insert(E entity);

    /**
     * 插入一条记录, 主键字段不为空
     *
     * @param entity 实例
     * @return ignore
     */
    int insertWithPk(E entity);

    /**
     * 批量插入数据，实例主键必须全部未赋值
     *
     * @param entities 实例列表
     * @return ignore
     */
    int insertBatch(@Param(Param_List) Collection<E> entities);

    /**
     * 批量插入数据，实例主键必须全部已赋值
     *
     * @param entities 实例列表
     * @return ignore
     */
    int insertBatchWithPk(@Param(Param_List) Collection<E> entities);

    /**
     * insert into a_table(fields) select fields from b_table;
     *
     * @param fields 要插入的字段
     * @param query  select数据
     * @return 拷贝插入的记录数
     * @see BaseSqlProvider#insertSelect(Map)
     */
    int insertSelect(@Param(Param_Fields) String[] fields, @Param(Param_EW) IQuery query);

    /**
     * 根据id修改
     *
     * @param entity 实体对象
     * @return ignore
     */
    int updateById(@Param(Param_ET) E entity);

    /**
     * 根据update对象更新记录
     *
     * <pre>
     * 传入多个Update时, 需要数据库支持
     * 比如MySql需要在jdbc url链接中附加设置 &allowMultiQueries=true
     * </pre>
     *
     * @param updates 更新列表
     * @return ignore
     */
    int updateBy(@Param(Param_EW) IUpdate... updates);

    /**
     * 根据 ID 查询
     *
     * @param id 主键ID
     * @return ignore
     */
    E findById(Serializable id);

    /**
     * 根据 query 条件，查询一条记录
     *
     * @param query 实体对象封装操作类（可以为 null）
     * @return ignore
     */
    E findOne(@Param(Param_EW) IQuery query);

    /**
     * 查询（根据ID 批量查询）
     *
     * @param ids 主键ID列表(不能为 null 以及 empty)
     * @return ignore
     */
    List<E> listByIds(@Param(Param_List) Collection ids);

    /**
     * 查询（根据 columnMap 条件）
     *
     * @param columnMap 表字段 map 对象
     * @return ignore
     */
    List<E> listByMap(@Param(Param_CM) Map<String, Object> columnMap);

    /**
     * 根据 query 条件，查询全部记录
     *
     * @param query 实体对象封装操作类（可以为 null）
     * @return ignore
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
     * @return ignore
     */
    <O> List<O> listObjs(@Param(Param_EW) IQuery query);

    /**
     * 根据 query 条件，查询总记录数
     *
     * @param query 实体对象封装操作类（可以为 null）
     * @return ignore
     */
    Integer count(@Param(Param_EW) IQuery query);

    /**
     * 根据 query 条件(如果有pageOffset, 去掉pageOffset限制部分)，查询总记录数
     *
     * @param query 实体对象封装操作类（可以为 null）
     * @return ignore
     */
    Integer countNoLimit(@Param(Param_EW) IQuery query);

    /**
     * 根据id删除记录
     *
     * @param ids 主键列表
     * @return ignore
     */
    int deleteById(@Param(Param_List) Serializable... ids);

    /**
     * 根据id列表批量删除
     *
     * @param idList id列表（值不能为null或者empty）
     * @return ignore
     */
    int deleteByIds(@Param(Param_List) Collection<? extends Serializable> idList);

    /**
     * 根据 columnMap key值删除记录
     *
     * @param cm k-v条件
     * @return ignore
     */
    int deleteByMap(@Param(Param_CM) Map<String, Object> cm);

    /**
     * 根据wrapper删除记录
     *
     * @param wrapper 实体对象封装操作类（属性条件可以为null）
     * @return ignore
     */
    int delete(@Param(Param_EW) IQuery wrapper);

    /**
     * 根据id逻辑删除
     *
     * @param ids 主键值列表
     * @return ignore
     */
    int logicDeleteById(@Param(Param_List) Serializable... ids);

    /**
     * 根据id列表批量逻辑删除
     *
     * @param idList id列表（值不能为null或者empty）
     * @return ignore
     */
    int logicDeleteByIds(@Param(Param_List) Collection<? extends Serializable> idList);

    /**
     * 根据 columnMap key值逻辑删除记录
     *
     * @param cm k-v条件
     * @return ignore
     */
    int logicDeleteByMap(@Param(Param_CM) Map<String, Object> cm);

    /**
     * 根据wrapper删除记录
     *
     * @param wrapper 实体对象封装操作类（属性条件可以为null）
     * @return ignore
     */
    int logicDelete(@Param(Param_EW) IQuery wrapper);
}