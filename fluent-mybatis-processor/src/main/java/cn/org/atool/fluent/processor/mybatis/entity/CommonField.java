package cn.org.atool.fluent.processor.mybatis.entity;

import cn.org.atool.generator.database.model.FieldType;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.TypeName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Objects;

import static cn.org.atool.fluent.common.kits.StringKit.*;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.camelToUnderline;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.capitalFirst;
import static cn.org.atool.generator.util.GeneratorHelper.isBlank;

/**
 * 普通字段
 *
 * @author darui.wu
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class CommonField extends FieldOrMethod implements Comparable<CommonField> {
    /**
     * 字段对应的表字段
     * 对关联字段非必须
     */
    @Setter(AccessLevel.NONE)
    protected String column;

    /**
     * jdbc类型
     */
    private String jdbcType;

    /**
     * numeric scale
     */
    private String numericScale;
    /**
     * type handler
     */
    private TypeName typeHandler;

    /**
     * not large
     */
    private boolean notLarge = true;

    /**
     * insert
     */
    private String insert;

    /**
     * update
     */
    private String update;

    /**
     * file type
     */
    private FieldType type = FieldType.Common;

    /**
     * 构造函数
     *
     * @param property 属性名
     * @param javaType java类型
     */
    public CommonField(String property, TypeName javaType) {
        super(property, javaType);
        // 设置column默认值
        this.column = camelToUnderline(this.name, false);
    }

    /**
     * 设置列名
     *
     * @param column 列名
     * @return CommonField
     */
    public CommonField setColumn(String column) {
        if (!isBlank(column)) {
            this.column = column;
        }
        return this;
    }

    /**
     * get方法名称
     *
     * @return get(is) method name
     */
    public String getMethodName() {
        if (isPrimitive() && Objects.equals(javaType, ClassName.BOOLEAN)) {
            return PRE_IS + capitalFirst(this.name, PRE_IS);
        } else {
            return PRE_GET + capitalFirst(this.name);
        }
    }

    /**
     * set方法名称
     *
     * @return set method name
     */
    public String setMethodName() {
        if (isPrimitive() && Objects.equals(javaType, ClassName.BOOLEAN)) {
            return PRE_SET + capitalFirst(this.name, PRE_IS);
        } else {
            return PRE_SET + capitalFirst(this.name);
        }
    }

    /**
     * 是否主键
     *
     * @return true: primary key
     */
    public boolean isPrimary() {
        return false;
    }

    @Override
    public int compareTo(CommonField field) {
        int compare = this.type.compareTo(field.type);
        if (compare == 0) {
            return this.name.compareTo(field.name);
        } else {
            return compare;
        }
    }
}