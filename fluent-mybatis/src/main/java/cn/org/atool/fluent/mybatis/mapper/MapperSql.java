package cn.org.atool.fluent.mybatis.mapper;


import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.fluent.mybatis.segment.model.HintType;
import cn.org.atool.fluent.mybatis.segment.model.WrapperData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static cn.org.atool.fluent.mybatis.If.isBlank;
import static cn.org.atool.fluent.mybatis.If.notBlank;
import static cn.org.atool.fluent.mybatis.base.model.FieldMapping.el;
import static cn.org.atool.fluent.mybatis.mapper.StrConstant.ASTERISK;
import static cn.org.atool.fluent.mybatis.mapper.StrConstant.SPACE;
import static java.util.stream.Collectors.joining;

/**
 * Mapper SQL组装
 *
 * @author darui.wu
 */
public class MapperSql {
    private StringBuffer buffer = new StringBuffer();

    @Override
    public String toString() {
        return buffer.toString().trim();
    }

    public MapperSql SELECT(String table, String columns) {
        buffer.append("SELECT ").append(columns).append(" FROM ").append(table);
        return this;
    }

    public MapperSql COUNT(String table, WrapperData data) {
        this.hint(data, HintType.Before_All);
        buffer.append("SELECT ");
        this.hint(data, HintType.After_CrudKey);
        buffer.append("COUNT(");
        String select = data.getSqlSelect();
        // select 单字段和多字段判断
        buffer.append(isBlank(select) || select.contains(",") ? ASTERISK : select.trim());
        buffer.append(") FROM ");
        this.hint(data, HintType.Before_Table);
        buffer.append(table);
        this.hint(data, HintType.After_Table);
        return this;
    }

    public MapperSql INSERT_INTO(String table) {
        buffer.append("INSERT INTO ").append(table);
        return this;
    }

    public MapperSql VALUES() {
        buffer.append(" VALUES ");
        return this;
    }

    public MapperSql INSERT_COLUMNS(DbType dbType, String... columns) {
        String joining = Stream.of(columns)
            .map(String::trim)
            .map(dbType::wrap)
            .collect(joining(", "));
        buffer.append("(").append(joining).append(")");
        return this;
    }

    public MapperSql INSERT_COLUMNS(DbType dbType, List<String> columns) {
        String joining = columns.stream()
            .map(String::trim)
            .map(dbType::wrap)
            .collect(joining(", "));
        buffer.append("(").append(joining).append(")");
        return this;
    }

    public MapperSql INSERT_VALUES(List<String> values) {
        buffer.append("(").append(String.join(", ", values)).append(")");
        return this;
    }

    public MapperSql DELETE_FROM(String table, WrapperData data) {
        this.hint(data, HintType.Before_All);
        buffer.append(" DELETE ");
        this.hint(data, HintType.After_CrudKey);
        buffer.append(" FROM ");
        this.hint(data, HintType.Before_Table);
        buffer.append(table);
        this.hint(data, HintType.After_Table);
        return this;
    }

    public MapperSql UPDATE(String table) {
        return this.UPDATE(table, null);
    }

    public MapperSql UPDATE(String table, WrapperData data) {
        this.hint(data, HintType.Before_All);
        buffer.append(" UPDATE ");
        this.hint(data, HintType.After_CrudKey);
        this.hint(data, HintType.Before_Table);
        buffer.append(table);
        this.hint(data, HintType.After_Table);
        return this;
    }

    public MapperSql SET(String... sets) {
        buffer.append(" SET " + String.join(",\n", sets));
        return this;
    }

    public MapperSql SET(List<String> sets) {
        buffer.append(" SET " + String.join(",\n", sets));
        return this;
    }

    public MapperSql WHERE(String whereSql) {
        buffer.append(" WHERE " + whereSql);
        return this;
    }

    public MapperSql WHERE(List<String> where) {
        buffer.append(" WHERE " + String.join(" AND ", where));
        return this;
    }

    public MapperSql WHERE(String prefix, Map<String, Object> where) {
        List<String> ands = new ArrayList<>();
        for (Map.Entry<String, Object> entry : where.entrySet()) {
            if (entry.getValue() == null) {
                ands.add(entry.getKey() + " IS NULL");
            } else {
                ands.add(el(entry.getKey(), prefix, entry.getKey()));
            }
        }
        return this.WHERE(ands);
    }

    public MapperSql WHERE_GROUP_BY(WrapperData data) {
        if (notBlank(data.getWhereSql())) {
            this.WHERE(data.getWhereSql());
        }
        if (notBlank(data.getGroupBy())) {
            this.APPEND(data.getGroupBy());
        }
        if (notBlank(data.getLastSql())) {
            this.APPEND(data.getLastSql());
        }
        return this;
    }

    public MapperSql WHERE_GROUP_ORDER_BY(WrapperData data) {
        if (notBlank(data.getWhereSql())) {
            this.WHERE(data.getWhereSql());
        }
        if (notBlank(data.getGroupBy())) {
            this.APPEND(data.getGroupBy());
        }
        if (notBlank(data.getOrderBy())) {
            this.APPEND(data.getOrderBy());
        }
        if (notBlank(data.getLastSql())) {
            this.APPEND(data.getLastSql());
        }
        return this;
    }

    public MapperSql WHERE_PK_IN(String column, int size) {
        buffer.append(" WHERE ").append(column).append(" IN (");
        for (int index = 0; index < size; index++) {
            if (index > 0) {
                buffer.append(", ");
            }
            buffer.append("#{coll[" + index + "]}");
        }
        buffer.append(")");
        return this;
    }

    public MapperSql APPEND(String sql) {
        buffer.append(" " + sql + " ");
        return this;
    }

    public MapperSql SELECT(String table, WrapperData data, String defaultColumns) {
        this.hint(data, HintType.Before_All);
        buffer.append("SELECT ");
        this.hint(data, HintType.After_CrudKey);
        this.APPEND(data.isDistinct() ? "DISTINCT " : SPACE);
        buffer.append(isBlank(data.getSqlSelect()) ? defaultColumns : data.getSqlSelect());
        buffer.append(" FROM ");
        this.hint(data, HintType.Before_Table);
        buffer.append(table);
        this.hint(data, HintType.After_Table);
        return this;
    }

    /**
     * 添加limit语句
     *
     * @param data
     * @param offsetEverZero 永远从0开始情况
     * @return
     */
    public MapperSql LIMIT(WrapperData data, boolean offsetEverZero) {
        if (data == null || data.getPaged() == null) {
            return this;
        }
        if (offsetEverZero) {
            this.APPEND(" LIMIT #{ew.wrapperData.paged.limit}");
        } else {
            this.APPEND(" LIMIT #{ew.wrapperData.paged.offset}, #{ew.wrapperData.paged.limit}");
        }
        return this;
    }

    private void hint(WrapperData data, HintType hintType) {
        if (data != null) {
            buffer.append(data.hint(hintType));
        }
    }
}