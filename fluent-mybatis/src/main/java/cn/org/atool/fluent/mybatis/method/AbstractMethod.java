package cn.org.atool.fluent.mybatis.method;

import cn.org.atool.fluent.mybatis.metadata.FieldInfo;
import cn.org.atool.fluent.mybatis.metadata.TableInfo;
import cn.org.atool.fluent.mybatis.method.model.SqlBuilder;

import java.util.ArrayList;
import java.util.List;

import static cn.org.atool.fluent.mybatis.util.MybatisUtil.isNotEmpty;
import static java.util.stream.Collectors.joining;

/**
 * AbstractMethod 抽象方法
 *
 * @author darui.wu
 * @create 2020/5/26 11:25 上午
 */
public abstract class AbstractMethod implements InjectMethod {
    /**
     * where部分
     *
     * @param table
     * @param builder
     * @return
     */
    protected SqlBuilder whereEntity(TableInfo table, SqlBuilder builder) {
        return builder
            .ifThen("ew != null and ew.entity != null", () -> {
                if (table.getPrimary() != null) {
                    builder.ifThen("ew.entity.@property != null", "@column=#{ew.entity.@column}", table.getKeyProperty(), table.getKeyColumn());
                }
                builder.eachJoining(table.getFields(), (field) -> {
                    builder.ifThen("ew.entity.@property != null", "AND @column=#{ew.entity.@property}", field.getProperty(), field.getColumn());
                });
            })
            .ifThen("ew != null and ew.sqlSegment != null and ew.sqlSegment != ''", "AND ${ew.sqlSegment}");
    }

    /**
     * where id = #{id}
     * 没有主键的表，需要特殊处理，避免扫表
     *
     * @param table
     * @param builder
     * @return
     */
    protected SqlBuilder whereById(TableInfo table, SqlBuilder builder) {
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
    protected SqlBuilder whereByIds(TableInfo table, SqlBuilder builder) {
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
    protected SqlBuilder whereByMap(TableInfo table, SqlBuilder builder) {
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
    protected String getColumnsWithPrimary(TableInfo table) {
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
    public static boolean isUpdateDefault(FieldInfo field) {
        return isNotEmpty(field.getUpdate());
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
    protected boolean isInsertDefault(FieldInfo field) {
        return isNotEmpty(field.getInsert());
    }
}