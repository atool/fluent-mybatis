package cn.org.atool.fluent.mybatis.base.mapper;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.BaseQuery;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.crud.IUpdate;
import cn.org.atool.fluent.mybatis.base.intf.BatchCrud;
import cn.org.atool.fluent.mybatis.base.intf.IHasMapping;
import cn.org.atool.fluent.mybatis.base.provider.SqlProvider;
import cn.org.atool.fluent.mybatis.base.provider.StatementBuilder;
import cn.org.atool.fluent.mybatis.model.KeyMap;
import cn.org.atool.fluent.mybatis.utility.RefKit;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.builder.annotation.ProviderContext;
import org.apache.ibatis.mapping.StatementType;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ConfigurationKit;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static cn.org.atool.fluent.mybatis.mapper.FluentConst.*;

/**
 * IEntityMapper: 实例Mapper基类，Mapper 继承该接口后，无需编写 mapper.xml 文件，即可获得CRUD功能
 *
 * @author wudarui 2019-06-25 14:00
 */
@SuppressWarnings({"rawtypes", "UnusedReturnValue", "unchecked"})
public interface IEntityMapper<E extends IEntity> extends IMapper<E>, IHasMapping {

    /**
     * 插入一条记录, 主键字段为空
     *
     * @param entity 实例
     * @return 1: 插入成功
     * @see SqlProvider#insert(Map, ProviderContext)
     * @see StatementBuilder#selectKeyStatementOfInsert()
     */
    @InsertProvider(type = SqlProvider.class, method = M_Insert)
    int insert(@Param(Param_EW) E entity);

    /**
     * 批量插入数据，实例主键必须全部未赋值
     *
     * @param entities 实例列表
     * @return ignore
     * @see SqlProvider#insertBatch(Map, ProviderContext)
     * @see StatementBuilder#selectKeyStatementOfBatchInsert()
     */
    @InsertProvider(type = SqlProvider.class, method = M_InsertBatch)
    int insertBatch(@Param(Param_List) Collection<E> entities);

    /**
     * 根据 query 条件，查询全部记录
     *
     * @param query 实体对象封装操作类（可以为 null）
     * @return ignore
     */
    default List<E> listEntity(@Param(Param_EW) IQuery query) {
        List<E> list = this.internalListEntity(query);
        if (query instanceof BaseQuery) {
            Set<String> methods = ((BaseQuery) query).getWithRelations();
            for (String method : methods) {
                RefKit.invokeRefMethod(this.mapping().entityClass(), method, list);
            }
        }
        return list;
    }

    /**
     * 根据 query 条件，查询全部记录
     *
     * @param query 实体对象封装操作类（可以为 null）
     * @return ignore
     * @see SqlProvider#listEntity(Map, ProviderContext)
     * @see StatementBuilder#listEntityStatement()
     * @see ConfigurationKit#ConfigurationKit(Configuration, KeyMap)
     */
    @SelectProvider(type = SqlProvider.class, method = M_listEntity)
    List<E> internalListEntity(@Param(Param_EW) IQuery query);

    /**
     * 插入一条记录, 主键字段不为空
     *
     * @param entity 实例
     * @return ignore
     * @see SqlProvider#insertBatchWithPk(Map, ProviderContext)
     */
    @InsertProvider(type = SqlProvider.class, method = M_insertWithPk)
    int insertWithPk(@Param(Param_EW) E entity);

    /**
     * 批量插入数据，实例主键必须全部已赋值
     *
     * @param entities 实例列表
     * @return ignore
     * @see SqlProvider#insertBatchWithPk(Map, ProviderContext)
     */
    @InsertProvider(type = SqlProvider.class, method = M_insertBatchWithPk)
    int insertBatchWithPk(@Param(Param_List) Collection<E> entities);

    /**
     * insert into a_table(fields) select fields from b_table;
     *
     * @param fields 要插入的字段
     * @param query  select数据
     * @return 拷贝插入的记录数
     * @see SqlProvider#insertSelect(Map, ProviderContext)
     */
    @InsertProvider(type = SqlProvider.class, method = M_insertSelect)
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
    @UpdateProvider(type = SqlProvider.class, method = M_updateBy)
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
    @SelectProvider(type = SqlProvider.class, method = M_listObjs)
    <O> List<O> listObjs(@Param(Param_EW) IQuery query);

    /**
     * 根据 query 条件，查询全部记录
     *
     * @param query 实体对象封装操作类（可以为 null）
     * @return map列表
     * @see SqlProvider#listMaps(Map, ProviderContext)
     */
    @SelectProvider(type = SqlProvider.class, method = M_listMaps)
    @ResultType(Map.class)
    List<Map<String, Object>> listMaps(@Param(Param_EW) IQuery query);

    /**
     * 根据 query 条件，查询总记录数
     *
     * @param query 实体对象封装操作类 (允许key对应value值为 null)
     *              if value=null then column IS NULL; other column = value
     * @return ignore
     * @see SqlProvider#count(Map, ProviderContext)
     */
    @SelectProvider(type = SqlProvider.class, method = M_count)
    int count(@Param(Param_EW) IQuery query);

    /**
     * 根据 query 条件(如果有pageOffset, 去掉pageOffset限制部分)，查询总记录数
     *
     * @param query 实体对象封装操作类（可以为 null）
     * @return ignore
     * @see SqlProvider#countNoLimit(Map, ProviderContext)
     */
    @SelectProvider(type = SqlProvider.class, method = M_countNoLimit)
    int countNoLimit(@Param(Param_EW) IQuery query);

    /**
     * 根据wrapper删除记录
     *
     * @param query 实体对象封装操作类（属性条件可以为null）
     * @return ignore
     * @see SqlProvider#delete(Map, ProviderContext)
     */
    @DeleteProvider(type = SqlProvider.class, method = M_delete)
    int delete(@Param(Param_EW) IQuery query);

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
    @UpdateProvider(type = SqlProvider.class, method = M_batchCrud)
    void batchCrud(@Param(Param_EW) BatchCrud crud);

    /**
     * 调用存储过程
     *
     * @param procedure 存储过程及参数, 比如:
     *                  procedureName(#{p.input1, mode=IN, jdbcType=INTEGER}, #{p.output1, mode=OUT, jdbcType=INTEGER})
     * @param parameter 存储过程引用的入参和出参设置对象, 以前缀 "p." 引用属性
     */
    @Options(statementType = StatementType.CALLABLE)
    @Select("{CALL ${procedure}}")
    void callProcedure(@Param(Param_Procedure) String procedure, @Param(Param_P) Object parameter);
}