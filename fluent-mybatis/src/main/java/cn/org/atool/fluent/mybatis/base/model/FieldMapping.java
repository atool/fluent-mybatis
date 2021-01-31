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
     * @return
     */
    @Override
    public String toString() {
        return this.column;
    }

    /**
     * column = #{prefix.field}
     *
     * @param prefix
     * @return
     */
    public String el(String prefix) {
        return el(this.column, prefix, this.name);
    }

    /**
     * alias.column
     *
     * @param alias 表别名
     * @return
     */
    public String alias(String alias) {
        return alias(alias, column);
    }

    /**
     * tableAlias.column 表达式
     *
     * @param alias  table别名
     * @param column 字段
     * @return
     */
    public static String alias(String alias, String column) {
        return isBlank(alias) ? column : alias + "." + column;
    }

    /**
     * key = #{prefix.value}
     *
     * @param key
     * @param prefix
     * @param value
     * @return
     */
    public static String el(String key, String prefix, String value) {
        return key + " = " + placeholder(prefix, value);
    }

    /**
     * 构造 #{prefix.field} 表达式
     *
     * @param prefix 前置
     * @param field  字段
     * @return
     */
    public static String placeholder(String prefix, String field) {
        return "#{" + (isBlank(prefix) ? field : prefix + "." + field) + "}";
    }

    /**
     * 判断是否是数据库表字段名称
     * 非全数字, 只包含数字+字母+下划线组成
     *
     * @param input
     * @return
     */
    public static boolean isColumnName(String input) {
        if (isBlank(input)) {
            return false;
        }
        boolean is_digit = true;
        for (char ch : input.toCharArray()) {
            if (Character.isDigit(ch)) {
                continue;
            } else if (Character.isLetter(ch) || ch == '_') {
                is_digit = false;
                continue;
            }
            return false;
        }
        return !is_digit;
    }
}