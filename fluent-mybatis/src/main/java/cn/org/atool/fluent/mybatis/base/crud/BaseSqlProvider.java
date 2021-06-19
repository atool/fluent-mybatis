package cn.org.atool.fluent.mybatis.base.crud;

import cn.org.atool.fluent.mybatis.If;
import cn.org.atool.fluent.mybatis.base.BatchCrud;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.mapper.IEntityMapper;
import cn.org.atool.fluent.mybatis.base.model.InsertList;
import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;
import cn.org.atool.fluent.mybatis.mapper.MapperSql;
import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.fluent.mybatis.segment.model.WrapperData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static cn.org.atool.fluent.mybatis.mapper.FluentConst.*;
import static cn.org.atool.fluent.mybatis.mapper.StrConstant.EMPTY;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.*;
import static cn.org.atool.fluent.mybatis.utility.SqlProviderUtils.*;
import static java.lang.Integer.min;
import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

/**
 * SqlProvider: 动态SQL构造基类
 *
 * @author wudarui
 */
public abstract class BaseSqlProvider<E extends IEntity> {
    /**
     * 批量更新, 插入, 删除操作语句构造
     * {@link IEntityMapper#batchCrud(BatchCrud)}
     *
     * @param map
     * @return
     */
    public static String batchCrud(Map map) {
        IWrapper wrapper = getWrapper(map, Param_EW);
        if (!(wrapper instanceof BatchCrudImpl)) {
            throw new IllegalArgumentException("the wrapper should be an instance of BatchUpdaterImpl.");
        }
        return ((BatchCrudImpl) wrapper).batchSql();
    }

    /**
     * 构造{@link IEntityMapper#insertSelect(String[], IQuery)}SQL语句
     *
     * @param map
     * @return
     */
    public String insertSelect(Map map) {
        String[] fields = (String[]) map.get(Param_Fields);
        IQuery query = (IQuery) map.get(Param_EW);
        return buildInsertSelect(this.tableName(), fields, query);
    }

    public static String buildInsertSelect(String tableName, String[] fields, IQuery query) {
        assertNotBlank("tableName", tableName);
        assertNotEmpty(Param_Fields, fields);
        assertNotNull(Param_EW, query);
        StringBuilder buff = new StringBuilder("INSERT INTO ")
            .append(tableName)
            .append(" (")
            .append(String.join(", ", fields))
            .append(") ")
            .append(query.getWrapperData().getQuerySql());

        return buff.toString();
    }

    /**
     * 插入id未赋值的entity
     *
     * @param entity
     * @return
     */
    public String insert(E entity) {
        return buildInsertSql(EMPTY, entity, false);
    }

    /**
     * 插入id已赋值的entity
     *
     * @param entity
     * @return
     */
    public String insertWithPk(E entity) {
        return buildInsertSql(EMPTY, entity, true);
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

    private String insertBatch(List<E> entities, boolean withPk) {
        MapperSql sql = new MapperSql();
        for (E entity : entities) {
            this.validateInsertEntity(entity, withPk);
        }
        sql.INSERT_INTO(this.tableName());
        sql.INSERT_COLUMNS(dbType(), this.allFields(withPk));
        sql.VALUES();
        for (int index = 0; index < entities.size(); index++) {
            if (index > 0) {
                sql.APPEND(", ");
            }
            sql.INSERT_VALUES(this.insertBatchEntity(index, entities.get(index), withPk));
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
    protected abstract void insertEntity(InsertList inserts, String prefix, E entity, boolean withPk);

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
        WrapperData ew = getWrapperData(map, Param_EW);
        if (If.notBlank(ew.getCustomizedSql())) {
            return ew.getCustomizedSql();
        }
        MapperSql sql = new MapperSql();
        sql.COUNT(ew.getTable(), ew);
        sql.WHERE_GROUP_BY(ew);
        return sql.toString();
    }

    /**
     * count(IQuery) SQL构造
     *
     * @param map
     * @return
     */
    public String count(Map map) {
        WrapperData ew = getWrapperData(map, Param_EW);
        if (If.notBlank(ew.getCustomizedSql())) {
            return ew.getCustomizedSql();
        }
        MapperSql sql = new MapperSql();
        sql.COUNT(ew.getTable(), ew);
        sql.WHERE_GROUP_ORDER_BY(ew);
        return byPaged(this.dbType(), ew, sql.toString());
    }

    private String queryByWrapperData(Map map) {
        WrapperData ew = getWrapperData(map, Param_EW);
        if (If.notBlank(ew.getCustomizedSql())) {
            return ew.getCustomizedSql();
        }
        MapperSql sql = new MapperSql();
        sql.SELECT(ew.getTable(), ew, this.joiningAllFields(true));
        sql.WHERE_GROUP_ORDER_BY(ew);
        return byPaged(this.dbType(), ew, sql.toString());
    }

    /**
     * 根据动态条件查询Entity SQL构造
     *
     * @param map
     * @return
     */
    public String listEntity(Map map) {
        return this.queryByWrapperData(map);
    }

    /**
     * 根据动态条件查询Map列表 SQL构造
     *
     * @param map
     * @return
     */
    public String listMaps(Map map) {
        return this.queryByWrapperData(map);
    }

    /**
     * 根据动态条件查询单列数据列表 SQL构造
     *
     * @param map
     * @return
     */
    public String listObjs(Map map) {
        return this.queryByWrapperData(map);
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
        sql.SELECT(this.tableName(), this.joiningAllFields(true));
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
        sql.SELECT(this.tableName(), this.joiningAllFields(true));
        sql.WHERE_PK_IN(this.dbType().wrap(this.idColumn()), ids.size());
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
        sql.SELECT(this.tableName(), this.joiningAllFields(true));
        sql.WHERE(format("%s = #{value}", this.idColumn()));
        return sql.toString();
    }

    private String joiningAllFields(boolean withPk) {
        return this.allFields(withPk).stream()
            .map(dbType()::wrap)
            .collect(joining(", "));
    }

    /**
     * 根据动态条件查询一条记录SQL构造
     *
     * @param map
     * @return
     */
    public String findOne(Map map) {
        return this.queryByWrapperData(map);
    }

    /**
     * 根据主键物理删除数据SQL构造
     *
     * @param ids 主键值
     * @return
     */
    public String deleteById(Serializable[] ids) {
        assertNotEmpty("ids", ids);
        MapperSql sql = new MapperSql();
        sql.DELETE_FROM(this.tableName(), null);
        this.whereEqIds(sql, ids);
        return sql.toString();
    }

    /**
     * 根据主键逻辑删除数据SQL构造
     *
     * @param ids 主键值
     * @return
     */
    public String logicDeleteById(Serializable[] ids) {
        assertNotEmpty("ids", ids);
        MapperSql sql = new MapperSql();
        this.logicDelete(sql);
        this.whereEqIds(sql, ids);
        return sql.toString();
    }

    private void whereEqIds(MapperSql sql, Serializable[] ids) {
        String idColumn = this.dbType().wrap(this.idColumn());
        if (ids.length == 1) {
            sql.WHERE(format("%s = #{array[0]}", idColumn));
        } else {
            StringBuilder values = new StringBuilder();
            for (int index = 0; index < ids.length; index++) {
                if (index > 0) {
                    values.append(", ");
                }
                values.append("#{array[" + index + "]}");
            }
            sql.WHERE(format("%s IN (%s)", idColumn, values));
        }
    }

    /**
     * 根据主键列表物理删除数据SQL构造
     *
     * @param map
     * @return
     */
    public String deleteByIds(Map map) {
        Collection ids = getParas(map, Param_Coll);
        assertNotEmpty("ids", ids);
        MapperSql sql = new MapperSql();
        sql.DELETE_FROM(this.tableName(), null);
        sql.WHERE_PK_IN(this.dbType().wrap(this.idColumn()), ids.size());
        return sql.toString();
    }

    /**
     * 根据主键列表逻辑删除数据SQL构造
     *
     * @param map
     * @return
     */
    public String logicDeleteByIds(Map map) {
        Collection ids = getParas(map, Param_Coll);
        assertNotEmpty("ids", ids);
        MapperSql sql = new MapperSql();
        this.logicDelete(sql);
        sql.WHERE_PK_IN(this.dbType().wrap(this.idColumn()), ids.size());
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
        this.whereByMap(sql, cm);
        return sql.toString();
    }

    /**
     * 按map逻辑删除数据SQL构造
     *
     * @param map
     * @return
     */
    public String logicDeleteByMap(Map<String, Object> map) {
        Map<String, Object> cm = getParas(map, Param_CM);
        MapperSql sql = new MapperSql();
        this.logicDelete(sql);
        this.whereByMap(sql, cm);
        return sql.toString();
    }

    /**
     * 按map构造条件语句
     *
     * @param sql sql构造器
     * @param map key-value条件
     */
    private void whereByMap(MapperSql sql, Map<String, Object> map) {
        List<String> where = new ArrayList<>();
        for (String key : map.keySet()) {
            where.add(format("%s = #{%s.%s}", dbType().wrap(key), Param_CM, key));
        }
        sql.WHERE(where);
    }

    /**
     * 根据动态查询条件物理删除数据SQL构造
     *
     * @param map
     * @return
     */
    public String delete(Map map) {
        WrapperData ew = getWrapperData(map, Param_EW);
        return buildDeleteSql(ew, false);
    }

    /**
     * 根据动态查询条件逻辑删除数据SQL构造
     *
     * @param map
     * @return
     */
    public String logicDelete(Map map) {
        WrapperData ew = getWrapperData(map, Param_EW);
        return buildDeleteSql(ew, true);
    }

    /**
     * update(IQuery) SQL构造
     *
     * @param map
     * @return
     */
    public String updateBy(Map<String, Object> map) {
        Object wrapper = map.get(Param_EW);
        if (If.isEmpty(wrapper)) {
            throw FluentMybatisException.instance("the parameter[%s] can't be empty.", Param_EW);
        }
        if (!(wrapper instanceof IUpdate[])) {
            throw new IllegalArgumentException("the parameter should be an array of IUpdate");
        }
        IUpdate[] updaters = (IUpdate[]) wrapper;
        List<String> list = new ArrayList<>(updaters.length);
        int index = 0;
        for (IUpdate updater : updaters) {
            String sql = this.buildUpdaterSql(updater.getWrapperData());
            sql = this.addEwParaIndex(sql, format("[%d]", index));
            index++;
            list.add(sql);
        }
        String text = list.stream().collect(joining(";\n"));
        return text;
    }

    private final static char[] EW_CONST = "#{ew.".toCharArray();

    private final static String sub = ".wrapperData.parameters.";

    /**
     * 替换变量占位符, 增加ew数组下标
     * #{ew.wrapperData.parameters.xxx}替换为#{ew[0].wrapperData.parameters.xxx}
     * 不采用正则表达式方式替换, 是编码方式替换简单，一次字符串扫描即可完成
     *
     * @param sql
     * @param aIndex
     * @return
     */
    static String addEwParaIndex(String sql, String aIndex) {
        StringBuilder buff = new StringBuilder();
        int match = 0;
        int len = sql.length();
        for (int loop = 0; loop < sql.length(); loop++) {
            char ch = sql.charAt(loop);
            if (match == 4) {
                if (sql.substring(loop, min(sub.length() + loop, len)).equals(sub)) {
                    buff.append(aIndex);
                }
                match = 0;
            } else {
                match = EW_CONST[match] == ch ? match + 1 : 0;
            }
            buff.append(ch);
        }
        return buff.toString();
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
     * 构建插入语句
     *
     * @param prefix entity变量前缀
     * @param entity
     * @param withPk 包含主键?
     * @return
     */
    public String buildInsertSql(String prefix, E entity, boolean withPk) {
        assertNotNull(Param_Entity, entity);
        this.validateInsertEntity(entity, withPk);
        MapperSql sql = new MapperSql();
        sql.INSERT_INTO(this.tableName());
        InsertList inserts = new InsertList();
        this.insertEntity(inserts, prefix, entity, withPk);
        sql.INSERT_COLUMNS(dbType(), inserts.columns);
        sql.VALUES();
        sql.INSERT_VALUES(inserts.values);
        return sql.toString();
    }

    /**
     * 根据WrapperData设置构建更新语句
     *
     * @param ew
     * @return
     */
    public String buildUpdaterSql(WrapperData ew) {
        assertNotNull("wrapperData of updater", ew);
        if (If.notBlank(ew.getCustomizedSql())) {
            return ew.getCustomizedSql();
        }
        Map<String, String> updates = ew.getUpdates();
        assertNotEmpty("updates", updates);

        MapperSql sql = new MapperSql();
        sql.UPDATE(ew.getTable(), ew);
        List<String> needDefaults = this.updateDefaults(updates);
        needDefaults.add(ew.getUpdateStr());
        sql.SET(needDefaults);
        this.checkUpdateVersionWhere(ew.findWhereColumns());
        sql.WHERE_GROUP_ORDER_BY(ew);
        sql.LIMIT(ew, true);
        return sql.toString();
    }

    /**
     * 更新时, 检查乐观锁字段条件是否设置
     */
    protected void checkUpdateVersionWhere(List<String> wheres) {
        String versionField = this.versionField();
        if (If.isBlank(versionField)) {
            return;
        } else if (wheres.contains(versionField) || wheres.contains(this.dbType().wrap(versionField))) {
            return;
        } else {
            throw new RuntimeException("The version lock field was explicitly set, but no version condition was found in the update condition.");
        }
    }

    /**
     * 根据WrapperData设置构建删除语句
     *
     * @param ew
     * @return
     */
    public String buildDeleteSql(WrapperData ew, boolean isLogic) {
        if (If.notBlank(ew.getCustomizedSql())) {
            return ew.getCustomizedSql();
        }
        MapperSql sql = new MapperSql();
        if (isLogic) {
            this.logicDelete(sql);
        } else {
            sql.DELETE_FROM(this.tableName(), ew);
        }
        sql.WHERE_GROUP_ORDER_BY(ew);
        return sql.toString();
    }

    /**
     * 构造 UPDATE table SET logic_delete_field = true 语句
     *
     * @param sql sql构造器
     */
    private void logicDelete(MapperSql sql) {
        assertNotNull("logical delete field of table(" + this.tableName() + ")", this.logicDeleteField());
        sql.UPDATE(this.tableName(), null);
        if (this.longTypeOfLogicDelete()) {
            sql.SET(String.format("%s = %d", dbType().wrap(this.logicDeleteField()), System.currentTimeMillis()));
        } else {
            sql.SET(String.format("%s = true", dbType().wrap(this.logicDeleteField())));
        }
    }

    /**
     * 数据库类型
     *
     * @return
     */
    protected abstract DbType dbType();

    /**
     * 返回表名
     *
     * @return
     */
    protected abstract String tableName();

    /**
     * 所有字段列表(以逗号隔开)
     *
     * @param withPk 是否包含主键字段
     * @return
     */
    protected abstract List<String> allFields(boolean withPk);

    /**
     * 主键字段名
     *
     * @return
     */
    protected abstract String idColumn();

    /**
     * 乐观锁字段
     *
     * @return
     */
    protected abstract String versionField();

    /**
     * 逻辑删除字段
     *
     * @return
     */
    protected abstract String logicDeleteField();

    /**
     * 逻辑删除字段是否为 Long 型
     *
     * @return
     */
    protected abstract boolean longTypeOfLogicDelete();
}