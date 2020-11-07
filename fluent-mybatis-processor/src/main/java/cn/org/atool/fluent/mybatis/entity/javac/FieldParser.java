package cn.org.atool.fluent.mybatis.entity.javac;

import cn.org.atool.fluent.mybatis.annotation.NotField;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.entity.base.IProcessor;
import cn.org.atool.fluent.mybatis.entity.field.CommonField;
import cn.org.atool.fluent.mybatis.entity.field.FieldOrMethod;
import cn.org.atool.fluent.mybatis.entity.field.PrimaryField;
import com.sun.source.tree.Tree;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAssign;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.UnknownTypeHandler;

import javax.lang.model.element.Modifier;
import java.util.Objects;

/**
 * 字段解析工具类
 *
 * @author wudarui
 */
public class FieldParser extends BaseParser {

    public FieldParser(IProcessor processor) {
        super(processor);
    }

    /**
     * 解析字段
     *
     * @param variable
     * @return
     */
    public FieldOrMethod valueOf(JCTree.JCVariableDecl variable) {
        String property = variable.getName().toString();
        Type javaType = variable.getType().type;
        FieldOrMethod field = null;
        for (JCTree.JCAnnotation annotation : variable.mods.annotations) {
            String type = annotation.type.toString();
            if (type.contains(TableId.class.getSimpleName())) {
                field = new PrimaryField(property, javaType);
            } else if (type.contains(TableField.class.getSimpleName())) {
                field = new CommonField(property, javaType);
            } else if (type.contains(NotField.class.getSimpleName())) {
                return null;
            }
            if (field != null) {
                setTableField(field, annotation);
                return field;
            }
        }
        /** 默认无注解情况 **/
        if (variable.getModifiers().getFlags().contains(Modifier.TRANSIENT)) {
            return null;
        } else {
            return new CommonField(property, javaType);
        }
    }

    /**
     * 设置@TableId, @TableField 属性
     *
     * @param field
     * @param annotation 注解
     */
    private void setTableField(FieldOrMethod field, JCTree.JCAnnotation annotation) {
        for (JCTree.JCExpression expression : annotation.args) {
            if (!expression.getKind().equals(Tree.Kind.ASSIGNMENT)) {
                continue;
            }
            JCAssign assign = (JCAssign) expression;
            if (!assign.lhs.getKind().equals(Tree.Kind.IDENTIFIER)) {
                continue;
            }

            if (field instanceof PrimaryField) {
                this.setPrimaryField((PrimaryField) field, assign);
            } else if (field instanceof CommonField) {
                this.setCommonField((CommonField) field, assign);
            }
        }
    }

    /**
     * 设置主键属性
     *
     * @param field
     * @param assign
     */
    private void setPrimaryField(PrimaryField field, JCAssign assign) {
        this.setValue(field, assign, "value", "", field::setColumn);
        this.setValue(field, assign, "auto", "true", v -> field.setAutoIncrease(Boolean.valueOf(v)));
        this.setValue(field, assign, "seqName", "", field::setSeqName);
        this.setValue(field, assign, "before", "false", v -> field.setSeqIsBeforeOrder(Boolean.valueOf(v)));
    }

    private void setCommonField(CommonField field, JCAssign assign) {
        this.setValue(field, assign, "value", "", field::setColumn);
        this.setValue(field, assign, "insert", "", field::setInsert);
        this.setValue(field, assign, "update", "", field::setUpdate);
        this.setValue(field, assign, "notLarge", "true", v -> field.setNotLarge(Boolean.valueOf(v)));
        this.setValue(field, assign, "numericScale", "", field::setNumericScale);
        this.setEnumVal(field, assign, "jdbcType", value -> {
            if (!Objects.equals(JdbcType.UNDEFINED.name(), value)) {
                field.setJdbcType(value);
            }
        });
        this.setTypeArg(field, assign, "typeHandler", type -> {
            if (type != null && !type.toString().endsWith(UnknownTypeHandler.class.getSimpleName())) {
                field.setTypeHandler(type);
            }
        });
    }
}