package com.baomidou.mybatisplus.core.metadata;

import cn.org.atool.fluent.mybatis.method.metadata.FieldMeta;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.UnknownTypeHandler;

import java.lang.reflect.Field;

/**
 * TableFieldInfo
 *
 * @author darui.wu
 * @create 2020/6/3 11:54 上午
 */
@Deprecated
public class TableFieldInfo extends FieldMeta {
    public TableFieldInfo(String column, Field field) {
        super(column, field);
    }

    public TableFieldInfo(Field field, TableField tableField) {
        this(tableField.value(), field);
        this.setJdbcType(tableField.jdbcType());
        this.numericScale = tableField.numericScale();
        this.typeHandler = UnknownTypeHandler.class == tableField.typeHandler() ? null : (Class<? extends TypeHandler<?>>) tableField.typeHandler();

        this.selected = tableField.select();
        FieldFill fill = tableField.fill();
        if (fill == FieldFill.INSERT || fill == FieldFill.INSERT_UPDATE) {
            this.insert = tableField.update();
        }
        if (fill == FieldFill.UPDATE || fill == FieldFill.INSERT_UPDATE) {
            this.update = tableField.update();
        }
        this.el = this.el();
    }
}