package cn.org.atool.fluent.mybatis.metadata;

import cn.org.atool.fluent.mybatis.annotation.TableField;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.UnknownTypeHandler;

import java.lang.reflect.Field;

import static cn.org.atool.fluent.mybatis.If.isBlank;
import static cn.org.atool.fluent.mybatis.If.notBlank;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.camelToUnderline;

/**
 * 数据库表字段反射信息
 *
 * @author darui.wu
 */
@SuppressWarnings({"unchecked"})
@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public class TableFieldMeta extends FieldMeta {
    /**
     * 属性表达式#{property}, 可以指定jdbcType, typeHandler等
     */
    protected String el;
    /**
     * 类型处理器
     */
    protected Class<? extends TypeHandler<?>> typeHandler;
    /**
     * numericScale
     */
    protected String numericScale;
    /**
     * 是否非大字段查询
     * <p>大字段可设置为 false 不加入 select 查询范围</p>
     */
    protected boolean notLarge = true;
    /**
     * 字段 update set 部分注入
     */
    protected String update;
    /**
     * 字段填充策略
     */
    protected String insert;

    /**
     * 全新的 存在 TableField 注解时使用的构造函数
     */
    public TableFieldMeta(Field field, TableField tableField) {
        super(tableField == null || isBlank(tableField.value()) ? camelToUnderline(field.getName(), false) : tableField.value(), field);
        if (tableField != null) {
            this.setJdbcType(tableField.jdbcType());
            this.numericScale = tableField.numericScale();
            this.typeHandler = UnknownTypeHandler.class == tableField.typeHandler() ? null : (Class<? extends TypeHandler<?>>) tableField.typeHandler();

            this.notLarge = tableField.notLarge();
            this.insert = tableField.insert();
            this.update = tableField.update();
        }
        this.el = this.el();
    }

    @Override
    public String el() {
        String el = super.el();
        if (typeHandler != null) {
            el += (", typeHandler = " + typeHandler.getName());
        }
        if (notBlank(numericScale)) {
            el += (", numericScale = " + numericScale);
        }
        return el;
    }
}