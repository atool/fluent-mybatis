package cn.org.atool.fluent.mybatis.entity.field;

import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.TypeTag;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.camelToUnderline;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.capitalFirst;

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
    protected String column;

    private String jdbcType;

    private String numericScale;
    /**
     * type handler
     */
    private Type typeHandler;

    private boolean notLarge = true;

    private String insert;

    private String update;

    public CommonField(String property, Type javaType) {
        super(property, javaType);
        // 设置column默认值
        this.column = camelToUnderline(this.name, false);
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

    public boolean isPrimary() {
        return false;
    }
}