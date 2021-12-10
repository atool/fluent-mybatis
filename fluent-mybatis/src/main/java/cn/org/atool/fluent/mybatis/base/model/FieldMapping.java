package cn.org.atool.fluent.mybatis.base.model;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.functions.IGetter;
import cn.org.atool.fluent.mybatis.functions.ISetter;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import static cn.org.atool.fluent.mybatis.If.isBlank;
import static cn.org.atool.fluent.mybatis.utility.StrConstant.*;

/**
 * FieldMeta: 实体字段和数据库字段映射信息
 *
 * @author darui.wu  2020/6/23 9:16 上午
 */
@SuppressWarnings("rawtypes")
@Accessors(chain = true)
@EqualsAndHashCode(of = {"name", "column"})
public class FieldMapping<E extends IEntity> {
    /**
     * 属性名称
     */
    public final String name;
    /**
     * 字段名称
     */
    public final String column;
    /**
     * 字段类型
     */
    public final UniqueType uniqueType;
    /**
     * 插入时的默认值
     */
    public final String insert;
    /**
     * 更新时的默认值
     */
    public final String update;
    /**
     * java 类型
     */
    public final Class javaType;
    /**
     * type Handler
     */
    public final Class typeHandler;

    /**
     * e->((BlobValuePoJo)e).getId()
     */
    public IGetter<E> getter;

    /**
     * (e,v)->((BlobValuePoJo)e).setId((Long)v)
     */
    public ISetter<E> setter;

    /**
     * sg: setter, getter简写
     *
     * @param setter setter方法
     * @param getter getter方法
     * @return FieldMapping
     */
    public FieldMapping<E> sg(ISetter<E> setter, IGetter<E> getter) {
        if (this.setter != null || this.getter != null) {
            throw new RuntimeException("Secondary assignment is not allowed.");
        }
        this.getter = getter;
        this.setter = setter;
        return this;
    }

    public FieldMapping(String name, String column, UniqueType uniqueType, String insert, String update, Class javaType, Class typeHandler) {
        this.name = name;
        this.column = column;
        this.uniqueType = uniqueType;
        this.insert = insert;
        this.update = update;
        this.javaType = javaType;
        this.typeHandler = typeHandler;
    }

    public FieldMapping(String name, String column) {
        this(name, column, null, null, null, null, null);
    }

    /**
     * 返回字段名称
     *
     * @return ignore
     */
    @Override
    public String toString() {
        return this.name;
    }

    /**
     * 变量表达式
     *
     * @param prefix 变量名称前缀
     * @param name   外部传入的变量名称, 如果为空, 取内部的name
     * @return 变量表达式
     */
    public String var(String prefix, String name) {
        String full = (isBlank(prefix) ? EMPTY : (prefix.endsWith(DOT_STR) ? prefix : prefix + DOT_STR))
            +
            (isBlank(name) ? this.name : name);
        if (this.typeHandler == null) {
            return HASH_MARK_LEFT_CURLY + full + RIGHT_CURLY_BRACKET;
        } else {
            return String.format("#{%s, javaType=%s, typeHandler=%s}",
                full, this.javaType.getName(), this.typeHandler.getName());
        }
    }

    /**
     * alias.column
     *
     * @param alias 表别名
     * @return ignore
     */
    public String alias(String alias) {
        return alias(alias, column);
    }

    /**
     * tableAlias.column 表达式
     *
     * @param alias  table别名
     * @param column 字段
     * @return ignore
     */
    public static String alias(String alias, String column) {
        return isBlank(alias) ? column : alias + "." + column;
    }

    /**
     * 主键字段
     *
     * @return true:主键字段
     */
    public boolean isPrimary() {
        return this.uniqueType == UniqueType.PRIMARY_ID;
    }

    /**
     * 版本号字段
     *
     * @return true:版本号字段
     */
    public boolean isVersion() {
        return this.uniqueType == UniqueType.LOCK_VERSION;
    }
}