package cn.org.atool.fluent.mybatis.entity.base;

import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.TypeTag;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.camelToUnderline;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.capitalFirst;

@Data
@Accessors(chain = true)
public class FieldColumn {
    /**
     * 是否主键
     */
    private boolean primary = false;
    /**
     * 自增
     */
    private boolean autoIncrease = false;

    @Setter(AccessLevel.NONE)
    private String property;

    private String column;

    private Type javaType;

    private String jdbcType;

    private String seqName;

    private boolean seqIsBeforeOrder;

    private String numericScale;
    /**
     * type handler
     */
    private Type typeHandler;

    private boolean notLarge = true;

    private String insert;

    private String update;

    public FieldColumn setProperty(String property) {
        this.property = property;
        if (column == null) {
            column = camelToUnderline(this.property, false);
        }
        return this;
    }

    public boolean isPrimitive() {
        return javaType.isPrimitive();
    }

    /**
     * get方法名称
     *
     * @return
     */
    public String getMethodName() {
        if (isPrimitive() && javaType.getTag() == TypeTag.BOOLEAN) {
            return "is" + capitalFirst(this.property, "is");
        } else {
            return "get" + capitalFirst(this.property, null);
        }
    }

    /**
     * set方法名称
     *
     * @return
     */
    public String setMethodName() {
        if (isPrimitive() && javaType.getTag() == TypeTag.BOOLEAN) {
            return "set" + capitalFirst(this.property, "is");
        } else {
            return "set" + capitalFirst(this.property, null);
        }
    }

    /**
     * mybatis el 表达式
     *
     * @return
     */
    public String mybatisEl() {
        return this.column + " = #{" + this.property + "}";
    }

    public String getPropertyEl() {
        return "#{" + this.property + "}";
    }
}