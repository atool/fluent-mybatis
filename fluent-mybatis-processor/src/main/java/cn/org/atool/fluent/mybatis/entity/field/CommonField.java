package cn.org.atool.fluent.mybatis.entity.field;

import com.sun.tools.javac.code.Type;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 普通字段
 *
 * @author darui.wu
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class CommonField extends BaseField<CommonField> {

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

    public boolean isPrimary() {
        return false;
    }
}