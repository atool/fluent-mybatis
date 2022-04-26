package cn.org.atool.fluent.processor.mybatis.entity;

import com.squareup.javapoet.TypeName;
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

    public PrimaryField(String name, TypeName javaType) {
        super(name, javaType);
    }

    @Override
    public boolean isPrimary() {
        return true;
    }
}