package cn.org.atool.fluent.mybatis.base.provider;

import cn.org.atool.fluent.mybatis.If;
import cn.org.atool.fluent.mybatis.base.BatchCrud;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.IHasDbType;
import cn.org.atool.fluent.mybatis.base.crud.*;
import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.base.mapper.IEntityMapper;
import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;
import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.fluent.mybatis.segment.model.WrapperData;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static cn.org.atool.fluent.mybatis.If.isBlank;
import static cn.org.atool.fluent.mybatis.mapper.FluentConst.*;
import static cn.org.atool.fluent.mybatis.mapper.StrConstant.EMPTY;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.assertNotEmpty;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.assertNotNull;
import static cn.org.atool.fluent.mybatis.utility.SqlProviderKit.*;

/**
 * SqlProvider: 动态SQL构造基类
 *
 * @author wudarui
 */
@SuppressWarnings({"rawtypes", "unused"})
public abstract class SqlProvider<E extends IEntity> implements IHasDbType {
    private final SqlKit sqlKit;

    public SqlProvider() {
        this.sqlKit = SqlKit.factory(this);
    }

    /**
     * 批量更新, 插入, 删除操作语句构造
     * {@link IEntityMapper#batchCrud(BatchCrud)}
     *
     * @param map k-v条件
     * @return ignore
     */
    public static String batchCrud(Map map) {
        IWrapper wrapper = getWrapper(map, Param_EW);
        if (!(wrapper instanceof BatchCrudImpl)) {
            throw new IllegalArgumentException("the wrapper should be an instance of BatchUpdaterImpl.");
        } else {
            BatchCrudImpl crud = (BatchCrudImpl) wrapper;
            return SqlKit.factory(crud).batchCrud(crud);
        }
    }

    /**
     * 构造 insert ... select ... SQL语句
     * {@link IEntityMapper#insertSelect(String[], IQuery)}
     *
     * @param map k-v条件
     * @return ignore
     */
    public String insertSelect(Map map) {
        String[] fields = (String[]) map.get(Param_Fields);
        BaseQuery query = (BaseQuery) map.get(Param_EW);
        String table = this.dynamic(query);
        return sqlKit.insertSelect(table, fields, query);
    }

    /**
     * 插入id未赋值的entity
     * {@link IEntityMapper#insert(IEntity)}
     *
     * @param entity 实体实例
     * @return sql
     */
    public String insert(E entity) {
        assertNotNull("entity", entity);
        return sqlKit.insertEntity(this, EMPTY, entity, false);
    }

    /**
     * 插入id已赋值的entity
     * {@link IEntityMapper#insertWithPk(IEntity)}
     *
     * @param entity 实体实例
     * @return sql
     */
    public String insertWithPk(E entity) {
        assertNotNull("entity", entity);
        return sqlKit.insertEntity(this, EMPTY, entity, true);
    }

    /**
     * 批量插入实例
     * {@link IEntityMapper#insertBatch(Collection)}
     *
     * @return sql
     */
    public String insertBatch(Map map) {
        assertNotEmpty(Param_List, map);
        List<E> entities = getParas(map, Param_List);
        return sqlKit.insertBatch(this, entities, false);
    }

    /**
     * 批量插入实例
     * {@link IEntityMapper#insertBatchWithPk(Collection)}
     *
     * @return sql
     */
    public String insertBatchWithPk(Map map) {
        assertNotEmpty(Param_List, map);
        List<E> entities = getParas(map, Param_List);
        return sqlKit.insertBatch(this, entities, true);
    }

    /**
     * 去掉limit部分 count(IQuery) SQL构造
     * {@link IEntityMapper#countNoLimit(IQuery)}
     *
     * @param map k-v条件
     * @return ignore
     */
    public String countNoLimit(Map map) {
        WrapperData ew = getWrapperData(map, Param_EW);
        return sqlKit.countNoLimit(this, ew);
    }

    /**
     * count(IQuery) SQL构造
     * {@link IEntityMapper#count(IQuery)}
     *
     * @param map k-v条件
     * @return ignore
     */
    public String count(Map map) {
        WrapperData ew = getWrapperData(map, Param_EW);
        return sqlKit.count(this, ew);
    }

    /**
     * 根据动态条件查询Entity SQL构造
     * {@link IEntityMapper#listEntity(IQuery)}
     *
     * @param map k-v条件
     * @return ignore
     */
    public String listEntity(Map map) {
        WrapperData ew = getWrapperData(map, Param_EW);
        return sqlKit.queryByQuery(this, ew);
    }

    /**
     * 根据动态条件查询Map列表 SQL构造
     * {@link IEntityMapper#listMaps(IQuery)}
     *
     * @param map k-v条件
     * @return ignore
     */
    public String listMaps(Map map) {
        WrapperData ew = getWrapperData(map, Param_EW);
        return sqlKit.queryByQuery(this, ew);
    }

    /**
     * 根据动态条件查询单列数据列表 SQL构造
     * {@link IEntityMapper#listObjs(IQuery)}
     *
     * @param map k-v条件
     * @return ignore
     */
    public String listObjs(Map map) {
        WrapperData ew = getWrapperData(map, Param_EW);
        return sqlKit.queryByQuery(this, ew);
    }

    /**
     * 根据Map查询数据SQL构造
     * {@link IEntityMapper#listByMap(Map)}
     *
     * @param map k-v条件
     * @return ignore
     */
    public String listByMap(Map map) {
        Map<String, Object> where = getParas(map, Param_CM);
        assertNotEmpty("where", where);
        return sqlKit.queryByMap(this, where);
    }

    /**
     * 根据Id列表查询数据SQL构造
     * {@link IEntityMapper#listByIds(Collection)}
     *
     * @param map k-v条件
     * @return sql
     */
    public String listByIds(Map map) {
        Collection ids = getParas(map, Param_List);
        assertNotEmpty("PrimaryKeyList", ids);
        return sqlKit.queryByIds(this, ids);
    }

    /**
     * 根据主键查找数据SQL构造
     * {@link IEntityMapper#findById(Serializable)}
     *
     * @param id 主键
     * @return ignore
     */
    public String findById(Serializable id) {
        assertNotNull("PrimaryKey", id);
        return sqlKit.queryById(this, id);
    }

    /**
     * 根据动态条件查询一条记录SQL构造
     * {@link IEntityMapper#findOne(IQuery)}
     *
     * @param map k-v条件
     * @return ignore
     */
    public String findOne(Map map) {
        WrapperData ew = getWrapperData(map, Param_EW);
        return sqlKit.queryByQuery(this, ew);
    }

    /**
     * 根据主键物理删除数据SQL构造
     * {@link IEntityMapper#deleteById(Serializable...)}
     *
     * @param map 主键值
     * @return ignore
     */
    public String deleteById(Map map) {
        Serializable[] ids = (Serializable[]) map.get(Param_List);
        assertNotEmpty("ids", ids);
        return sqlKit.deleteById(this, ids);
    }

    /**
     * 根据主键逻辑删除数据SQL构造
     * {@link IEntityMapper#logicDeleteById(Serializable...)}
     *
     * @param map 参数
     * @return ignore
     */
    public String logicDeleteById(Map map) {
        Serializable[] ids = (Serializable[]) map.get(Param_List);
        assertNotEmpty("ids", ids);
        return sqlKit.logicDeleteById(this, ids);
    }

    /**
     * 根据主键列表物理删除数据SQL构造
     * {@link IEntityMapper#deleteByIds(Collection)}
     *
     * @param map k-v条件
     * @return ignore
     */
    public String deleteByIds(Map map) {
        Collection ids = getParas(map, Param_List);
        assertNotEmpty("ids", ids);
        return sqlKit.deleteByIds(this, ids);
    }

    /**
     * 根据主键列表逻辑删除数据SQL构造
     * {@link IEntityMapper#logicDeleteByIds(Collection)}
     *
     * @param map k-v条件
     * @return ignore
     */
    public String logicDeleteByIds(Map map) {
        Collection ids = getParas(map, Param_List);
        assertNotEmpty("ids", ids);
        return sqlKit.logicDeleteByIds(this, ids);
    }

    /**
     * 按map删除数据SQL构造
     * {@link IEntityMapper#deleteByMap(Map)}
     *
     * @param map k-v条件
     * @return ignore
     */
    public String deleteByMap(Map<String, Object> map) {
        Map<String, Object> cm = getParas(map, Param_CM);
        return sqlKit.deleteByMap(this, cm);
    }

    /**
     * 按map逻辑删除数据SQL构造
     * {@link IEntityMapper#logicDeleteByMap(Map)}
     *
     * @param map k-v条件
     * @return ignore
     */
    public String logicDeleteByMap(Map<String, Object> map) {
        Map<String, Object> cm = getParas(map, Param_CM);
        return sqlKit.logicDeleteByMap(this, cm);
    }

    /**
     * 根据动态查询条件物理删除数据SQL构造
     * {@link IEntityMapper#delete(IQuery)}
     *
     * @param map k-v条件
     * @return ignore
     */
    public String delete(Map map) {
        WrapperData ew = getWrapperData(map, Param_EW);
        return sqlKit.deleteBy(this, ew);
    }

    /**
     * 根据动态查询条件逻辑删除数据SQL构造
     * {@link IEntityMapper#logicDelete(IQuery)}
     *
     * @param map k-v条件
     * @return ignore
     */
    public String logicDelete(Map map) {
        WrapperData ew = getWrapperData(map, Param_EW);
        return sqlKit.logicDeleteBy(this, ew);
    }

    /**
     * update(IQuery) SQL构造
     * {@link IEntityMapper#updateBy(IUpdate[])}
     *
     * @param map k-v条件
     * @return ignore
     */
    public String updateBy(Map<String, Object> map) {
        Object wrapper = map.get(Param_EW);
        if (If.isEmpty(wrapper)) {
            throw FluentMybatisException.instance("the parameter[%s] can't be empty.", Param_EW);
        } else if (!(wrapper instanceof IUpdate[])) {
            throw new IllegalArgumentException("the parameter should be an array of IUpdate");
        } else {
            return sqlKit.updateBy(this, (IUpdate[]) wrapper);
        }
    }

    /**
     * 根据{@link IDefaultSetter#setInsertDefault(IEntity)}设置默认值
     *
     * @param entity 实体实例
     */
    public void setEntityByDefault(IEntity entity) {
        mapping().defaultSetter().setInsertDefault(entity);
    }

    /**
     * 数据库类型
     *
     * @return ignore
     */
    public DbType dbType() {
        return mapping().getDbType();
    }

    /**
     * 返回表名
     *
     * @return ignore
     */
    public String tableName() {
        return mapping().getTableName();
    }

    /**
     * 获取字段映射关系
     *
     * @return 字段映射
     */
    public abstract IMapping mapping();

    /**
     * 获取IQuery或IUpdate对应的表名称
     *
     * @param wrapper IQuery或IUpdate
     * @return 表名称
     */
    private String dynamic(IWrapper wrapper) {
        String table = (String) wrapper.getTable().get();
        return isBlank(table) ? this.tableName() : table;
    }
}