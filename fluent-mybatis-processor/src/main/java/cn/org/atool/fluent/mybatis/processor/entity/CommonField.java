package cn.org.atool.fluent.mybatis.processor.entity;


import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Objects;

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
public class CommonField extends FieldOrMethod<CommonField> {
    /**
     * 字段对应的表字段
     * 对关联字段非必须
     */
    @Setter(AccessLevel.NONE)
    protected String column;

    private String jdbcType;

    private String numericScale;
    /**
     * type handler
     */
    private TypeName typeHandler;

    private boolean notLarge = true;

    private String insert;

    private String update;

    public CommonField(String property, TypeName javaType) {
        super(property, javaType);
        // 设置column默认值
        this.column = camelToUnderline(this.name, false);
    }

    public CommonField setColumn(String column) {
        if (!isBlank(column)) {
            this.column = column;
        }
        return this;
    }

    /**
     * get方法名称
     *
     * @return
     */
    public String getMethodName() {
        if (isPrimitive() && Objects.equals(javaType, ClassName.BOOLEAN)) {
            return "is" + capitalFirst(this.name, "is");
        } else {
            return "get" + capitalFirst(this.name, null);
        }
    }

    /**
     * set方法名称
     *
     * @return
     */
    public String setMethodName() {
        if (isPrimitive() && Objects.equals(javaType, ClassName.BOOLEAN)) {
            return "set" + capitalFirst(this.name, "is");
        } else {
            return "set" + capitalFirst(this.name, null);
        }
    }

    public boolean isPrimary() {
        return false;
    }
}