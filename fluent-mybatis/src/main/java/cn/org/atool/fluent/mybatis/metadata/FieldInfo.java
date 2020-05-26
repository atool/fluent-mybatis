package cn.org.atool.fluent.mybatis.metadata;

import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.UnknownTypeHandler;

import java.lang.reflect.Field;

import static cn.org.atool.fluent.mybatis.util.Constants.COMMA;
import static cn.org.atool.fluent.mybatis.util.MybatisUtil.isNotEmpty;

/**
 * 数据库表字段反射信息
 *
 * @author darui.wu
 */
@Getter
@ToString
@EqualsAndHashCode
public class FieldInfo implements Comparable<FieldInfo> {
    /**
     * 字段名
     */
    private final String column;
    /**
     * 属性名
     */
    private final String property;
    /**
     * 属性类型
     */
    private final Class<?> propertyType;
    /**
     * 属性表达式#{property}, 可以指定jdbcType, typeHandler等
     */
    private final String el;
    /**
     * numericScale
     */
    private String numericScale;
    /**
     * 是否进行 select 查询
     * <p>大字段可设置为 false 不加入 select 查询范围</p>
     */
    private boolean select = true;
    /**
     * 字段 update set 部分注入
     */
    private String update;
    /**
     * 字段填充策略
     */
    private String insert;
    /**
     * JDBC类型
     *
     * @since 3.1.2
     */
    private JdbcType jdbcType;
    /**
     * 类型处理器
     *
     * @since 3.1.2
     */
    private Class<? extends TypeHandler<?>> typeHandler;

    public FieldInfo(Field field, TableId tableId) {
        this.column = tableId.value();
        this.property = field.getName();
        this.propertyType = field.getType();
        this.jdbcType = JdbcType.UNDEFINED == tableId.jdbcType() ? null : tableId.jdbcType();
        this.jdbcType = null;
        this.typeHandler = null;
        this.el = this.el();
        this.select = true;
    }

    /**
     * 全新的 存在 TableField 注解时使用的构造函数
     */
    public FieldInfo(Field field, TableField tableField) {
        this.property = field.getName();
        this.propertyType = field.getType();
        this.jdbcType = JdbcType.UNDEFINED == tableField.jdbcType() ? null : tableField.jdbcType();
        this.numericScale = tableField.numericScale();
        this.typeHandler = UnknownTypeHandler.class == tableField.typeHandler() ? null : (Class<? extends TypeHandler<?>>) tableField.typeHandler();
        this.el = this.el();
        this.column = tableField.value();
        this.select = tableField.select();

        this.insert = tableField.insert();
        this.update = tableField.update();
    }

    private String el() {
        String el = this.property;
        if (this.jdbcType != null) {
            el += (COMMA + "jdbcType=" + jdbcType.name());
        }
        if (typeHandler != null) {
            el += (COMMA + "typeHandler=" + typeHandler.getName());
        }
        if (isNotEmpty(numericScale)) {
            el += (COMMA + "numericScale=" + numericScale);
        }
        return el;
    }

    @Override
    public int compareTo(FieldInfo info) {
        return this.column.compareTo(info == null ? null : info.getColumn());
    }
}