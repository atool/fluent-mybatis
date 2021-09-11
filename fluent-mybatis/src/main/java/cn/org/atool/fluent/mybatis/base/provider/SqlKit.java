package cn.org.atool.fluent.mybatis.base.provider;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.IHasDbType;
import cn.org.atool.fluent.mybatis.base.IHasMapping;
import cn.org.atool.fluent.mybatis.base.crud.BatchCrudImpl;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.crud.IUpdate;
import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.base.mapper.IEntityMapper;
import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.fluent.mybatis.segment.model.WrapperData;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分数据库类型, 组装对应操作的sql语句
 *
 * @author darui.wu
 */
@SuppressWarnings("rawtypes")
public interface SqlKit {
    /**
     * 工具类单例
     */
    Map<DbType, SqlKit> kits = new HashMap<>(8);

    static SqlKit factory(DbType dbType) {
        SqlKit kit = kits.get(dbType);
        if (kit == null) {
            synchronized (kits) {
                if (kits.containsKey(dbType)) {
                    return kits.get(dbType);
                }
                switch (dbType) {
                    case ORACLE:
                    case ORACLE12:
                        kits.put(dbType, new OracleSqlKit(dbType));
                        break;
                    default:
                        kits.put(dbType, new CommonSqlKit(dbType));
                }
                return kits.get(dbType);
            }
        } else {
            return kit;
        }
    }

    static SqlKit factory(IHasDbType hasDbType) {
        return factory(hasDbType.dbType());
    }

    static SqlKit factory(IHasMapping mapping) {
        return factory(mapping.mapping().getDbType());
    }

    /**
     * 批量更新, 插入, 删除操作语句构造
     *
     * @param crud BatchCrudImpl
     * @return sql
     */
    default String batchCrud(BatchCrudImpl crud) {
        return crud.batchSql();
    }

    /**
     * 构建插入语句
     *
     * @param prefix entity变量前缀
     * @param entity 实体实例
     * @param withPk 包含主键?
     * @return ignore
     */
    <E extends IEntity> String insertEntity(SqlProvider provider, String prefix, E entity, boolean withPk);

    /**
     * 生成 insertSelect 对应的sql语句
     *
     * @param tableName insert table
     * @param fields    要insert的字段列表
     * @param query     select query
     * @return sql语句
     */
    String insertSelect(String tableName, String[] fields, IQuery query);

    /**
     * 批量插入
     *
     * @param provider SqlProvider
     * @param entities Entity list
     * @param withPk   是否带主键
     * @return sql
     */
    <E extends IEntity> String insertBatch(SqlProvider provider, List<E> entities, boolean withPk);

    /**
     * 根据id列表构造查询条件IQuery
     *
     * @param mapping IMapping
     * @param ids     主键列表
     * @return IQuery
     */
    IQuery queryByIds(IMapping mapping, Collection ids);

    /**
     * 根据id列表构造查询条件IQuery
     *
     * @param mapping IMapping
     * @param ids     主键列表
     * @return IQuery
     */
    IQuery queryByIds(IMapping mapping, Object[] ids);

    /**
     * 根据map(k-v)条件构造查询条件IQuery
     *
     * @param mapping   IMapping
     * @param condition 查询条件
     * @return IQuery
     */
    IQuery queryByMap(IMapping mapping, Map<String, Object> condition);

    /**
     * 根据WrapperData设置构建物理删除语句
     *
     * @param provider SqlProvide
     * @param ew       更新/查询 条件
     * @return sql
     */
    String deleteBy(SqlProvider provider, WrapperData ew);

    /**
     * 根据IQuery条件构造逻辑删除更新IUpdate
     *
     * @param mapping IMapping
     * @param query   逻辑删除条件
     * @return IUpdate
     */
    IUpdate logicDeleteBy(IMapping mapping, IQuery query);

    /**
     * 根据Entity构造IUpdate
     *
     * @param mapping entity对应的数据库映射定义
     * @param entity  entity实例
     * @return IUpdate
     */
    IUpdate updateById(IMapping mapping, IEntity entity);

    /**
     * update(IQuery) SQL构造
     * {@link IEntityMapper#updateBy(IUpdate[])}
     *
     * @param provider SqlProvide
     * @param updaters 更新条件
     * @return sql
     */
    String updateBy(SqlProvider provider, IUpdate[] updaters);

    /**
     * 根据IUpdate构造sql语句, 考虑版本锁字段
     *
     * @param provider SqlProvider
     * @param ew       IUpdate数据
     * @return sql
     */
    String updateBy(SqlProvider provider, WrapperData ew);

    /**
     * 去掉limit部分 count(IQuery) SQL构造
     *
     * @param provider    SqlProvide
     * @param wrapperData query查询条件
     * @return sql
     */
    String countNoLimit(SqlProvider provider, WrapperData wrapperData);

    /**
     * 包含limit部分 count(IQuery) SQL构造
     *
     * @param provider    SqlProvide
     * @param wrapperData query查询条件
     * @return sql
     */
    String count(SqlProvider provider, WrapperData wrapperData);

    /**
     * 构造IQuery查询条件语句
     *
     * @param provider SqlProvide
     * @param ew       query查询条件
     * @return sql
     */
    String queryByQuery(SqlProvider provider, WrapperData ew);

    /**
     * 按主键列表查询数据
     *
     * @param provider SqlProvider
     * @param ids      主键列表
     * @return sql
     */
    String queryByIds(SqlProvider provider, Collection ids);

    /**
     * 按主键查询数据
     *
     * @param provider SqlProvider
     * @param id       主键值
     * @return sql
     */
    String queryById(SqlProvider provider, Serializable id);
}