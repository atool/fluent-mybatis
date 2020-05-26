package cn.org.atool.fluent.mybatis.metadata;

import cn.org.atool.fluent.mybatis.annotation.IdType;
import cn.org.atool.fluent.mybatis.annotation.KeySequence;
import cn.org.atool.fluent.mybatis.util.Constants;
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
public class TableInfo implements Constants {
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
    private FieldInfo primary;

    /**
     * 表主键ID Sequence
     */
    private KeySequence sequence;

    /**
     * 表主键ID 类型
     */
    private IdType idType = IdType.AUTO;

    /**
     * 表字段信息列表
     */
    private List<FieldInfo> fields;

    /**
     * 缓存包含主键及字段的 sql select
     */
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private String allSqlSelect;

    public TableInfo(Class<?> entityType) {
        this.entityType = entityType;
    }

    /**
     * 获取包含主键及字段的 select sql 片段
     *
     * @return sql 片段
     */
    public String getAllSqlSelect() {
        if (allSqlSelect == null) {
            allSqlSelect = chooseSelect(f -> true);
        }
        return allSqlSelect;
    }

    /**
     * 获取需要进行查询的 select sql 片段
     *
     * @param predicate 过滤条件
     * @return sql 片段
     */
    public String chooseSelect(Predicate<FieldInfo> predicate) {
        List<String> columns = new ArrayList<>();
        if (primary != null && predicate.test(primary)) {
            columns.add(primary.getColumn());
        }
        fields.stream().filter(predicate).forEach(f -> columns.add(f.getColumn()));
        return columns.stream().collect(joining(COMMA));
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
    public Class getPropertyType() {
        return this.primary == null ? Long.class : this.primary.getPropertyType();
    }
}