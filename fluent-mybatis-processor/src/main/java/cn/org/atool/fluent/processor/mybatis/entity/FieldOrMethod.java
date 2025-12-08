package cn.org.atool.fluent.processor.mybatis.entity;

import com.palantir.javapoet.TypeName;
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
public abstract class FieldOrMethod {
    /**
     * 字段或方法名称
     */
    protected final String name;
    /**
     * 字段定义类型
     */
    protected final TypeName javaType;

    /**
     * 构造函数
     *
     * @param name     名称
     * @param javaType 类型
     */
    protected FieldOrMethod(String name, TypeName javaType) {
        this.name = name;
        this.javaType = javaType;
    }

    /**
     * javaType是否是原生类型int, double...
     *
     * @return true: primitive
     */
    public boolean isPrimitive() {
        return javaType.isPrimitive();
    }
}