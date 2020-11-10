package cn.org.atool.fluent.mybatis.processor.entity;

import com.squareup.javapoet.TypeName;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 字段描述基类
 *
 * @author darui.wu
 */
@Getter
@ToString
@Accessors(chain = true)
@EqualsAndHashCode(of = "name")
public abstract class FieldOrMethod<F extends FieldOrMethod<F>> {
    /**
     * 字段或方法名称
     */
    protected String name;
    /**
     * 字段定义类型
     */
    protected TypeName javaType;

    protected FieldOrMethod(String name, TypeName javaType) {
        this.name = name;
        this.javaType = javaType;
    }

    public F setJavaType(TypeName javaType) {
        this.javaType = javaType;
        return (F) this;
    }

    /**
     * javaType是否是原生类型int, double...
     *
     * @return
     */
    public boolean isPrimitive() {
        return javaType.isPrimitive();
    }
}