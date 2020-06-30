package cn.org.atool.fluent.mybatis.method;

import cn.org.atool.fluent.mybatis.method.metadata.DbType;
import cn.org.atool.fluent.mybatis.method.metadata.TableFieldMeta;
import cn.org.atool.fluent.mybatis.method.metadata.TableMeta;
import cn.org.atool.fluent.mybatis.method.model.SqlBuilder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static cn.org.atool.fluent.mybatis.method.model.XmlConstant.*;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.*;
import static java.util.stream.Collectors.joining;

/**
 * AbstractMethod 抽象方法
 *
 * @author darui.wu
 * @create 2020/5/26 11:25 上午
 */
public abstract class AbstractMethod implements InjectMethod {
    @Getter
    private DbType dbType;

    protected AbstractMethod(DbType dbType) {
        assertNotNull("dbType", dbType);
        this.dbType = dbType;
    }

    /**
     * where部分
     *
     * @param builder
     * @return
     */
    protected SqlBuilder whereByWrapper(SqlBuilder builder) {
        return builder
            .ifThen(Wrapper_Where_NotNull, Wrapper_Where_Var);
    }

    /**
     * last 部分
     *
     * @param builder
     * @param withOrder 是否加order by
     * @return
     */
    protected SqlBuilder lastByWrapper(SqlBuilder builder, boolean withOrder) {
        builder.ifThen(Wrapper_GroupBy_NotNull, Wrapper_GroupBy_Var);
        if (withOrder) {
            builder.ifThen(Wrapper_OrderBy_NotNull, Wrapper_OrderBy_Var);
        }
        builder.ifThen(Wrapper_LastSql_NotNull, Wrapper_LastSql_Var);
        return builder;
    }

    /**
     * where id = #{id}
     * 没有主键的表，需要特殊处理，避免扫表
     *
     * @param table
     * @param builder
     * @return
     */
    protected SqlBuilder whereById(TableMeta table, SqlBuilder builder) {
        if (table.getPrimary() == null) {
            return builder.append("1!=1");
        } else {
            return builder.value("@column=#{et.@property}", table.getKeyProperty(), table.getKeyColumn());
        }
    }

    /**
     * where id in(?, ?, ?)
     * 没有主键的表，需要特殊处理，避免扫表
     *
     * @param table
     * @param builder
     * @return
     */
    protected SqlBuilder whereByIds(TableMeta table, SqlBuilder builder) {
        if (table.getPrimary() == null) {
            return builder.append("1!=1");
        } else {
            return builder
                .value("@property IN (", table.getKeyProperty(), null)
                .foreach("coll", "item", ",", () -> builder.append("#{item}"))
                .append(")");
        }
    }


    /**
     * 根据map条件查询
     *
     * @param table
     * @param builder
     */
    protected SqlBuilder whereByMap(TableMeta table, SqlBuilder builder) {
        return builder.ifThen("cm != null and !cm.isEmpty", () -> {
            builder.foreach("cm", "v", "AND ", () -> {
                builder.choose("v == null", "${k} IS NULL ", "${k} = #{v}");
            });
        });
    }

    /**
     * 返回包含主键的字段拼接
     *
     * @param table
     * @return
     */
    protected String getColumnsWithPrimary(TableMeta table) {
        List<String> list = new ArrayList<>();
        if (table.getKeyColumn() != null) {
            list.add(table.getKeyColumn());
        }
        table.getFields().forEach(field -> list.add(field.getColumn()));
        return list.stream().collect(joining(", "));
    }

    /**
     * 是否是insert时默认赋值字段
     *
     * @param field
     * @return
     */
    public static boolean isUpdateDefault(TableFieldMeta field) {
        return isNotBlank(field.getUpdate());
    }

    /**
     * 指定分库分表?
     *
     * @return
     */
    protected boolean isSpecTable() {
        return false;
    }

    /**
     * 是否是insert时默认赋值字段
     *
     * @param field
     * @return
     */
    protected boolean isInsertDefault(TableFieldMeta field) {
        return isNotBlank(field.getInsert());
    }
}