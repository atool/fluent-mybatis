package cn.org.atool.fluent.mybatis.base.provider;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.IHasDbType;
import cn.org.atool.fluent.mybatis.base.crud.BatchCrudImpl;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.crud.IUpdate;
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
     * 按主键物理删除
     *
     * @param provider SqlProvider
     * @param ids      主键列表
     * @return sql
     */
    String deleteById(SqlProvider provider, Serializable[] ids);

    /**
     * 根据主键列表物理删除数据SQL构造
     *
     * @param provider SqlProvider
     * @param ids      主键列表
     * @return sql
     */
    String deleteByIds(SqlProvider provider, Collection ids);

    /**
     * 按主键逻辑删除
     *
     * @param provider SqlProvider
     * @param ids      主键列表
     * @return sql
     */
    String logicDeleteById(SqlProvider provider, Serializable[] ids);

    /**
     * 按主键逻辑删除
     *
     * @param provider SqlProvider
     * @param ids      主键列表
     * @return sql
     */
    String logicDeleteByIds(SqlProvider provider, Collection ids);

    /**
     * 按Map k-v条件物理删除
     *
     * @param provider SqlProvider
     * @param map      k-v条件
     * @return sql
     */
    String deleteByMap(SqlProvider provider, Map<String, Object> map);

    /**
     * 按Map k-v条件逻辑删除
     *
     * @param provider SqlProvider
     * @param map      k-v条件
     * @return sql
     */
    String logicDeleteByMap(SqlProvider provider, Map<String, Object> map);

    /**
     * 根据WrapperData设置构建物理删除语句
     *
     * @param provider SqlProvide
     * @param ew       更新/查询 条件
     * @return sql
     */
    String deleteBy(SqlProvider provider, WrapperData ew);

    /**
     * 根据WrapperData设置构建逻辑删除语句
     *
     * @param provider SqlProvide
     * @param ew       更新/查询 条件
     * @return sql
     */
    String logicDeleteBy(SqlProvider provider, WrapperData ew);

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
     * 按K-V构造查询条件语句
     *
     * @param provider SqlProvide
     * @param where    查询条件
     * @return sql
     */
    String queryByMap(SqlProvider provider, Map where);

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