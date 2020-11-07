package cn.org.atool.fluent.mybatis.entity.field;

import com.sun.tools.javac.code.Type;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 主键字段
 *
 * @author darui.wu
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class PrimaryField extends CommonField {
    /**
     * 是否自增主键
     */
    private boolean autoIncrease = false;
    /**
     * 自增seqName定义
     */
    private String seqName;

    private boolean seqIsBeforeOrder;

    public PrimaryField(String name, Type javaType) {
        super(name, javaType);
    }

    @Override
    public boolean isPrimary() {
        return true;
    }
}