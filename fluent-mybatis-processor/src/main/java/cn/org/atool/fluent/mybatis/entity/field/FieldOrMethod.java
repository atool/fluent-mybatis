package cn.org.atool.fluent.mybatis.entity.field;

import com.sun.tools.javac.code.Type;
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
public abstract class FieldOrMethod<F extends FieldOrMethod<F>> {
    /**
     * 字段或方法名称
     */
    protected String name;
    /**
     * 字段定义类型
     */
    protected Type javaType;

    protected FieldOrMethod(String name, Type javaType) {
        this.name = name;
        this.javaType = javaType;
    }

    public F setJavaType(Type javaType) {
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