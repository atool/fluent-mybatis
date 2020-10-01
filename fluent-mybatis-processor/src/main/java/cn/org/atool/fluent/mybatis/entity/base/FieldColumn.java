package cn.org.atool.fluent.mybatis.entity.base;

import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.utility.MybatisUtil;
import com.sun.source.tree.Tree;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.TypeTag;
import com.sun.tools.javac.tree.JCTree;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.JdbcType;

import java.util.function.Consumer;

import static com.sun.tools.javac.tree.JCTree.*;

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

    private Type type;

    private JdbcType jdbcType;

    private String seqName;

    private String numericScale;
    /**
     * type handler
     */
    private String typeHandler;

    private boolean notLarge = true;

    private String insert;

    private String update;

    public FieldColumn setProperty(String property) {
        this.property = property;
        if (column == null) {
            column = MybatisUtil.camelToUnderline(this.property, false);
        }
        return this;
    }

    public boolean isPrimitive() {
        return type.isPrimitive();
    }

    /**
     * get方法名称
     *
     * @return
     */
    public String getMethodName() {
        if (isPrimitive() && type.getTag() == TypeTag.BOOLEAN) {
            return "is" + MybatisUtil.capitalFirst(this.property, "is");
        } else {
            return "get" + MybatisUtil.capitalFirst(this.property, null);
        }
    }

    /**
     * set方法名称
     *
     * @return
     */
    public String setMethodName() {
        if (isPrimitive() && type.getTag() == TypeTag.BOOLEAN) {
            return "set" + MybatisUtil.capitalFirst(this.property, "is");
        } else {
            return "set" + MybatisUtil.capitalFirst(this.property, null);
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