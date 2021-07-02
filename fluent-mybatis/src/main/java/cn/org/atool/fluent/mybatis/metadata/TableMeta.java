package cn.org.atool.fluent.mybatis.metadata;

import cn.org.atool.fluent.mybatis.functions.FieldPredicate;
import cn.org.atool.fluent.mybatis.mapper.StrConstant;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库表反射信息
 *
 * @author darui.wu
 */
@SuppressWarnings({"rawtypes"})
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

    public TableMeta(Class entityType) {
        this.entityType = entityType;
    }

    /**
     * 获取需要进行查询的 select sql 片段
     *
     * @param includePrimary 包含主键
     * @param predicate      过滤条件
     * @return sql 片段
     */
    public List<String> filter(boolean includePrimary, FieldPredicate predicate) {
        List<String> columns = new ArrayList<>();
        if (primary != null && (includePrimary || predicate.test(primary))) {
            columns.add(primary.getColumn());
        }
        fields.stream().filter(predicate).forEach(f -> columns.add(f.getColumn()));
        return columns;
    }
}