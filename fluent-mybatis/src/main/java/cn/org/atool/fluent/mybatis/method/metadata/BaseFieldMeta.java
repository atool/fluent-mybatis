package cn.org.atool.fluent.mybatis.method.metadata;

import lombok.Getter;
import org.apache.ibatis.type.JdbcType;

import java.lang.reflect.Field;

import static cn.org.atool.fluent.mybatis.segment.model.StrConstant.COMMA;

/**
 * BaseFieldInfo
 *
 * @author darui.wu
 * @create 2020/5/27 6:46 下午
 */
@Getter
public abstract class BaseFieldMeta implements Comparable<BaseFieldMeta> {
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

    public BaseFieldMeta(String column, Field field) {
        this.column = column;
        this.property = field.getName();
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

    public boolean isSelected() {
        return true;
    }

    @Override
    public int compareTo(BaseFieldMeta info) {
        return this.column.compareTo(info == null ? null : info.getColumn());
    }
}