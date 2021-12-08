package cn.org.atool.fluent.mybatis.base.provider;

import cn.org.atool.fluent.mybatis.If;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.BaseQuery;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.crud.IUpdate;
import cn.org.atool.fluent.mybatis.base.crud.IWrapper;
import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.base.entity.IRichEntity;
import cn.org.atool.fluent.mybatis.base.entity.PkGeneratorKits;
import cn.org.atool.fluent.mybatis.base.entity.TableId;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.base.model.InsertList;
import cn.org.atool.fluent.mybatis.base.model.SqlOp;
import cn.org.atool.fluent.mybatis.base.model.UpdateDefault;
import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;
import cn.org.atool.fluent.mybatis.mapper.MapperSql;
import cn.org.atool.fluent.mybatis.segment.fragment.Column;
import cn.org.atool.fluent.mybatis.segment.fragment.IFragment;
import cn.org.atool.fluent.mybatis.segment.fragment.JoiningFrag;
import cn.org.atool.fluent.mybatis.segment.model.WrapperData;
import cn.org.atool.fluent.mybatis.utility.RefKit;
import cn.org.atool.fluent.mybatis.utility.SqlProviderKit;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cn.org.atool.fluent.mybatis.If.isBlank;
import static cn.org.atool.fluent.mybatis.If.notBlank;
import static cn.org.atool.fluent.mybatis.base.model.InsertList.el;
import static cn.org.atool.fluent.mybatis.mapper.FluentConst.*;
import static cn.org.atool.fluent.mybatis.mapper.MapperSql.brackets;
import static cn.org.atool.fluent.mybatis.mapper.MapperSql.tmpTable;
import static cn.org.atool.fluent.mybatis.mapper.StrConstant.*;
import static cn.org.atool.fluent.mybatis.segment.fragment.KeyFrag.*;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.*;
import static java.lang.String.format;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

/**
 * 通用SQL构造器
 *
 * @author darui.wu
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class CommonSqlKit implements SqlKit {
    public CommonSqlKit() {
    }

    @Override
    public KeyGenerator insert(StatementBuilder builder, FieldMapping primary, TableId tableId) {
        if (tableId == null) {
            return NoKeyGenerator.INSTANCE;
        } else if (this.isAutoKeyGenerator(tableId)) {
            return Jdbc3KeyGenerator.INSTANCE;
        } else {
            return builder.handleSelectKey(primary, tableId);
        }
    }

    @Override
    public KeyGenerator insertBatch(IMapping mapping, StatementBuilder builder, FieldMapping primary, TableId tableId) {
        if (tableId == null) {
            return NoKeyGenerator.INSTANCE;
        } else if (this.isAutoKeyGenerator(tableId)) {
            return Jdbc3KeyGenerator.INSTANCE;
        } else if (tableId.isSeqBefore(mapping.db())) {
            /* 使用 select insert 方式 */
            return NoKeyGenerator.INSTANCE;
        } else {
            return builder.handleSelectKey(primary, tableId);
        }
    }

    protected boolean isAutoKeyGenerator(TableId tableId) {
        return tableId.auto && isBlank(tableId.seqName);
    }

    @Override
    public <E extends IEntity> String insertEntity(IMapping mapping, String prefix, E entity, boolean withPk) {
        assertNotNull(Param_Entity, entity);

        withPk = validateInsertEntity(mapping, entity, withPk, mapping.defaultSetter()::setInsertDefault, mapping.tableId());
        MapperSql sql = new MapperSql();
        sql.INSERT_INTO(dynamic(entity, mapping));
        InsertList inserts = this.insertColumns(mapping, prefix, entity, withPk);
        sql.INSERT_COLUMNS(mapping, inserts.columns);
        sql.VALUES();
        sql.APPEND(brackets(COMMA_SPACE, inserts.values));
        return sql.toString();
    }

    /**
     * 单个插入时, insert字段和值
     *
     * @param entity 实体实例
     * @param withPk true:with id; false: without id
     */
    private InsertList insertColumns(IMapping mapping, String prefix, IEntity entity, boolean withPk) {
        InsertList inserts = new InsertList();
        List<FieldMapping> fields = mapping.allFields();
        Map map = entity.toEntityMap();
        for (FieldMapping f : fields) {
            if (!f.isPrimary() || withPk) {
                inserts.add(prefix, f, map.get(f.name), f.insert);
            }
        }
        return inserts;
    }

    @Override
    public String insertSelect(IMapping mapping, String tableName, String[] fields, IQuery query) {
        assertNotBlank("tableName", tableName);
        String columns = Stream.of(fields).map(mapping.db()::wrap).collect(joining(", "));
        if (isBlank(columns)) {
            List<String> list = new ArrayList<>();
            List<String> items = splitByComma(query.data().getSelect().get(mapping));
            for (String item : items) {
                String[] arr = splitBySpace(item).stream().filter(If::notBlank).toArray(String[]::new);
                list.add(arr[arr.length - 1]);
            }
            columns = String.join(COMMA_SPACE, list);
        }
        assertNotBlank(Param_Fields, columns);
        assertNotNull(Param_EW, query);
        if (!query.data().hasSelect()) {
            ((BaseQuery) query).select(fields);
        }
        return joinWithSpace(
            INSERT_INTO.key(), tableName,
            brackets(columns),
            query.data().sql(false).get(mapping));
    }

    @Override
    public <E extends IEntity> String insertBatch(IMapping mapping, List<E> entities, boolean withPk, TableId tableId) {
        MapperSql sql = new MapperSql();
        List<Map> maps = this.toMaps(mapping, entities, withPk);
        /* 所有非空字段 */
        List<FieldMapping> nonFields = this.nonFields(mapping, maps, withPk);
        String tableName = dynamic(entities, mapping);
        sql.INSERT_INTO(tableName);

        if (this.isSelectInsert(mapping, withPk, tableId)) {
            this.insertSelect(mapping, tableId, withPk, sql, maps, nonFields);
        } else {
            this.insertValues(mapping, sql, maps, nonFields);
        }
        return sql.toString();
    }

    /**
     * 是否用 insert select 方式批量插入
     */
    protected boolean isSelectInsert(IMapping mapping, boolean withPk, TableId tableId) {
        return !withPk && tableId != null && tableId.isSeqBefore(mapping.db());
    }

    /**
     * https://blog.csdn.net/w_y_t_/article/details/51416201
     * <p>
     * https://www.cnblogs.com/xunux/p/4882761.html
     * <p>
     * https://blog.csdn.net/weixin_41175479/article/details/80608512
     */
    protected void insertSelect(IMapping mapping, TableId tableId, boolean withPk, MapperSql sql, List<Map> maps, List<FieldMapping> nonFields) {
        List<String> columns = new ArrayList<>();
        if (!withPk && tableId != null) {
            columns.add(tableId.column);
        }
        nonFields.stream().map(f -> f.column).forEach(columns::add);
        sql.INSERT_COLUMNS(mapping, columns);

        sql.APPEND("SELECT");
        if (!withPk && tableId != null) {
            String seq = isBlank(tableId.seqName) ? mapping.db().feature.getSeq() : tableId.seqName;
            sql.APPEND(getSeq(seq) + ",");
        }
        sql.APPEND("TMP.* FROM (");
        for (int index = 0; index < maps.size(); index++) {
            if (index > 0) {
                sql.APPEND(" UNION ALL ");
            }
            sql.APPEND("(SELECT");
            boolean first = true;
            for (FieldMapping f : nonFields) {
                if (f.isPrimary() && !withPk) {
                    continue;
                }
                if (!first) {
                    sql.APPEND(COMMA_SPACE);
                } else {
                    first = false;
                }
                Object value = maps.get(index).get(f.column);
                if (value == null && notBlank(f.insert)) {
                    sql.APPEND(f.insert);
                } else {
                    String el = el("list[" + index + "].", f, value, EMPTY);
                    sql.APPEND(isBlank(el) ? f.column : el);
                }
            }
            sql.APPEND(" FROM DUAL)");
        }
        sql.APPEND(") TMP");
    }

    protected void insertValues(IMapping mapping, MapperSql sql, List<Map> maps, List<FieldMapping> nonFields) {
        List<String> columns = new ArrayList<>();
        nonFields.stream().map(f -> f.column).forEach(columns::add);
        sql.INSERT_COLUMNS(mapping, columns);

        sql.VALUES();
        List<String> values = new ArrayList<>();
        for (int index = 0; index < maps.size(); index++) {
            List<String> lines = new ArrayList<>();
            for (FieldMapping f : nonFields) {
                lines.add(el("list[" + index + "].", f, maps.get(index).get(f.column), f.insert));
            }
            values.add(brackets(COMMA_SPACE, lines));
        }
        sql.APPEND(String.join(COMMA_SPACE, values));
    }

    @Override
    public IUpdate logicDeleteByIds(IMapping mapping, Collection ids) {
        return this.logicDeleteByIds(mapping, ids.toArray());
    }

    @Override
    public IUpdate logicDeleteByIds(IMapping mapping, Object[] ids) {
        assertNotEmpty("ids", ids);
        /* 根据id的条件不用附加默认条件 */
        IUpdate updater = mapping.updater();
        updater.data().segments().where.clear();
        /* 逻辑删除忽略版本号 */
        updater.data().setIgnoreLockVersion(true);
        String logicDeleteColumn = mapping.logicDeleteColumn();
        if (isBlank(logicDeleteColumn)) {
            throw new FluentMybatisException("logic delete column(@LogicDelete) not found.");
        }
        if (mapping.longTypeOfLogicDelete()) {
            updater.updateSet(logicDeleteColumn, currentTimeMillis());
        } else {
            updater.updateSet(logicDeleteColumn, true);
        }
        String primary = mapping.primaryId(true);
        if (ids.length == 1) {
            updater.where().apply(primary, SqlOp.EQ, ids[0]);
        } else {
            updater.where().apply(primary, SqlOp.IN, ids);
        }
        return updater;
    }

    @Override
    public IQuery queryByIds(IMapping mapping, Collection ids) {
        return this.queryByIds(mapping, ids.toArray());
    }

    @Override
    public IQuery queryByIds(IMapping mapping, Object[] ids) {
        assertNotEmpty("ids", ids);
        /* 根据id的条件不用附加默认条件 */
        IQuery query = mapping.emptyQuery();
        String primary = mapping.primaryId(true);
        if (ids.length == 1) {
            query.where().apply(primary, SqlOp.EQ, ids[0]);
        } else {
            query.where().apply(primary, SqlOp.IN, ids);
        }
        return query;
    }

    @Override
    public String deleteBy(IMapping mapping, WrapperData ew) {
        if (ew.getCustomizedSql().notEmpty()) {
            return ew.getCustomizedSql().get(mapping);
        } else {
            MapperSql mapperSql = new MapperSql();
            mapperSql.DELETE_FROM(mapping, ew.table(), ew);
            mapperSql.WHERE_GROUP_ORDER_BY(mapping, ew);
            return mapperSql.toString();
        }
    }

    @Override
    public void setLogicDeleted(IMapping mapping, IUpdate update) {
        String logicDeleted = mapping.logicDeleteColumn();
        assertNotNull("logical delete field of table(" + mapping.getTableName() + ")", logicDeleted);
        if (mapping.longTypeOfLogicDelete()) {
            update.updateSet(logicDeleted, currentTimeMillis());
        } else {
            update.updateSet(logicDeleted, true);
        }
    }

    @Override
    public void eqByMap(IMapping mapping, IWrapper wrapper, boolean isColumn, Map<String, Object> condition) {
        Map<String, FieldMapping> fields = isColumn ? mapping.getColumnMap() : mapping.getFieldsMap();
        for (Map.Entry<String, Object> entry : condition.entrySet()) {
            String key = entry.getKey();
            FieldMapping f = fields.get(key);
            if (f == null) {
                String err = isColumn
                    ? "Column[" + key + "] of Table[" + mapping.getTableName() + "]"
                    : "Field[" + key + "] of Entity[" + mapping.entityClass().getSimpleName() + "]";
                throw new FluentMybatisException(err + " is not found.");
            }
            Object value = entry.getValue();
            if (value == null) {
                wrapper.where().apply(f.column, SqlOp.IS_NULL);
            } else {
                wrapper.where().apply(f.column, SqlOp.EQ, value);
            }
        }
    }

    @Override
    public IUpdate logicDeleteBy(IMapping mapping, IQuery query) {
        if (query.data().getCustomizedSql().notEmpty()) {
            throw new FluentMybatisException("Logical deletion does not support custom SQL.");
        } else {
            IUpdate update = mapping.updater();
            this.setLogicDeleted(mapping, update);
            update.data().replacedByQuery(query);
            return update;
        }
    }

    private static long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    @Override
    public String updateBy(IMapping mapping, IUpdate[] updaters) {
        List<String> list = new ArrayList<>(updaters.length);
        int index = 0;
        for (IUpdate updater : updaters) {
            String sql = updateBy(mapping, updater.data());
            sql = SqlProviderKit.addEwParaIndex(sql, format("[%d]", index));
            index++;
            list.add(sql.trim());
        }
        return String.join(SEMICOLON_NEWLINE, list);
    }

    @Override
    public String updateBy(IMapping mapping, WrapperData ew) {
        assertNotNull("data of updater", ew);
        if (ew.getCustomizedSql().notEmpty()) {
            return ew.getCustomizedSql().get(mapping);
        }
        Map<IFragment, String> updates = ew.getUpdates();
        assertNotEmpty("updates", updates);

        MapperSql mapperSql = new MapperSql();
        mapperSql.UPDATE(mapping, ew.table(), ew);
        JoiningFrag needDefaults = updateDefaults(mapping, ew.getWrapper(), updates, ew.ignoreVersion());
        // 如果忽略版本锁, 则移除版本锁更新的默认值
        String version = mapping.versionColumn();
        if (notBlank(version)) {
            if (ew.ignoreVersion()) {
                needDefaults.removeColumn(version);
            } else if (!ew.getEqWhere().containsKey(version)) {
                throw new RuntimeException("@Version field of where condition not set.");
            }
        }
        needDefaults.add(ew.update());
        mapperSql.SET(mapping, needDefaults);
        mapperSql.WHERE_GROUP_ORDER_BY(mapping, ew);
        mapperSql.LIMIT(ew, true);
        return mapperSql.toString();
    }

    @Override
    public IUpdate updateById(IMapping mapping, IEntity entity) {
        assertNotNull("entity", entity);
        IUpdate update = mapping.updater();
        /* 清空byId场景下默认条件设置 */
        update.data().segments().where.clear();

        List<FieldMapping> fields = mapping.allFields();
        FieldMapping primary = null;
        FieldMapping version = null;
        Map values = entity.toColumnMap();
        for (FieldMapping f : fields) {
            Object value = values.get(f.column);
            Column column = Column.set((IWrapper) update, f.column);
            if (f.isPrimary()) {
                primary = f;
            } else if (f.isVersion()) {
                version = f;
                update.data().updateSql(column, f.update);
            } else if (value != null) {
                update.updateSet(f.column, value);
            } else if (notBlank(f.update)) {
                update.data().updateSql(column, f.update);
            }
        }
        if (primary == null) {
            throw new IllegalArgumentException("Primary of entity[" + entity.entityClass().getSimpleName() + "] is not found.");
        } else {
            update.where().apply(primary.column, SqlOp.EQ, values.get(primary.column));
        }
        if (version != null) {
            assertNotNull("lock version field(" + version.name + ")", values.get(version.column));
            update.where().apply(version.column, SqlOp.EQ, values.get(version.column));
        }
        return update;
    }

    @Override
    public String countNoLimit(IMapping mapping, WrapperData ew) {
        if (ew.getCustomizedSql().notEmpty()) {
            return ew.getCustomizedSql().get(mapping);
        } else {
            return this.count(mapping, ew, false);
        }
    }

    @Override
    public String count(IMapping mapping, WrapperData ew) {
        if (ew.getCustomizedSql().notEmpty()) {
            return ew.getCustomizedSql().get(mapping);
        } else {
            return this.count(mapping, ew, true);
        }
    }

    private String count(IMapping mapping, WrapperData ew, boolean withLimit) {
        MapperSql text = new MapperSql();
        text.COUNT(mapping, ew.table(), ew);
        text.WHERE_GROUP_BY(mapping, ew);
        String sql = text.toString();
        if (withLimit) {
            sql = ew.wrappedByPaged(sql).get(mapping);
        }
        if (ew.hasGroupBy()) {
            return joinWithSpace(SELECT.key(), COUNT_ASTERISK, FROM.key(), brackets(sql), tmpTable());
        } else {
            return sql;
        }
    }

    @Override
    public String queryBy(IMapping mapping, WrapperData ew) {
        return ew.sql(true).get(mapping);
    }

    /**
     * 批量转换为Map
     *
     * @param mapping  IMapping
     * @param entities entity list
     * @param withPk   with pk column
     * @return entity map list
     */
    protected <E extends IEntity> List<Map> toMaps(IMapping mapping, List<E> entities, boolean withPk) {
        List<Map> maps = new ArrayList<>(entities.size());
        for (IEntity entity : entities) {
            validateInsertEntity(mapping, entity, withPk, mapping.defaultSetter()::setInsertDefault, mapping.tableId());
            maps.add(entity.toColumnMap());
        }
        return maps;
    }

    /**
     * 所有非空字段
     *
     * @param mapping IMapping
     * @param maps    entity列表
     * @param withPk  是否包含主键
     * @return 非空字段列表
     */
    protected List<FieldMapping> nonFields(IMapping mapping, List<Map> maps, boolean withPk) {
        Set<String> set = new HashSet<>();
        maps.forEach(m -> set.addAll(m.keySet()));
        List<FieldMapping> fields = mapping.allFields();
        return fields.stream()
            .filter(f -> set.contains(f.column) || notBlank(f.insert))
            .filter(f -> !f.isPrimary() || withPk)
            .collect(toList());
    }

    /**
     * 设置默认值，校验pk设置是否合法
     *
     * @param entity 实体实例
     * @param withPk true: 带id值插入; false: 不带id值插入
     */
    @SuppressWarnings("all")
    private boolean validateInsertEntity(IMapping mapping, IEntity entity, boolean withPk, Consumer<IEntity> setByDefault, TableId tableId) {
        Object oldId = entity.findPk();
        PkGeneratorKits.setPkByGenerator(entity);
        Object newId = entity.findPk();
        if (tableId != null && tableId.isSeqBefore(mapping.db())) {
            /* do nothing */
        } else if (withPk || (oldId == null && newId != null)) {
            isTrue(newId != null, "The pk of insert entity can't be null, you should use method insert without pk.");
        } else {
            isTrue(newId == null, "The pk of insert entity must be null, you should use method insert with pk.");
        }
        setByDefault.accept(entity);
        /* 主键有可能被 IdGenerator 赋值 **/
        return newId != null;
    }

    /**
     * 获取指定的动态表名称
     *
     * @param entity 要插入的实例
     * @return 操作表名称
     */
    static String dynamic(IEntity entity, IMapping mapping) {
        IFragment fragment = RefKit.byEntity(entity.entityClass()).table(entity);
        String tableName = fragment.get(mapping);
        if (entity instanceof IRichEntity) {
            String dynamic = entity.tableSupplier();
            return isBlank(dynamic) ? tableName : dynamic;
        } else {
            return tableName;
        }
    }

    /**
     * 获取指定的动态表名称
     *
     * @param entity 要插入的实例
     * @return 操作表名称
     */
    static <E extends IEntity> String dynamic(List<E> entities, IMapping mapping) {
        Set<String> tables = entities.stream().map(e -> dynamic(e, mapping)).collect(Collectors.toSet());
        if (tables.size() != 1) {
            throw new RuntimeException("Cannot batch insert data into multiple target tables:" + tables);
        } else {
            return tables.iterator().next();
        }
    }

    /**
     * 构造updates中没有显式设置的默认值构造
     *
     * @param updates 显式update字段
     * @return ignore
     */
    static JoiningFrag updateDefaults(IMapping mapping, IWrapper wrapper, Map<IFragment, String> updates, boolean ignoreLockVersion) {
        List<FieldMapping> fields = mapping.allFields();
        UpdateDefault defaults = new UpdateDefault(updates);
        for (FieldMapping f : fields) {
            if (isBlank(f.update) || f.isVersion() && ignoreLockVersion) {
                continue;
            }
            defaults.add(wrapper, f, f.update);
        }
        return defaults.getUpdateDefaults();
    }

    static final Map<String, String> SEQs = new HashMap<>();

    /**
     * 返回seq的值
     */
    protected static String getSeq(String seq) {
        if (SEQs.containsKey(seq)) {
            return SEQs.get(seq);
        }
        synchronized (SEQs) {
            if (SEQs.containsKey(seq)) {
                return SEQs.get(seq);
            }
            String upper = seq.toUpperCase().trim();
            String value = seq;
            /* 去掉 SELECT 部分 */
            if (upper.startsWith(SELECT.name())) {
                value = value.substring(6);
                upper = upper.substring(6);
            }
            /* 去掉 FROM DUAL 部分 */
            int index = upper.indexOf(FROM.name());
            if (index > 0 && upper.endsWith("DUAL")) {
                value = value.substring(0, index);
            }
            SEQs.put(seq, value.trim());
            return SEQs.get(seq);
        }
    }
}