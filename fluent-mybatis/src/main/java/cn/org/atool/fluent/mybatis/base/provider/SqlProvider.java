package cn.org.atool.fluent.mybatis.base.provider;

import cn.org.atool.fluent.mybatis.If;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.BaseQuery;
import cn.org.atool.fluent.mybatis.base.crud.BatchCrudImpl;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.crud.IUpdate;
import cn.org.atool.fluent.mybatis.base.entity.AMapping;
import cn.org.atool.fluent.mybatis.base.intf.BatchCrud;
import cn.org.atool.fluent.mybatis.base.mapper.IEntityMapper;
import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;
import cn.org.atool.fluent.mybatis.segment.model.WrapperData;
import cn.org.atool.fluent.mybatis.utility.RefKit;
import org.apache.ibatis.builder.annotation.ProviderContext;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static cn.org.atool.fluent.mybatis.base.provider.SqlKitFactory.factory;
import static cn.org.atool.fluent.mybatis.mapper.FluentConst.*;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.*;
import static cn.org.atool.fluent.mybatis.utility.SqlProviderKit.getParas;
import static cn.org.atool.fluent.mybatis.utility.SqlProviderKit.getWrapperData;

/**
 * SqlProvider: 动态SQL构造基类
 *
 * @author wudarui
 */
@SuppressWarnings({"rawtypes", "unused", "unchecked"})
public class SqlProvider {
    private SqlProvider() {
    }

    /**
     * 插入id未赋值的entity
     * {@link IEntityMapper#insert(IEntity)}
     *
     * @param map     实体实例
     * @param context ProviderContext
     * @return sql
     */
    public static String insert(Map map, ProviderContext context) {
        IEntity entity = getParas(map, Param_EW);
        assertNotNull(Param_Entity, entity);
        AMapping mapping = mapping(context);
        return sqlKit(mapping).insertEntity(mapping, Param_EW, entity, false);
    }

    /**
     * 批量插入实例
     * {@link IEntityMapper#insertBatch(Collection)}
     *
     * @return sql
     */
    public static String insertBatch(Map map, ProviderContext context) {
        List entities = getParas(map, Param_List);
        assertNotEmpty(Param_List, entities);
        AMapping mapping = mapping(context);
        return sqlKit(mapping).insertBatch(mapping, entities, false, mapping.tableId());
    }

    /**
     * 批量插入实例
     * {@link IEntityMapper#insertBatchWithPk(Collection)}
     *
     * @return sql
     */
    public static String insertBatchWithPk(Map map, ProviderContext context) {
        List entities = getParas(map, Param_List);
        assertNotEmpty(Param_List, entities);
        AMapping mapping = mapping(context);
        return sqlKit(mapping).insertBatch(mapping, entities, true, mapping.tableId());
    }

    /**
     * 根据动态条件查询Entity SQL构造
     * {@link IEntityMapper#internalListEntity(IQuery)}
     *
     * @param map k-v条件
     * @return ignore
     */
    public static String listEntity(Map map, ProviderContext context) {
        WrapperData ew = getWrapperData(map, Param_EW);
        assertNotNull(Param_EW, ew);
        AMapping mapping = mapping(context);
        return sqlKit(mapping).queryBy(mapping, ew);
    }

    /**
     * 批量更新, 插入, 删除操作语句构造
     * {@link IEntityMapper#batchCrud(BatchCrud)}
     *
     * @param map k-v条件
     * @return ignore
     */
    public static String batchCrud(Map map, ProviderContext context) {
        BatchCrud crud = (BatchCrud) map.get(Param_EW);
        if (!(crud instanceof BatchCrudImpl)) {
            throw new IllegalArgumentException("the wrapper should be an instance of BatchUpdaterImpl.");
        }
        AMapping mapping = mapping(context);
        return factory(mapping).batchCrud(mapping, (BatchCrudImpl) crud);
    }

    /**
     * 构造 insert ... select ... SQL语句
     * {@link IEntityMapper#insertSelect(String[], IQuery)}
     *
     * @param map k-v条件
     * @return ignore
     */
    public static String insertSelect(Map map, ProviderContext context) {
        String[] fields = (String[]) map.get(Param_Fields);
        BaseQuery query = (BaseQuery) map.get(Param_EW);
        AMapping mapping = mapping(context);
        String table = mapping.dynamic(query).get(mapping);
        return sqlKit(mapping).insertSelect(mapping, table, fields, query);
    }

    /**
     * 插入id已赋值的entity
     * {@link IEntityMapper#insertWithPk(IEntity)}
     *
     * @param map 实体实例
     * @return sql
     */
    public static String insertWithPk(Map map, ProviderContext context) {
        IEntity entity = getParas(map, Param_EW);
        assertNotNull(Param_Entity, entity);
        AMapping mapping = mapping(context);
        return sqlKit(mapping).insertEntity(mapping, Param_EW, entity, true);
    }

    /**
     * 去掉limit部分 count(IQuery) SQL构造
     * {@link IEntityMapper#countNoLimit(IQuery)}
     *
     * @param map k-v条件
     * @return ignore
     */
    public static String countNoLimit(Map map, ProviderContext context) {
        WrapperData ew = getWrapperData(map, Param_EW);
        assertNotNull(Param_EW, ew);
        AMapping mapping = mapping(context);
        return sqlKit(mapping).countNoLimit(mapping, ew);
    }

    /**
     * count(IQuery) SQL构造
     * {@link IEntityMapper#count(IQuery)}
     *
     * @param map k-v条件
     * @return ignore
     */
    public static String count(Map map, ProviderContext context) {
        WrapperData ew = getWrapperData(map, Param_EW);
        assertNotNull(Param_EW, ew);
        AMapping mapping = mapping(context);
        return sqlKit(mapping).count(mapping, ew);
    }

    /**
     * 根据动态条件查询Map列表 SQL构造
     * {@link IEntityMapper#listMaps(IQuery)}
     *
     * @param map k-v条件
     * @return ignore
     */
    public static String listMaps(Map map, ProviderContext context) {
        WrapperData ew = getWrapperData(map, Param_EW);
        assertNotNull(Param_EW, ew);
        AMapping mapping = mapping(context);
        return sqlKit(mapping).queryBy(mapping, ew);
    }

    /**
     * 根据动态条件查询单列数据列表 SQL构造
     * {@link IEntityMapper#listObjs(IQuery)}
     *
     * @param map k-v条件
     * @return ignore
     */
    public static String listObjs(Map map, ProviderContext context) {
        WrapperData ew = getWrapperData(map, Param_EW);
        assertNotNull(Param_EW, ew);
        AMapping mapping = mapping(context);
        return sqlKit(mapping).queryBy(mapping, ew);
    }

    /**
     * 根据动态查询条件物理删除数据SQL构造
     * {@link IEntityMapper#delete(IQuery)}
     *
     * @param map k-v条件
     * @return ignore
     */
    public static String delete(Map map, ProviderContext context) {
        WrapperData ew = getWrapperData(map, Param_EW);
        assertNotNull(Param_EW, ew);
        AMapping mapping = mapping(context);
        return sqlKit(mapping).deleteBy(mapping, ew);
    }

    /**
     * update(IQuery) SQL构造
     * {@link IEntityMapper#updateBy(IUpdate[])}
     *
     * @param map k-v条件
     * @return ignore
     */
    public static String updateBy(Map<String, Object> map, ProviderContext context) {
        Object wrapper = map.get(Param_EW);
        if (If.isEmpty(wrapper)) {
            throw FluentMybatisException.instance("the parameter[%s] can't be empty.", Param_EW);
        } else if (!(wrapper instanceof IUpdate[])) {
            wrapper = new IUpdate[]{(IUpdate) wrapper};
        }
        AMapping mapping = mapping(context);
        return sqlKit(mapping).updateBy(mapping, (IUpdate[]) wrapper);
    }

    private static SqlKit sqlKit(AMapping mapping) {
        return factory(mapping);
    }

    private static AMapping mapping(ProviderContext context) {
        isMapperFactoryInitialized();
        Class mapperClass = context.getMapperType();
        return (AMapping) RefKit.byMapper(mapperClass);
    }
}