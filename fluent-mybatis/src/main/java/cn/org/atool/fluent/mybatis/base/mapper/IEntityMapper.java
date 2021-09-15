package cn.org.atool.fluent.mybatis.base.mapper;

import cn.org.atool.fluent.mybatis.base.BatchCrud;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.IHasMapping;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.crud.IUpdate;
import cn.org.atool.fluent.mybatis.base.crud.IWrapper;
import cn.org.atool.fluent.mybatis.base.provider.SqlProvider;
import cn.org.atool.fluent.mybatis.base.provider.StatementBuilder;
import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.builder.annotation.ProviderContext;
import org.apache.ibatis.mapping.StatementType;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static cn.org.atool.fluent.mybatis.If.isEmpty;
import static cn.org.atool.fluent.mybatis.base.provider.SqlKitFactory.factory;
import static cn.org.atool.fluent.mybatis.mapper.FluentConst.*;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.assertNotEmpty;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.assertNotNull;

/**
 * IEntityMapper: 实例Mapper基类，Mapper 继承该接口后，无需编写 mapper.xml 文件，即可获得CRUD功能
 *
 * @author wudarui 2019-06-25 14:00
 */
@SuppressWarnings({"rawtypes", "UnusedReturnValue"})
public interface IEntityMapper<E extends IEntity> extends IMapper<E>, IHasMapping {

    /**
     * 插入一条记录, 主键字段为空
     *
     * @param entity 实例
     * @return 1: 插入成功
     * @see SqlProvider#insert(IEntity, ProviderContext)
     * @see StatementBuilder#insertStatement()
     */
    @InsertProvider(
        type = SqlProvider.class,
        method = M_Insert
    )
    int insert(E entity);

    /**
     * 批量插入数据，实例主键必须全部未赋值
     *
     * @param entities 实例列表
     * @return ignore
     * @see SqlProvider#insertBatch(Map, ProviderContext)
     * @see StatementBuilder#insertBatchStatement()
     */
    @InsertProvider(
        type = SqlProvider.class,
        method = M_InsertBatch
    )
    int insertBatch(@Param(Param_List) Collection<E> entities);

    /**
     * 根据 query 条件，查询全部记录
     *
     * @param query 实体对象封装操作类（可以为 null）
     * @return ignore
     * @see SqlProvider#listEntity(Map, ProviderContext)
     * @see StatementBuilder#listEntityStatement()
     */
    @SelectProvider(
        type = SqlProvider.class,
        method = M_listEntity
    )
    List<E> listEntity(@Param(Param_EW) IQuery query);

    /* =================================*/

    /**
     * 插入一条记录, 主键字段不为空
     *
     * @param entity 实例
     * @return ignore
     * @see SqlProvider#insertBatchWithPk(Map, ProviderContext)
     */
    @InsertProvider(
        type = SqlProvider.class,
        method = "insertWithPk"
    )
    int insertWithPk(E entity);

    /**
     * 批量插入数据，实例主键必须全部已赋值
     *
     * @param entities 实例列表
     * @return ignore
     * @see SqlProvider#insertBatchWithPk(Map)
     */
    @InsertProvider(
        type = SqlProvider.class,
        method = "insertBatchWithPk"
    )
    int insertBatchWithPk(@Param(Param_List) Collection<E> entities);

    /**
     * insert into a_table(fields) select fields from b_table;
     *
     * @param fields 要插入的字段
     * @param query  select数据
     * @return 拷贝插入的记录数
     * @see SqlProvider#insertSelect(Map, ProviderContext)
     */
    @InsertProvider(
        type = SqlProvider.class,
        method = "insertSelect"
    )
    int insertSelect(@Param(Param_Fields) String[] fields, @Param(Param_EW) IQuery query);

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
     * @see SqlProvider#updateBy(Map, ProviderContext)
     */
    @UpdateProvider(
        type = SqlProvider.class,
        method = "updateBy"
    )
    int updateBy(@Param(Param_EW) IUpdate... updates);

    /**
     * 根据 query 条件，查询全部记录
     * 注意： 只返回第一个字段的值
     * </p>
     *
     * @param query 实体对象封装操作类（可以为 null）
     * @return ignore
     * @see SqlProvider#listObjs(Map, ProviderContext)
     */
    @SelectProvider(
        type = SqlProvider.class,
        method = "listObjs"
    )
    <O> List<O> listObjs(@Param(Param_EW) IQuery query);

    /**
     * 根据 query 条件，查询全部记录
     *
     * @param query 实体对象封装操作类（可以为 null）
     * @return map列表
     * @see SqlProvider#listMaps(Map, ProviderContext)
     */
    @SelectProvider(
        type = SqlProvider.class,
        method = "listMaps"
    )
    @ResultType(Map.class)
    List<Map<String, Object>> listMaps(@Param(Param_EW) IQuery query);

    /**
     * 根据 query 条件，查询总记录数
     *
     * @param query 实体对象封装操作类（可以为 null）
     * @return ignore
     * @see SqlProvider#count(Map, ProviderContext)
     */
    @SelectProvider(
        type = SqlProvider.class,
        method = "count"
    )
    Integer count(@Param(Param_EW) IQuery query);

    /**
     * 根据 query 条件(如果有pageOffset, 去掉pageOffset限制部分)，查询总记录数
     *
     * @param query 实体对象封装操作类（可以为 null）
     * @return ignore
     * @see SqlProvider#countNoLimit(Map)
     */
    @SelectProvider(
        type = SqlProvider.class,
        method = "countNoLimit"
    )
    Integer countNoLimit(@Param(Param_EW) IQuery query);

    /**
     * 根据wrapper删除记录
     *
     * @param wrapper 实体对象封装操作类（属性条件可以为null）
     * @return ignore
     * @see SqlProvider#delete(Map, ProviderContext)
     */
    @DeleteProvider(
        type = SqlProvider.class,
        method = "delete"
    )
    int delete(@Param(Param_EW) IQuery wrapper);

    /* =========== 静态引用 =========== */

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
     * @see SqlProvider#batchCrud(Map, ProviderContext)
     */
    @UpdateProvider(
        type = SqlProvider.class,
        method = "batchCrud"
    )
    void batchCrud(@Param(Param_EW) BatchCrud crud);

    /* ===========default 实现=========== */

    /**
     * 根据id修改
     *
     * @param entity 实体对象
     * @return ignore
     */
    default int updateById(E entity) {
        IUpdate update = factory(this).updateById(this.mapping(), entity);
        return this.updateBy(update);
    }

    /**
     * 根据 ID 查询
     *
     * @param id 主键ID
     * @return ignore
     */
    default E findById(Serializable id) {
        IQuery query = factory(this).queryByIds(this.mapping(), new Object[]{id});
        return this.findOne(query);
    }

    /**
     * 根据 query 条件，查询一条记录
     *
     * @param query 实体对象封装操作类（可以为 null）
     * @return ignore
     */
    default E findOne(IQuery query) {
        List<E> list = this.listEntity(query);
        if (isEmpty(list)) {
            return null;
        } else if (list.size() == 1) {
            return list.get(0);
        } else {
            throw new FluentMybatisException("Expected one result (or null) to be returned, but found " + list.size() + " results.");
        }
    }

    /**
     * 查询（根据ID 批量查询）
     *
     * @param ids 主键ID列表(不能为 null 以及 empty)
     * @return ignore
     */
    default List<E> listByIds(Object... ids) {
        IQuery query = factory(this).queryByIds(this.mapping(), ids);
        return this.listEntity(query);
    }

    /**
     * 查询（根据ID 批量查询）
     *
     * @param ids 主键ID列表(不能为 null 以及 empty)
     * @return ignore
     */
    default List<E> listByIds(Collection ids) {
        IQuery query = factory(this).queryByIds(this.mapping(), ids);
        return this.listEntity(query);
    }

    /**
     * 根据数据库字段(或Entity属性)查询
     *
     * @param isColumn  true: key值为数据库字段; false: key值为Entity属性字段
     * @param condition 数据库字段(或Entity属性) k-v条件
     * @return ignore
     */
    default List<E> listByMap(boolean isColumn, Map<String, Object> condition) {
        assertNotEmpty("condition", condition);
        IQuery query = this.mapping().query();
        factory(this).eqByMap(this.mapping(), (IWrapper) query, isColumn, condition);
        return this.listEntity(query);
    }

    /**
     * 根据id删除记录
     *
     * @param ids 主键列表
     * @return ignore
     */
    default int deleteById(Serializable... ids) {
        assertNotEmpty("ids", ids);
        IQuery query = factory(this).queryByIds(this.mapping(), ids);
        return this.delete(query);
    }

    /**
     * 根据id列表批量删除
     *
     * @param ids id列表（值不能为null或者empty）
     * @return ignore
     */
    default int deleteByIds(Collection ids) {
        assertNotEmpty("ids", ids);
        IQuery query = factory(this).queryByIds(this.mapping(), ids);
        return this.delete(query);
    }

    /**
     * 根据数据库字段(或Entity属性)删除记录
     *
     * @param isColumn  true: key值为数据库字段; false: key值为Entity属性字段
     * @param condition 数据库字段(或Entity属性)k-v条件
     * @return ignore
     */
    default int deleteByMap(boolean isColumn, Map<String, Object> condition) {
        IQuery query = this.mapping().query();
        factory(this).eqByMap(this.mapping(), (IWrapper) query, isColumn, condition);
        return this.delete(query);
    }

    /**
     * 根据id逻辑删除
     *
     * @param ids 主键值列表
     * @return ignore
     */
    default int logicDeleteById(Object... ids) {
        assertNotEmpty("ids", ids);
        IUpdate update = factory(this).logicDeleteByIds(this.mapping(), ids);
        return this.updateBy(update);
    }

    /**
     * 根据id列表批量逻辑删除
     *
     * @param ids id列表（值不能为null或者empty）
     * @return ignore
     */
    default int logicDeleteByIds(Collection ids) {
        assertNotEmpty("ids", ids);
        IUpdate update = factory(this).logicDeleteByIds(this.mapping(), ids);
        return this.updateBy(update);
    }

    /**
     * 根据数据库字段(或Entity属性)逻辑删除记录
     *
     * @param isColumn  true: key值为数据库字段; false: key值为Entity属性字段
     * @param condition 数据库字段(或Entity属性)k-v条件
     * @return ignore
     */
    default int logicDeleteByMap(boolean isColumn, Map<String, Object> condition) {
        assertNotEmpty("ids", condition);
        IUpdate update = this.mapping().updater();
        /* 逻辑删除忽略版本号 */
        update.getWrapperData().setIgnoreLockVersion(true);
        factory(this).setLogicDeleted(this.mapping(), update);
        factory(this).eqByMap(this.mapping(), (IWrapper) update, isColumn, condition);
        return this.updateBy(update);
    }

    /**
     * 根据wrapper删除记录
     *
     * @param query 实体对象封装操作类（属性条件可以为null）
     * @return ignore
     */
    default int logicDelete(IQuery query) {
        assertNotNull("query", query);
        /* 逻辑删除忽略版本号 */
        query.getWrapperData().setIgnoreLockVersion(true);
        IUpdate update = factory(this).logicDeleteBy(this.mapping(), query);
        return this.updateBy(update);
    }
}