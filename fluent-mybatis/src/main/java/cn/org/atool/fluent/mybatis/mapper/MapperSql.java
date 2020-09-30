package cn.org.atool.fluent.mybatis.mapper;


import cn.org.atool.fluent.mybatis.segment.model.WrapperData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.isBlank;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.isNotBlank;

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
        buffer.append("SELECT COUNT(");
        if (isBlank(data.getSqlSelect())) {
            buffer.append("*");
        } else {
            buffer.append(data.getSqlSelect());
        }
        buffer.append(") FROM ").append(table);
        return this;
    }

    public MapperSql INSERT_INTO(String table) {
        buffer.append("INSERT INTO ").append(table);
        return this;
    }

    public MapperSql INSERT_COLUMNS(String... columns) {
        buffer.append("(").append(String.join(", ", columns)).append(")");
        return this;
    }

    public MapperSql VALUES() {
        buffer.append(" VALUES ");
        return this;
    }

    public MapperSql INSERT_COLUMNS(List<String> columns) {
        buffer.append("(").append(String.join(", ", columns)).append(")");
        return this;
    }

    public MapperSql INSERT_VALUES(String... columns) {
        buffer.append("(").append(String.join(", ", columns)).append(")");
        return this;
    }

    public MapperSql INSERT_VALUES(List<String> columns) {
        buffer.append("(").append(String.join(", ", columns)).append(")");
        return this;
    }

    public MapperSql DELETE_FROM(String table) {
        buffer.append(" DELETE FROM " + table);
        return this;
    }

    public MapperSql UPDATE(String table) {
        buffer.append(" UPDATE " + table);
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
                ands.add(entry.getKey() + " = #{" + prefix + "." + entry.getKey() + "}");
            }
        }
        return this.WHERE(ands);
    }

    public MapperSql WHERE_GROUP_BY(WrapperData data) {
        if (isNotBlank(data.getWhereSql())) {
            this.WHERE(data.getWhereSql());
        }
        if (isNotBlank(data.getGroupBy())) {
            this.APPEND(data.getGroupBy());
        }
        if (isNotBlank(data.getLastSql())) {
            this.APPEND(data.getLastSql());
        }
        return this;
    }

    public MapperSql WHERE_GROUP_ORDER_BY(WrapperData data) {
        if (isNotBlank(data.getWhereSql())) {
            this.WHERE(data.getWhereSql());
        }
        if (isNotBlank(data.getGroupBy())) {
            this.APPEND(data.getGroupBy());
        }
        if (isNotBlank(data.getOrderBy())) {
            this.APPEND(data.getOrderBy());
        }
        if (isNotBlank(data.getLastSql())) {
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

    public MapperSql LIMIT(String limit) {
        buffer.append(" LIMIT " + limit);
        return this;
    }

    public MapperSql SELECT(String table, WrapperData data, String defaultColumns) {
        buffer.append("SELECT ");
        if (data.isDistinct()) {
            this.APPEND("DISTINCT ");
        }
        if (isBlank(data.getSqlSelect())) {
            buffer.append(defaultColumns).append(" FROM ").append(table);
        } else {
            buffer.append(data.getSqlSelect()).append(" FROM ").append(table);
        }
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
}