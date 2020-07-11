package cn.org.atool.fluent.mybatis.method.metadata;

import cn.org.atool.fluent.mybatis.segment.model.StrConstant;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static java.util.stream.Collectors.joining;

/**
 * 数据库表反射信息
 *
 * @author darui.wu
 */
@Data
@Accessors(chain = true)
public class TableMeta implements StrConstant {
    /**
     * 表名称
     */
    private String tableName;
    /**
     * 实体类型
     */
    private Class<?> entityType;
    /**
     * 主键
     */
    private TablePrimaryMeta primary;
    /**
     * 表字段信息列表
     */
    private List<TableFieldMeta> fields;
    /**
     * 缓存包含主键及字段的 sql select
     */
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private String allSqlSelect;

    public TableMeta() {
    }

    public TableMeta(Class<?> entityType) {
        this.entityType = entityType;
    }

    /**
     * 获取包含主键及字段的 select sql 片段
     *
     * @return sql 片段
     */
    public String getAllSqlSelect() {
        if (allSqlSelect == null) {
            allSqlSelect = filter(true, f -> true);
        }
        return allSqlSelect;
    }

    /**
     * 获取需要进行查询的 select sql 片段
     *
     * @param includePrimary 包含主键
     * @param predicate      过滤条件
     * @return sql 片段
     */
    public String filter(boolean includePrimary, Predicate<BaseFieldMeta> predicate) {
        List<String> columns = new ArrayList<>();
        if (primary != null && (includePrimary || predicate.test(primary))) {
            columns.add(primary.getColumn());
        }
        fields.stream().filter(predicate).forEach(f -> columns.add(f.getColumn()));
        return columns.stream().collect(joining(COMMA_SPACE));
    }

    public String getKeyProperty() {
        return this.primary == null ? null : this.primary.getProperty();
    }

    public String getKeyColumn() {
        return this.primary == null ? null : this.primary.getColumn();
    }

    /**
     * 设置默认值，避免出错
     *
     * @return
     */
    public Class getKeyType() {
        return this.primary == null ? Long.class : this.primary.getPropertyType();
    }
}