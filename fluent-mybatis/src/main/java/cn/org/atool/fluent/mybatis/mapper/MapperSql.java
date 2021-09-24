package cn.org.atool.fluent.mybatis.mapper;


import cn.org.atool.fluent.mybatis.If;
import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.fluent.mybatis.segment.fragment.IFragment;
import cn.org.atool.fluent.mybatis.segment.fragment.JoiningFrag;
import cn.org.atool.fluent.mybatis.segment.model.WrapperData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static cn.org.atool.fluent.mybatis.If.isBlank;
import static cn.org.atool.fluent.mybatis.If.notBlank;
import static cn.org.atool.fluent.mybatis.mapper.StrConstant.*;
import static cn.org.atool.fluent.mybatis.segment.fragment.KeyFrag.*;
import static cn.org.atool.fluent.mybatis.segment.model.HintType.*;

/**
 * Mapper SQL组装
 *
 * @author darui.wu
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class MapperSql {
    private final List<String> text = new ArrayList<>();

    @Override
    public String toString() {
        return text.stream().map(String::trim)
            .filter(If::notBlank).collect(Collectors.joining(SPACE));
    }

    public MapperSql SELECT(String table, String columns) {
        this.add(SELECT.key(), columns, FROM.key(), table);
        return this;
    }

    private void add(String... objects) {
        text.addAll(Arrays.asList(objects));
    }

    public MapperSql COUNT(IMapping mapping, IFragment table, WrapperData data) {
        String select = data.select().get(mapping);
        // select 单字段和多字段判断
        select = isBlank(select) || select.contains(",") ? ASTERISK : select.trim();
        this.add(
            data.hint(Before_All), SELECT.key(), data.hint(After_CrudKey),
            "COUNT(" + select + ")",
            FROM.key(),
            data.hint(Before_Table), table.get(mapping), data.hint(After_Table)
        );
        return this;
    }

    public MapperSql INSERT_INTO(String table) {
        this.add(INSERT_INTO.key(), table);
        return this;
    }

    public MapperSql VALUES() {
        this.add("VALUES");
        return this;
    }

    public MapperSql INSERT_COLUMNS(IMapping mapping, List<String> columns) {
        String joining = columns.stream()
            .map(String::trim)
            .map(mapping.db()::wrap)
            .collect(Collectors.joining(COMMA_SPACE));
        this.add(brackets(joining));
        return this;
    }

    public MapperSql DELETE_FROM(IMapping mapping, IFragment table, WrapperData data) {
        this.add(
            data.hint(Before_All), DELETE.key(), data.hint(After_CrudKey),
            FROM.key(),
            data.hint(Before_Table), table.get(mapping), data.hint(After_Table));
        return this;
    }

    public MapperSql UPDATE(IMapping mapping, IFragment table) {
        this.add(UPDATE.key(), table.get(mapping));
        return this;
    }

    public MapperSql UPDATE(IMapping mapping, IFragment table, WrapperData data) {
        this.add(
            data.hint(Before_All), UPDATE.key(), data.hint(After_CrudKey),
            data.hint(Before_Table), table.get(mapping), data.hint(After_Table)
        );
        return this;
    }

    public MapperSql SET(String... sets) {
        this.add(SET.key(), String.join(COMMA_SPACE, sets));
        return this;
    }

    public MapperSql SET(IMapping mapping, JoiningFrag sets) {
        this.add(SET.key(), sets.get(mapping));
        return this;
    }

    public MapperSql WHERE(String where) {
        if (notBlank(where)) {
            this.add(WHERE.key(), where);
        }
        return this;
    }

    public MapperSql WHERE(List<String> where) {
        if (!where.isEmpty()) {
            this.add(WHERE.key(), String.join(" AND ", where));
        }
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

    public MapperSql WHERE_GROUP_BY(IMapping mapping, WrapperData data) {
        this.WHERE_GROUP_HAVING(mapping, data);
        if (data.last().notEmpty()) {
            this.APPEND(data.segments().last());
        }
        return this;
    }

    public MapperSql WHERE_GROUP_ORDER_BY(IMapping mapping, WrapperData data) {
        this.WHERE_GROUP_HAVING(mapping, data);
        if (data.orderBy().notEmpty()) {
            this.APPEND(data.segments().orderBy.get(mapping));
        }
        if (data.last().notEmpty()) {
            this.APPEND(data.segments().last());
        }
        return this;
    }

    private void WHERE_GROUP_HAVING(IMapping mapping, WrapperData data) {
        if (data.where().notEmpty()) {
            this.WHERE(data.segments().where.get(mapping));
        }
        if (data.groupBy().notEmpty()) {
            this.APPEND(data.segments().groupBy.get(mapping));
        }
        if (data.having().notEmpty()) {
            this.APPEND(data.segments().having.get(mapping));
        }
    }

    public MapperSql APPEND(String sql) {
        this.add(sql);
        return this;
    }

    public MapperSql SELECT(IMapping mapping, IFragment table, WrapperData data, IFragment defaultColumns) {
        String select = data.select().get(mapping);
        this.add(
            data.hint(Before_All), SELECT.key(), data.hint(After_CrudKey),
            data.isDistinct() ? DISTINCT.key() : EMPTY,
            isBlank(select) ? defaultColumns.get(mapping) : select,
            FROM.key(), data.hint(Before_Table), table.get(mapping), data.hint(After_Table)
        );
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
            this.add("LIMIT #{ew.data.paged.limit}");
        } else {
            this.add("LIMIT #{ew.data.paged.offset}, #{ew.data.paged.limit}");
        }
        return this;
    }

    /**
     * 给sql加上括弧
     *
     * @param obj sql
     * @return (obj)
     */
    public static String brackets(Object obj) {
        return obj == null ? "()" : "(" + String.valueOf(obj).trim() + ")";
    }

    public static String brackets(String delimiter, List<String> list) {
        return "(" + String.join(COMMA_SPACE, list) + ")";
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