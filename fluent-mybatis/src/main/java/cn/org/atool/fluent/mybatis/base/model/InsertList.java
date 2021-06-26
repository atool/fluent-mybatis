package cn.org.atool.fluent.mybatis.base.model;

import java.util.ArrayList;
import java.util.List;

import static cn.org.atool.fluent.mybatis.If.isBlank;

/**
 * insert by entity字段构造
 *
 * @author wudarui
 */
public class InsertList {
    /**
     * 待插入记录字段名称
     */
    public final List<String> columns = new ArrayList<>();
    /**
     * 待插入记录字段值
     */
    public final List<String> values = new ArrayList<>();

    /**
     * insert字段表达式
     *
     * @param prefix   前置
     * @param field    对象字段
     * @param value    对象属性值
     * @param _default insert默认值
     */
    public InsertList add(String prefix, FieldMapping field, Object value, String _default) {
        if (value == null && isBlank(_default)) {
            return this;
        }
        this.columns.add(field.column);
        this.values.add(el(prefix, field, value, _default));
        return this;
    }

    public static String el(String prefix, FieldMapping field, Object value, String _default) {
        if (value == null) {
            return isBlank(_default) ? null : _default;
        } else if (field.typeHandler == null) {
            return "#{" + prefix + field.name + "}";
        } else {
            return String.format("#{%s%s, javaType=%s, typeHandler=%s}",
                prefix, field.name, field.javaType.getName(), field.typeHandler.getName());
        }
    }
}