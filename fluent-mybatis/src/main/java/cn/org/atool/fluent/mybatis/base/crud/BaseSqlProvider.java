package cn.org.atool.fluent.mybatis.base.crud;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.model.InsertList;
import cn.org.atool.fluent.mybatis.mapper.MapperSql;
import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.fluent.mybatis.segment.model.WrapperData;
import cn.org.atool.fluent.mybatis.utility.MybatisUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.org.atool.fluent.mybatis.mapper.FluentConst.*;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.*;
import static cn.org.atool.fluent.mybatis.utility.SqlProviderUtils.*;
import static java.lang.String.format;

/**
 * SqlProvider: 动态SQL构造基类
 *
 * @author wudarui
 */
public abstract class BaseSqlProvider<E extends IEntity> {
    /**
     * 插入id未赋值的entity
     *
     * @param entity
     * @return
     */
    public String insert(E entity) {
        return insertEntity(entity, false);
    }

    /**
     * 插入id已赋值的entity
     *
     * @param entity
     * @return
     */
    public String insertWithPk(E entity) {
        return insertEntity(entity, true);
    }

    public String insertBatch(Map map) {
        assertNotEmpty(Param_List, map);
        List<E> entities = getParas(map, Param_List);
        return this.insertBatch(entities, false);
    }

    public String insertBatchWithPk(Map map) {
        assertNotEmpty(Param_List, map);
        List<E> entities = getParas(map, Param_List);
        return this.insertBatch(entities, true);
    }

    private String insertEntity(E entity, boolean withPk) {
        assertNotNull(Param_Entity, entity);
        this.validateInsertEntity(entity, withPk);
        MapperSql sql = new MapperSql();
        sql.INSERT_INTO(this.tableName());
        InsertList inserts = new InsertList();
        this.insertEntity(inserts, entity, withPk);
        sql.INSERT_COLUMNS(inserts.columns);
        sql.VALUES();
        sql.INSERT_VALUES(inserts.values);
        return sql.toString();
    }

    private String insertBatch(List<E> entities, boolean withPk) {
        MapperSql sql = new MapperSql();
        for (E entity : entities) {
            this.validateInsertEntity(entity, withPk);
        }
        sql.INSERT_INTO(this.tableName());
        sql.INSERT_COLUMNS(this.allFields(withPk));
        sql.VALUES();
        for (int index = 0; index < entities.size(); index++) {
            if (index > 0) {
                sql.APPEND(", ");
            }
            sql.INSERT_COLUMNS(this.insertBatchEntity(index, entities.get(index), withPk));
        }
        return sql.toString();
    }

    /**
     * 设置默认值，校验pk设置是否合法
     *
     * @param entity
     * @param withPk
     */
    private void validateInsertEntity(E entity, boolean withPk) {
        this.setEntityByDefault(entity);
        if (withPk) {
            isTrue(this.primaryNotNull(entity), "the pk of insert entity can't be null.");
        } else {
            isTrue(this.primaryIsNull(entity), "the pk of insert entity must be null.");
        }
    }

    /**
     * 单个插入时, insert字段和值
     *
     * @param inserts
     * @param entity
     * @param withPk
     */
    protected abstract void insertEntity(InsertList inserts, E entity, boolean withPk);

    /**
     * 批量插入时，第index个实例VALUES SQL构造
     *
     * @param index
     * @param entity
     * @param withPk
     * @return
     */
    protected abstract List<String> insertBatchEntity(int index, E entity, boolean withPk);

    /**
     * entity无主键或者主键值为null
     *
     * @param entity
     * @return
     */
    protected abstract boolean primaryIsNull(E entity);

    /**
     * entity无主键或者主键有值
     *
     * @param entity
     * @return
     */
    protected abstract boolean primaryNotNull(E entity);

    /**
     * 去掉limit部分 count(IQuery) SQL构造
     *
     * @param map
     * @return
     */
    public String countNoLimit(Map map) {
        WrapperData data = getWrapperData(map, Param_EW);
        MapperSql sql = new MapperSql();
        sql.COUNT(data.getTable(), data);
        sql.WHERE_GROUP_BY(data);
        return sql.toString();
    }

    /**
     * count(IQuery) SQL构造
     *
     * @param map
     * @return
     */
    public String count(Map map) {
        WrapperData data = getWrapperData(map, Param_EW);
        MapperSql sql = new MapperSql();
        sql.COUNT(data.getTable(), data);
        sql.WHERE_GROUP_ORDER_BY(data);
        return byPaged(this.dbType(), data, sql.toString());
    }

    /**
     * 根据动态条件查询Entity SQL构造
     *
     * @param map
     * @return
     */
    public String listEntity(Map map) {
        WrapperData data = getWrapperData(map, Param_EW);
        MapperSql sql = new MapperSql();
        sql.SELECT(data.getTable(), data, this.allFields(true));
        sql.WHERE_GROUP_ORDER_BY(data);
        return byPaged(this.dbType(), data, sql.toString());
    }

    /**
     * 根据动态条件查询Map列表 SQL构造
     *
     * @param map
     * @return
     */
    public String listMaps(Map map) {
        WrapperData data = getWrapperData(map, Param_EW);
        MapperSql sql = new MapperSql();
        sql.SELECT(data.getTable(), data, this.allFields(true));
        sql.WHERE_GROUP_ORDER_BY(data);
        return byPaged(this.dbType(), data, sql.toString());
    }

    /**
     * 根据动态条件查询单列数据列表 SQL构造
     *
     * @param map
     * @return
     */
    public String listObjs(Map map) {
        WrapperData data = getWrapperData(map, Param_EW);
        MapperSql sql = new MapperSql();
        sql.SELECT(data.getTable(), data, this.allFields(true));
        sql.WHERE_GROUP_ORDER_BY(data);
        return byPaged(this.dbType(), data, sql.toString());
    }

    /**
     * 根据Map查询数据SQL构造
     *
     * @param map
     * @return
     */
    public String listByMap(Map map) {
        Map<String, Object> where = getParas(map, Param_CM);
        assertNotEmpty("where", where);
        MapperSql sql = new MapperSql();
        sql.SELECT(this.tableName(), this.allFields(true));
        sql.WHERE(Param_CM, where);
        return sql.toString();
    }

    /**
     * 根据Id列表查询数据SQL构造
     *
     * @param map
     * @return
     */
    public String listByIds(Map map) {
        MapperSql sql = new MapperSql();
        Collection ids = getParas(map, Param_Coll);
        assertNotEmpty("PrimaryKeyList", ids);
        sql.SELECT(this.tableName(), this.allFields(true));
        sql.WHERE_PK_IN(this.idColumn(), ids.size());
        return sql.toString();
    }

    /**
     * 根据主键查找数据SQL构造
     *
     * @param id
     * @return
     */
    public String findById(Serializable id) {
        assertNotNull("PrimaryKey", id);
        MapperSql sql = new MapperSql();
        sql.SELECT(this.tableName(), this.allFields(true));
        sql.WHERE(format("%s = #{value}", this.idColumn()));
        return sql.toString();
    }

    /**
     * 根据动态条件查询一条记录SQL构造
     *
     * @param map
     * @return
     */
    public String findOne(Map map) {
        WrapperData data = getWrapperData(map, Param_EW);
        MapperSql sql = new MapperSql();
        sql.SELECT(data.getTable(), data, this.allFields(true));
        sql.WHERE_GROUP_ORDER_BY(data);
        return byPaged(this.dbType(), data, sql.toString());
    }

    /**
     * 根据主键物理删除数据SQL构造
     *
     * @param id 主键值
     * @return
     */
    public String deleteById(Serializable id) {
        MybatisUtil.assertNotNull("PrimaryKey", id);
        MapperSql sql = new MapperSql();
        sql.DELETE_FROM(this.tableName(), null);
        sql.WHERE(format("%s = #{value}", this.idColumn()));
        return sql.toString();
    }

    /**
     * 根据主键列表物理删除数据SQL构造
     *
     * @param map
     * @return
     */
    public String deleteByIds(Map map) {
        Collection ids = getParas(map, Param_Coll);
        assertNotEmpty("PrimaryKeyList", ids);
        MapperSql sql = new MapperSql();
        sql.DELETE_FROM(this.tableName(), null);
        sql.WHERE_PK_IN(this.idColumn(), ids.size());
        return sql.toString();
    }

    /**
     * 按map删除数据SQL构造
     *
     * @param map
     * @return
     */
    public String deleteByMap(Map<String, Object> map) {
        Map<String, Object> cm = getParas(map, Param_CM);
        MapperSql sql = new MapperSql();
        sql.DELETE_FROM(this.tableName(), null);
        List<String> where = new ArrayList<>();
        for (String key : cm.keySet()) {
            where.add(format("%s = #{%s.%s}", key, Param_CM, key));
        }
        sql.WHERE(where);
        return sql.toString();
    }

    /**
     * 根据动态查询条件物理删除数据SQL构造
     *
     * @param map
     * @return
     */
    public String delete(Map map) {
        WrapperData data = getWrapperData(map, Param_EW);
        MapperSql sql = new MapperSql();
        sql.DELETE_FROM(this.tableName(), data);
        sql.WHERE_GROUP_ORDER_BY(data);
        return sql.toString();
    }

    final static String Para_Regex = "#\\{ew\\.wrapperData\\.parameters\\.";

    final static String Para_Format = "#{ew[%d].wrapperData.parameters.";

    /**
     * update(IQuery) SQL构造
     *
     * @param map
     * @return
     */
    public String updateBy(Map<String, Object> map) {
        IUpdate[] updaters = (IUpdate[]) map.get(Param_EW);
        assertNotEmpty(Param_EW, updaters);
        List<String> list = new ArrayList<>(updaters.length);
        int index = 0;
        for (IUpdate updater : updaters) {
            String sql = this.getUpdaterSql(updater);
            /**
             * 替换变量占位符, 数组下标方式
             */
            sql = sql.replaceAll(Para_Regex, format(Para_Format, index));
            index++;
            list.add(sql);
        }
        String text = list.stream().collect(Collectors.joining(";\n"));
        return text;
    }

    private String getUpdaterSql(IUpdate updater) {
        WrapperData data = updater.getWrapperData();
        assertNotNull("wrapperData of updater", data);
        Map<String, String> updates = data.getUpdates();
        assertNotEmpty("updates", updates);

        MapperSql sql = new MapperSql();
        sql.UPDATE(data.getTable(), data);
        List<String> sets = this.updateDefaults(updates);
        sets.add(data.getUpdateStr());
        sql.SET(sets);
        sql.WHERE_GROUP_ORDER_BY(data);
        sql.LIMIT(data, true);
        return sql.toString();
    }

    /**
     * 根据{@link cn.org.atool.fluent.mybatis.base.crud.IDefaultSetter#setInsertDefault(IEntity)}设置默认值
     *
     * @param entity
     */
    protected abstract void setEntityByDefault(IEntity entity);

    /**
     * 构造updates中没有显式设置的默认值构造
     *
     * @param updates 显式update字段
     * @return
     */
    protected abstract List<String> updateDefaults(Map<String, String> updates);

    /**
     * 返回表名
     *
     * @return
     */
    protected abstract String tableName();

    /**
     * 主键字段名
     *
     * @return
     */
    protected abstract String idColumn();

    /**
     * 所有字段列表(以逗号隔开)
     *
     * @param withPk 是否包含主键字段
     * @return
     */
    protected abstract String allFields(boolean withPk);

    /**
     * 数据库类型
     *
     * @return
     */
    protected abstract DbType dbType();
}