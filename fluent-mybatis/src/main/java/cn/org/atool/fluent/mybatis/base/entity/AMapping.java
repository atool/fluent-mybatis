package cn.org.atool.fluent.mybatis.base.entity;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.BaseDefaults;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.crud.IUpdate;
import cn.org.atool.fluent.mybatis.base.crud.IWrapper;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.base.model.UniqueType;
import cn.org.atool.fluent.mybatis.functions.TableDynamic;
import cn.org.atool.fluent.mybatis.metadata.DbType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static cn.org.atool.fluent.mybatis.If.isBlank;
import static cn.org.atool.fluent.mybatis.If.notBlank;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

/**
 * 字段映射抽象类
 *
 * @author darui.wu
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Getter
public abstract class AMapping<E extends IEntity, Q extends IQuery<E>, U extends IUpdate<E>>
    extends BaseDefaults<E, Q, U>
    implements IMapping, IEntityKit {
    /**
     * schema
     */
    protected String schema;

    @Getter(AccessLevel.NONE)
    protected String tableName;

    @Getter(AccessLevel.NONE)
    @Setter
    private TableDynamic tableDynamic;
    /**
     * 数据库类型
     */
    @Getter(AccessLevel.NONE)
    private final DbType dbType;
    /**
     * 数据库字段对应的FieldMapping
     */
    public final Map<String, FieldMapping> columnMap;

    /**
     * 实体类字段对应的FieldMapping
     */
    public final Map<String, FieldMapping> fieldsMap;
    /**
     * 实体类所有字段列表
     */
    public final List<String> allFields;
    /**
     * 数据库所有字段列表
     */
    public final List<String> allColumns;
    /**
     * 数据库所有字段列表用逗号分隔
     */
    public final String selectAll;

    protected Map<UniqueType, FieldMapping> uniqueFields = new HashMap<>(4);

    protected AMapping(DbType dbType) {
        this.dbType = dbType;
        this.columnMap = this.allFields().stream().collect(Collectors.toMap(f -> f.column, f -> f));
        this.fieldsMap = this.allFields().stream().collect(Collectors.toMap(f -> f.name, f -> f));
        this.allColumns = Collections.unmodifiableList(this.allFields().stream().map(f -> f.column).collect(toList()));
        this.selectAll = this.allColumns.stream().map(dbType::wrap).collect(joining(", "));
        this.allFields = Collections.unmodifiableList(this.allFields().stream().map(f -> f.name).collect(toList()));
    }

    /**
     * 返回所有字段定义
     *
     * @return List<FieldMapping>
     */
    public abstract List<FieldMapping> allFields();

    @Override
    public String columnOfField(String field) {
        if (this.fieldsMap.containsKey(field)) {
            return this.fieldsMap.get(field).column;
        } else {
            return null;
        }
    }

    @Override
    public <T extends IEntity> T toEntity(Map<String, Object> map) {
        IEntity entity = this.newEntity();
        for (Map.Entry entry : map.entrySet()) {
            FieldMapping f = this.fieldsMap.get((String) entry.getKey());
            if (f != null) {
                f.setter.set(entity, entry.getValue());
            }
        }
        return (T) entity;
    }

    /**
     * entity转换为Map
     *
     * @param entity     Entity
     * @param isProperty true: 实体属性值, false: 数据库字段值
     * @param isNoN      is not null, true: 只允许非空值, false: 允许空值
     * @return entity value map
     */
    private Map<String, Object> toMap(IEntity entity, boolean isProperty, boolean isNoN) {
        Map<String, Object> map = new HashMap<>(this.allFields.size());
        if (entity == null) {
            return map;
        }
        for (FieldMapping f : this.allFields()) {
            Object value = f.getter.get(entity);
            if (!isNoN || value != null) {
                map.put(isProperty ? f.name : f.column, value);
            }
        }
        return map;
    }

    @Override
    public Map<String, Object> toColumnMap(IEntity entity, boolean isNoN) {
        return this.toMap(entity, false, isNoN);
    }

    @Override
    public Map<String, Object> toEntityMap(IEntity entity, boolean isNoN) {
        return this.toMap(entity, true, isNoN);
    }

    @Override
    public <T> T valueByField(IEntity entity, String prop) {
        if (entity == null || prop == null) {
            return null;
        } else {
            FieldMapping f = this.fieldsMap.get(prop);
            return f == null ? null : (T) f.getter.get(entity);
        }
    }

    @Override
    public <T> T valueByColumn(IEntity entity, String column) {
        if (entity == null || column == null) {
            return null;
        } else {
            FieldMapping f = this.columnMap.get(column);
            return f == null ? null : (T) f.getter.get(entity);
        }
    }

    @Override
    public <T extends IEntity> T copy(IEntity entity) {
        T copy = this.newEntity();
        for (FieldMapping f : this.allFields()) {
            f.setter.set(copy, f.getter.get(entity));
        }
        return copy;
    }

    /**
     * 获取表名
     */
    @Override
    public Supplier<String> table() {
        if (tableDynamic != null) {
            return () -> tableDynamic.get(this.tableName);
        } else if (NeedSchemaDb.contains(dbType) && notBlank(schema)) {
            return () -> this.schema + "." + this.dbType.wrap(this.tableName);
        } else {
            return () -> this.dbType.wrap(this.tableName);
        }
    }

    @Override
    public Optional<FieldMapping> findField(UniqueType type) {
        return Optional.ofNullable(uniqueFields.get(type));
    }

    /**
     * 表查询需要带上schema的数据库类型
     */
    static final List<DbType> NeedSchemaDb = Arrays.asList(
        DbType.DERBY, DbType.POSTGRE_SQL, DbType.SQL_SERVER2012, DbType.SQL_SERVER2005
    );

    @Override
    public DbType dbType() {
        return this.dbType;
    }

    /**
     * 获取IQuery或IUpdate对应的表名称
     *
     * @param wrapper IQuery或IUpdate
     * @return 表名称
     */
    public String dynamic(IWrapper wrapper) {
        String table = (String) wrapper.getTable().get();
        return isBlank(table) ? this.getTableName() : table;
    }
}