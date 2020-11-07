package cn.org.atool.fluent.mybatis.entity.field;

import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.TypeTag;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.camelToUnderline;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.capitalFirst;

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
    /**
     * 字段对应的表字段
     * 对关联字段非必须
     */
    protected String column;

    protected FieldOrMethod(String name, Type javaType) {
        this.name = name;
        this.javaType = javaType;
    }

    public F setName(String name) {
        this.name = name;
        if (column == null) {
            column = camelToUnderline(this.name, false);
        }
        return (F) this;
    }

    public F setJavaType(Type javaType) {
        this.javaType = javaType;
        return (F) this;
    }

    public F setColumn(String column) {
        this.column = column;
        return (F) this;
    }

    /**
     * get方法名称
     *
     * @return
     */
    public String getMethodName() {
        if (isPrimitive() && javaType.getTag() == TypeTag.BOOLEAN) {
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
        if (isPrimitive() && javaType.getTag() == TypeTag.BOOLEAN) {
            return "set" + capitalFirst(this.name, "is");
        } else {
            return "set" + capitalFirst(this.name, null);
        }
    }

    public boolean isPrimitive() {
        return javaType.isPrimitive();
    }
}