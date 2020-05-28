package cn.org.atool.fluent.mybatis.method.metadata;

import cn.org.atool.fluent.mybatis.annotation.TableField;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
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
@EqualsAndHashCode(callSuper = false)
public class FieldMeta extends BaseFieldMeta {
    /**
     * 属性表达式#{property}, 可以指定jdbcType, typeHandler等
     */
    private final String el;
    /**
     * 类型处理器
     */
    private Class<? extends TypeHandler<?>> typeHandler;
    /**
     * numericScale
     */
    private String numericScale;
    /**
     * 是否进行 select 查询
     * <p>大字段可设置为 false 不加入 select 查询范围</p>
     */
    private boolean selected = true;
    /**
     * 字段 update set 部分注入
     */
    private String update;
    /**
     * 字段填充策略
     */
    private String insert;

    /**
     * 全新的 存在 TableField 注解时使用的构造函数
     */
    public FieldMeta(Field field, TableField tableField) {
        super(tableField.value(), field);
        this.setJdbcType(tableField.jdbcType());
        this.numericScale = tableField.numericScale();
        this.typeHandler = UnknownTypeHandler.class == tableField.typeHandler() ? null : (Class<? extends TypeHandler<?>>) tableField.typeHandler();

        this.selected = tableField.select();
        this.insert = tableField.insert();
        this.update = tableField.update();

        this.el = this.el();
    }

    @Override
    protected String el() {
        String el = super.el();
        if (typeHandler != null) {
            el += (COMMA + "typeHandler=" + typeHandler.getName());
        }
        if (isNotEmpty(numericScale)) {
            el += (COMMA + "numericScale=" + numericScale);
        }
        return el;
    }
}