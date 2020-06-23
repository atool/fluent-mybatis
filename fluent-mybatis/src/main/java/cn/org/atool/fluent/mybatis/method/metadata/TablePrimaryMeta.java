package cn.org.atool.fluent.mybatis.method.metadata;

import cn.org.atool.fluent.mybatis.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;

/**
 * PrimaryInfo: 主键信息
 *
 * @author darui.wu
 * @create 2020/5/27 6:45 下午
 */
@Getter
public class TablePrimaryMeta extends BaseFieldMeta {
    /**
     * 属性表达式#{property}, 可以指定jdbcType, typeHandler等
     */
    private String el;
    /**
     * 主键ID是否自增
     */
    @Setter
    private boolean autoIncrease;
    /**
     * 表主键ID Sequence
     */
    private String seqName;

    public TablePrimaryMeta(String column, Field field) {
        super(column, field);
    }

    public TablePrimaryMeta(Field field, TableId tableId) {
        super(tableId.value(), field);
        this.setJdbcType(tableId.jdbcType());
        this.el = el();
        this.autoIncrease = tableId.auto();
        this.seqName = tableId.seqName();
    }
}