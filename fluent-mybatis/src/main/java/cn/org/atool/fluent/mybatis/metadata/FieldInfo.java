package cn.org.atool.fluent.mybatis.metadata;

import cn.org.atool.fluent.mybatis.util.Constants;
import cn.org.atool.fluent.mybatis.util.StringUtils;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.MybatisConfiguration;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.apache.ibatis.type.UnknownTypeHandler;

import java.lang.reflect.Field;

/**
 * 数据库表字段反射信息
 *
 * @author darui.wu
 */
@Getter
@ToString
@EqualsAndHashCode
public class FieldInfo {

    /**
     * 是否有存在字段名与属性名关联
     * <p>true: 表示要进行 as</p>
     */
    private final boolean related;
    /**
     * 字段名
     */
    private final String column;
    /**
     * 属性名
     */
    private final String property;
    /**
     * 属性表达式#{property}, 可以指定jdbcType, typeHandler等
     */
    private final String el;
    /**
     * 属性类型
     */
    private final Class<?> propertyType;
    /**
     * 属性是否是 CharSequence 类型
     */
    private final boolean isCharSequence;

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
     * 缓存 sql select
     */
    @Getter(AccessLevel.NONE)
    private String sqlSelect;
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

    /**
     * 全新的 存在 TableField 注解时使用的构造函数
     */
    public FieldInfo(Field field, TableField tableField) {
        this.property = field.getName();
        this.propertyType = field.getType();
        this.isCharSequence = StringUtils.isCharSequence(this.propertyType);
        this.insert = tableField.insert();
        this.update = tableField.update();
        JdbcType jdbcType = tableField.jdbcType();
        final Class<? extends TypeHandler> typeHandler = tableField.typeHandler();
        final String numericScale = tableField.numericScale();
        String el = this.property;
        if (JdbcType.UNDEFINED != jdbcType) {
            this.jdbcType = jdbcType;
            el += (Constants.COMMA + "jdbcType=" + jdbcType.name());
        }
        if (UnknownTypeHandler.class != typeHandler) {
            this.typeHandler = (Class<? extends TypeHandler<?>>) typeHandler;
            el += (Constants.COMMA + "typeHandler=" + typeHandler.getName());
        }
        if (StringUtils.isNotEmpty(numericScale)) {
            el += (Constants.COMMA + "numericScale=" + numericScale);
        }
        this.el = el;
        this.column = tableField.value();
        this.related = TableHelper.checkRelated(this.property, this.column);

        // 字段是否注入查询
        this.select = tableField.select();
    }

    /**
     * 获取 select sql 片段
     *
     * @return sql 片段
     */
    public String getSqlSelect() {
        if (sqlSelect != null) {
            return sqlSelect;
        }
        sqlSelect = column;
        if (related) {
            sqlSelect += (" AS " + property);
        }
        return sqlSelect;
    }


    /**
     * 获取 ResultMapping
     *
     * @param configuration MybatisConfiguration
     * @return ResultMapping
     */
    ResultMapping getResultMapping(final MybatisConfiguration configuration) {
        ResultMapping.Builder builder = new ResultMapping.Builder(configuration, property,
            StringUtils.getTargetColumn(column), propertyType);
        TypeHandlerRegistry registry = configuration.getTypeHandlerRegistry();
        if (jdbcType != null && jdbcType != JdbcType.UNDEFINED) {
            builder.jdbcType(jdbcType);
        }
        if (typeHandler != null && typeHandler != UnknownTypeHandler.class) {
            TypeHandler<?> typeHandler = registry.getMappingTypeHandler(this.typeHandler);
            if (typeHandler == null) {
                typeHandler = registry.getInstance(propertyType, this.typeHandler);
                // todo 这会有影响 registry.register(typeHandler);
            }
            builder.typeHandler(typeHandler);
        }
        return builder.build();
    }
}