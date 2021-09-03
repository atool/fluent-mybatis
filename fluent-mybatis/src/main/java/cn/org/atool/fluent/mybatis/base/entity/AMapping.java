package cn.org.atool.fluent.mybatis.base.entity;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.BaseDefaults;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.crud.IUpdate;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.functions.TableDynamic;
import cn.org.atool.fluent.mybatis.metadata.DbType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.function.Supplier;

import static cn.org.atool.fluent.mybatis.If.notBlank;
import static java.util.stream.Collectors.joining;

/**
 * 字段映射抽象类
 *
 * @author darui.wu
 */
@Getter
public abstract class AMapping<E extends IEntity, Q extends IQuery<E>, U extends IUpdate<E>>
    extends BaseDefaults<E, Q, U>
    implements IMapping {
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
    private final DbType dbType;
    /**
     * 数据库字段对应的FieldMapping
     */
    public final Map<String, FieldMapping> columnMappings;

    /**
     * 实体类字段对应的FieldMapping
     */
    public final Map<String, FieldMapping> fieldMappings;
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

    public final List<FieldMapping> fields;

    protected AMapping(DbType dbType,
                       Map<String, FieldMapping> columnMappings,
                       Map<String, FieldMapping> fieldMappings) {
        this.dbType = dbType;
        this.columnMappings = columnMappings;
        this.fieldMappings = fieldMappings;
        this.allColumns = Collections.unmodifiableList(new ArrayList<>(this.columnMappings.keySet()));
        this.selectAll = this.allColumns.stream().map(dbType::wrap).collect(joining(", "));
        this.allFields = Collections.unmodifiableList(new ArrayList<>(this.fieldMappings.keySet()));
        this.fields = Collections.unmodifiableList(new ArrayList<>(this.fieldMappings.values()));
    }

    @Override
    public String findColumnByField(String field) {
        if (this.fieldMappings.containsKey(field)) {
            return this.fieldMappings.get(field).column;
        } else {
            return null;
        }
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

    /**
     * 表查询需要带上schema的数据库类型
     */
    static final List<DbType> NeedSchemaDb = Arrays.asList(
        DbType.DERBY, DbType.POSTGRE_SQL, DbType.SQL_SERVER2012, DbType.SQL_SERVER2005
    );
}