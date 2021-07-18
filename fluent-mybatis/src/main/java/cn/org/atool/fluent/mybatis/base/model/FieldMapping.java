package cn.org.atool.fluent.mybatis.base.model;

import cn.org.atool.fluent.mybatis.mapper.StrConstant;
import cn.org.atool.fluent.mybatis.metadata.DbType;

import static cn.org.atool.fluent.mybatis.If.isBlank;

/**
 * FieldMeta: 实体字段和数据库字段映射信息
 *
 * @author darui.wu  2020/6/23 9:16 上午
 */
@SuppressWarnings("rawtypes")
public class FieldMapping {
    /**
     * 属性名称
     */
    public final String name;
    /**
     * 字段名称
     */
    public final String column;

    public final Class javaType;

    public final Class typeHandler;

    public FieldMapping(String name, String column) {
        this(name, column, null, null);
    }

    public FieldMapping(String name, String column, Class javaType, Class typeHandler) {
        this.name = name;
        this.column = column;
        this.javaType = javaType;
        this.typeHandler = typeHandler;
    }

    /**
     * 返回字段名称
     *
     * @return ignore
     */
    @Override
    public String toString() {
        return this.column;
    }

    /**
     * column = #{prefix.field}
     *
     * @param prefix 前缀
     * @return ignore
     */
    public String el(DbType dbType, final String prefix) {
        String _prefix = isBlank(prefix) ? StrConstant.EMPTY : prefix + ".";
        if (typeHandler == null) {
            return dbType.wrap(this.column) + " = " + "#{" + _prefix + this.name + "}";
        } else {
            return String.format("%s = #{%s%s, javaType=%s, typeHandler=%s}",
                dbType.wrap(this.column), _prefix, this.name, this.javaType.getName(), this.typeHandler.getName());
        }
    }

    /**
     * alias.column
     *
     * @param alias 表别名
     * @return ignore
     */
    public String alias(String alias) {
        return alias(alias, column);
    }

    /**
     * tableAlias.column 表达式
     *
     * @param alias  table别名
     * @param column 字段
     * @return ignore
     */
    public static String alias(String alias, String column) {
        return isBlank(alias) ? column : alias + "." + column;
    }
}