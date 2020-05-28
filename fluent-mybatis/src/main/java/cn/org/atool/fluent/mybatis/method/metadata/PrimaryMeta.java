package cn.org.atool.fluent.mybatis.method.metadata;

import cn.org.atool.fluent.mybatis.annotation.TableId;
import lombok.Getter;

import java.lang.reflect.Field;

/**
 * PrimaryInfo: 主键信息
 *
 * @author darui.wu
 * @create 2020/5/27 6:45 下午
 */
@Getter
public class PrimaryMeta extends BaseFieldMeta {
    /**
     * 属性表达式#{property}, 可以指定jdbcType, typeHandler等
     */
    private final String el;
    /**
     * 主键ID是否自增
     */
    private boolean autoIncrease;
    /**
     * 表主键ID Sequence
     */
    private String seqName;

    public PrimaryMeta(Field field, TableId tableId) {
        super(tableId.value(), field);
        this.setJdbcType(tableId.jdbcType());
        this.el = el();
        this.autoIncrease = tableId.auto();
        this.seqName = tableId.seqName();
    }
}