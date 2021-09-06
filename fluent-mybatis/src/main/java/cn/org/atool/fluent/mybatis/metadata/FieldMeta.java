package cn.org.atool.fluent.mybatis.metadata;

import lombok.Getter;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.lang.reflect.Field;

import static cn.org.atool.fluent.mybatis.If.isBlank;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.camelToUnderline;

/**
 * BaseFieldInfo
 *
 * @author darui.wu 2020/5/27 6:46 下午
 */
@SuppressWarnings({"unused"})
@Getter
public abstract class FieldMeta implements Comparable<FieldMeta> {
    /**
     * 字段名
     */
    protected final String column;
    /**
     * 属性名
     */
    protected final String property;
    /**
     * 属性类型
     */
    protected final Class<?> propertyType;
    /**
     * JDBC类型
     */
    private JdbcType jdbcType;
    /**
     * 类型处理器
     */
    protected Class<? extends TypeHandler<?>> typeHandler;
    /**
     * 属性表达式#{property}, 可以指定jdbcType, typeHandler等
     */
    protected String el;

    public FieldMeta(String column, Field field) {
        this.property = field.getName();
        this.column = isBlank(column) ? camelToUnderline(this.property, false) : column;
        this.propertyType = field.getType();
    }

    public void setJdbcType(JdbcType jdbcType) {
        if (JdbcType.UNDEFINED == jdbcType) {
            this.jdbcType = null;
        } else {
            this.jdbcType = jdbcType;
        }
    }

    protected String el() {
        String el = this.property;
        if (this.jdbcType != null) {
            el += (", jdbcType = " + jdbcType.name());
        }
        if (typeHandler != null) {
            el += (", typeHandler = " + typeHandler.getName());
        }
        return el;
    }

    /**
     * 非大字段
     *
     * @return true: 大字段
     */
    public boolean isNotLarge() {
        return true;
    }

    @Override
    public int compareTo(FieldMeta o) {
        return this.column.compareTo(o.getColumn());
    }
}