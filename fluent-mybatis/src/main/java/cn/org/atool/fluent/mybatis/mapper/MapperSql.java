package cn.org.atool.fluent.mybatis.mapper;


import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.fluent.mybatis.segment.fragment.IFragment;
import cn.org.atool.fluent.mybatis.segment.fragment.JoiningFrag;
import cn.org.atool.fluent.mybatis.segment.model.HintType;
import cn.org.atool.fluent.mybatis.segment.model.WrapperData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static cn.org.atool.fluent.mybatis.If.isBlank;
import static cn.org.atool.fluent.mybatis.If.notBlank;
import static cn.org.atool.fluent.mybatis.mapper.StrConstant.ASTERISK;
import static cn.org.atool.fluent.mybatis.mapper.StrConstant.SPACE;
import static cn.org.atool.fluent.mybatis.segment.fragment.KeyFrag.SELECT;
import static java.util.stream.Collectors.joining;

/**
 * Mapper SQL组装
 *
 * @author darui.wu
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class MapperSql {
    private final StringBuilder buffer = new StringBuilder();

    @Override
    public String toString() {
        return buffer.toString().trim();
    }

    public MapperSql SELECT(String table, String columns) {
        buffer.append(SELECT.get(null)).append(columns).append(" FROM ").append(table);
        return this;
    }

    public MapperSql COUNT(DbType dbType, IFragment table, WrapperData data) {
        this.hint(data, HintType.Before_All);
        buffer.append(SELECT.get(dbType));
        this.hint(data, HintType.After_CrudKey);
        buffer.append("COUNT(");
        String select = data.select().get(dbType);
        // select 单字段和多字段判断
        buffer.append(isBlank(select) || select.contains(",") ? ASTERISK : select.trim());
        buffer.append(") FROM ");
        this.hint(data, HintType.Before_Table);
        buffer.append(table.get(dbType));
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

    public MapperSql INSERT_COLUMNS(DbType dbType, List<String> columns) {
        String joining = columns.stream()
            .map(String::trim)
            .map(dbType::wrap)
            .collect(joining(", "));
        buffer.append(brackets(joining));
        return this;
    }

    public MapperSql INSERT_VALUES(List<String> values) {
        buffer.append(brackets(String.join(", ", values)));
        return this;
    }

    public MapperSql DELETE_FROM(DbType dbType, IFragment table, WrapperData data) {
        this.hint(data, HintType.Before_All);
        buffer.append(" DELETE ");
        this.hint(data, HintType.After_CrudKey);
        buffer.append(" FROM ");
        this.hint(data, HintType.Before_Table);
        buffer.append(table.get(dbType));
        this.hint(data, HintType.After_Table);
        return this;
    }

    public MapperSql UPDATE(DbType dbType, IFragment table) {
        return this.UPDATE(dbType, table, null);
    }

    public MapperSql UPDATE(DbType dbType, IFragment table, WrapperData data) {
        this.hint(data, HintType.Before_All);
        buffer.append(" UPDATE ");
        this.hint(data, HintType.After_CrudKey);
        this.hint(data, HintType.Before_Table);
        buffer.append(table.get(dbType));
        this.hint(data, HintType.After_Table);
        return this;
    }

    public MapperSql SET(String... sets) {
        buffer.append(" SET ").append(String.join(",\n", sets));
        return this;
    }

    public MapperSql SET(DbType dbType, JoiningFrag sets) {
        buffer.append(" SET ").append(sets.get(dbType));
        return this;
    }

    public MapperSql WHERE(String where) {
        if (notBlank(where)) {
            buffer.append(" WHERE ").append(where.trim());
        }
        return this;
    }

    public MapperSql WHERE(List<String> where) {
        buffer.append(" WHERE ").append(String.join(" AND ", where));
        return this;
    }

    public MapperSql WHERE(DbType dbType, String prefix, Map<String, Object> where) {
        List<String> ands = new ArrayList<>();
        for (Map.Entry<String, Object> entry : where.entrySet()) {
            if (entry.getValue() == null) {
                ands.add(dbType.wrap(entry.getKey()) + " IS NULL");
            } else {
                String column = entry.getKey();
                String el = dbType.wrap(column) + " = " + "#{" + (isBlank(prefix) ? column : prefix + "." + column) + "}";
                ands.add(el);
            }
        }
        return this.WHERE(ands);
    }

    public MapperSql WHERE_GROUP_BY(DbType dbType, WrapperData data) {
        this.WHERE_GROUP_HAVING(dbType, data);
        if (data.last().notEmpty()) {
            this.APPEND(data.segments().last());
        }
        return this;
    }

    public MapperSql WHERE_GROUP_ORDER_BY(DbType dbType, WrapperData data) {
        this.WHERE_GROUP_HAVING(dbType, data);
        if (data.orderBy().notEmpty()) {
            this.APPEND(data.segments().orderBy.get(dbType));
        }
        if (data.last().notEmpty()) {
            this.APPEND(data.segments().last());
        }
        return this;
    }

    private void WHERE_GROUP_HAVING(DbType dbType, WrapperData data) {
        if (data.where().notEmpty()) {
            this.WHERE(data.segments().where.get(dbType));
        }
        if (data.groupBy().notEmpty()) {
            this.APPEND(data.segments().groupBy.get(dbType));
        }
        if (data.having().notEmpty()) {
            this.APPEND(data.segments().having.get(dbType));
        }
    }

    public MapperSql APPEND(String sql) {
        buffer.append(SPACE).append(sql).append(SPACE);
        return this;
    }

    public MapperSql SELECT(DbType dbType, IFragment table, WrapperData data, IFragment defaultColumns) {
        this.hint(data, HintType.Before_All);
        buffer.append(SELECT.get(dbType));
        this.hint(data, HintType.After_CrudKey);
        this.APPEND(data.isDistinct() ? "DISTINCT " : SPACE);
        buffer.append(isBlank(data.select().get(dbType)) ? defaultColumns.get(dbType) : data.select().get(dbType));
        buffer.append(" FROM ");
        this.hint(data, HintType.Before_Table);
        buffer.append(table.get(dbType));
        this.hint(data, HintType.After_Table);
        return this;
    }

    /**
     * 添加limit语句
     *
     * @param data           WrapperData
     * @param offsetEverZero 永远从0开始情况
     * @return MapperSql
     */
    public MapperSql LIMIT(WrapperData data, boolean offsetEverZero) {
        if (data == null || data.paged() == null) {
            return this;
        }
        if (offsetEverZero) {
            this.APPEND(" LIMIT #{ew.data.paged.limit}");
        } else {
            this.APPEND(" LIMIT #{ew.data.paged.offset}, #{ew.data.paged.limit}");
        }
        return this;
    }

    private void hint(WrapperData data, HintType hintType) {
        if (data != null) {
            buffer.append(data.hint(hintType));
        }
    }

    /**
     * 给sql加上括弧
     *
     * @param obj sql
     * @return (obj)
     */
    public static String brackets(Object obj) {
        return obj == null ? "()" : "(" + obj + ")";
    }

    static final AtomicLong tmp = new AtomicLong(0);

    /**
     * 临时表别名
     *
     * @return 临时表别名
     */
    public static String tmpTable() {
        return "TMP_" + tmp.incrementAndGet();
    }
}