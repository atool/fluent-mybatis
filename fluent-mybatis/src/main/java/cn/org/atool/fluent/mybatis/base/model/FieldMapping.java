package cn.org.atool.fluent.mybatis.base.model;

import static cn.org.atool.fluent.mybatis.If.isBlank;

/**
 * FieldMeta: 实体字段和数据库字段映射信息
 *
 * @author darui.wu
 * @create 2020/6/23 9:16 上午
 */
public class FieldMapping {
    /**
     * 属性名称
     */
    public final String name;
    /**
     * 字段名称
     */
    public final String column;

    public FieldMapping(String name, String column) {
        this.name = name;
        this.column = column;
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
    public String el(String prefix) {
        return el(this.column, prefix, this.name);
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

    /**
     * key = #{prefix.value}
     *
     * @param key    key
     * @param prefix 前缀
     * @param value  value
     * @return key = #{prefix.value}
     */
    public static String el(String key, String prefix, String value) {
        return key + " = " + placeholder(prefix, value);
    }

    /**
     * 构造 #{prefix.field} 表达式
     *
     * @param prefix 前置
     * @param field  字段
     * @return #{prefix.field}
     */
    public static String placeholder(String prefix, String field) {
        return "#{" + (isBlank(prefix) ? field : prefix + "." + field) + "}";
    }
}