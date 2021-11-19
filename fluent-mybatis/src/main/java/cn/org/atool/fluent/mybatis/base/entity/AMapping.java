package cn.org.atool.fluent.mybatis.base.entity;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.BaseDefaults;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.crud.IUpdate;
import cn.org.atool.fluent.mybatis.base.crud.IWrapper;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.base.model.KeyMap;
import cn.org.atool.fluent.mybatis.base.model.UniqueType;
import cn.org.atool.fluent.mybatis.functions.RefFunction;
import cn.org.atool.fluent.mybatis.functions.RefKey;
import cn.org.atool.fluent.mybatis.functions.RefKeyFunc;
import cn.org.atool.fluent.mybatis.functions.TableDynamic;
import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.fluent.mybatis.segment.fragment.CachedFrag;
import cn.org.atool.fluent.mybatis.segment.fragment.IFragment;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

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

    @Getter
    protected String tableName;

    @Getter(AccessLevel.NONE)
    @Setter
    private TableDynamic tableSupplier;
    /**
     * 数据库类型
     */
    @Getter(AccessLevel.NONE)
    private DbType dbType;
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
    public final CachedFrag selectAll;

    protected Map<UniqueType, FieldMapping> uniqueFields = new HashMap<>(4);

    protected AMapping(DbType dbType) {
        this.dbType = dbType;
        this.columnMap = this.allFields().stream().collect(Collectors.toMap(f -> f.column, f -> f));
        this.fieldsMap = this.allFields().stream().collect(Collectors.toMap(f -> f.name, f -> f));
        this.allColumns = Collections.unmodifiableList(this.allFields().stream().map(f -> f.column).collect(toList()));
        this.selectAll = CachedFrag.set(m -> this.allColumns.stream().map(m.db()::wrap).collect(joining(", ")));
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
     * @param entity      Entity
     * @param isProperty  true: 实体属性值, false: 数据库字段值
     * @param allowedNull is allowed null, true:  允许空值, false:只允许非空值
     * @return entity value map
     */
    private Map<String, Object> toMap(IEntity entity, boolean isProperty, boolean allowedNull) {
        Map<String, Object> map = new HashMap<>(this.allFields.size());
        if (entity == null) {
            return map;
        }
        for (FieldMapping f : this.allFields()) {
            Object value = f.getter.get(entity);
            if (allowedNull || value != null) {
                map.put(isProperty ? f.name : f.column, value);
            }
        }
        return map;
    }

    @Override
    public Map<String, Object> toColumnMap(IEntity entity, boolean allowedNull) {
        return this.toMap(entity, false, allowedNull);
    }

    @Override
    public Map<String, Object> toEntityMap(IEntity entity, boolean allowedNull) {
        return this.toMap(entity, true, allowedNull);
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

    private final CachedFrag tableSegment = CachedFrag.set(m -> {
        DbType db = m.db();
        if (NeedSchemaDb.contains(db) && notBlank(schema)) {
            return this.schema + "." + db.wrap(this.tableName);
        } else {
            return db.wrap(this.tableName);
        }
    });

    /**
     * 获取表名
     */
    @Override
    public IFragment table() {
        return tableSupplier == null ? this.tableSegment : m -> tableSupplier.get(this.tableName);
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
    public DbType db() {
        return this.dbType;
    }

    @Override
    public void db(DbType dbType) {
        this.dbType = dbType;
    }

    /**
     * 获取IQuery或IUpdate对应的表名称
     *
     * @param wrapper IQuery或IUpdate
     * @return 表名称
     */
    public IFragment dynamic(IWrapper wrapper) {
        IFragment table = wrapper.table(false);
        return table != null && table.notEmpty() ? table : this.table();
    }

    protected TableId tableId = null;

    public TableId tableId() {
        return tableId;
    }

    protected final KeyMap<RefKey> Ref_Keys = new KeyMap<>();

    @Override
    public KeyMap<RefKey> refKeys() {
        return Ref_Keys;
    }

    /**
     * 增加关联关系
     *
     * @param refName 关联名称
     * @param src     原Entity关联键构造
     * @param isList  是否1:N
     * @param ref     关联Entity关联键构造
     * @return ignore
     */
    protected <R> AMapping ref(String refName, RefKeyFunc<E> src, boolean isList, RefKeyFunc<R> ref, RefFunction<E> refMethod) {
        Ref_Keys.put(refName, RefKey.refKey(refName, isList, src, ref, refMethod));
        return this;
    }

    /**
     * 增加关联关系
     *
     * @param refName 关联名称
     * @param isList  是否1:N
     * @return ignore
     */
    protected AMapping ref(String refName, boolean isList, RefFunction<E> refMethod) {
        Ref_Keys.put(refName, RefKey.refKey(refName, isList, null, null, refMethod));
        return this;
    }
}