package cn.org.atool.fluent.mybatis.method.metadata;

import lombok.Getter;
import org.apache.ibatis.type.JdbcType;

import java.lang.reflect.Field;

import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.camelToUnderline;
import static cn.org.atool.fluent.mybatis.utility.Predicates.isBlank;

/**
 * BaseFieldInfo
 *
 * @author darui.wu
 * @create 2020/5/27 6:46 下午
 */
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

    public String el() {
        String el = this.property;
        if (this.jdbcType != null) {
            el += (", jdbcType = " + jdbcType.name());
        }
        return el;
    }

    /**
     * 非大字段
     *
     * @return
     */
    public boolean isNotLarge() {
        return true;
    }

    @Override
    public int compareTo(FieldMeta info) {
        return this.column.compareTo(info == null ? null : info.getColumn());
    }
}