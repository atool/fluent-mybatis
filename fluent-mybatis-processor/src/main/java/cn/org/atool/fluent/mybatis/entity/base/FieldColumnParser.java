package cn.org.atool.fluent.mybatis.entity.base;

import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import com.sun.source.tree.Tree;
import com.sun.tools.javac.tree.JCTree;

import java.util.function.Consumer;

/**
 * 字段解析工具类
 *
 * @author wudarui
 */
public class FieldColumnParser {

    /**
     * 解析字段
     *
     * @param variable
     * @return
     */
    public static FieldColumn valueOf(JCTree.JCVariableDecl variable) {
        FieldColumn field = new FieldColumn().setProperty(variable.getName().toString())
            .setType(variable.getType().type);

        boolean isPrimary = false;
        JCTree.JCAnnotation jcAnnotation = null;

        for (JCTree.JCAnnotation annotation : variable.mods.annotations) {
            String type = annotation.type.toString();
            if (type.contains(TableId.class.getSimpleName())) {
                jcAnnotation = annotation;
                isPrimary = true;
                break;
            } else if (type.contains(TableField.class.getSimpleName())) {
                jcAnnotation = annotation;
                break;
            }
        }
        if (jcAnnotation == null) {
            return field;
        }

        field.setPrimary(isPrimary);
        for (JCTree.JCExpression expression : jcAnnotation.args) {
            if (!expression.getKind().equals(Tree.Kind.ASSIGNMENT)) {
                continue;
            }
            JCTree.JCAssign assign = (JCTree.JCAssign) expression;
            if (!assign.lhs.getKind().equals(Tree.Kind.IDENTIFIER)) {
                continue;
            }
            setValue(assign, "value", field::setColumn);
            if (isPrimary) {
                setPrimaryField(field, assign);
            } else {
                setField(field, assign);
            }
        }
        return field;
    }

    /**
     * 设置主键属性
     *
     * @param field
     * @param assign
     */
    private static void setPrimaryField(FieldColumn field, JCTree.JCAssign assign) {
        setValue(assign, "auto", v -> field.setAutoIncrease(Boolean.valueOf(v)));
        setValue(assign, "seqName", field::setSeqName);
    }

    private static void setField(FieldColumn field, JCTree.JCAssign assign) {
        setValue(assign, "insert", field::setInsert);
        setValue(assign, "update", field::setUpdate);
        setValue(assign, "notLarge", v -> field.setNotLarge(Boolean.valueOf(v)));
        setValue(assign, "numericScale", field::setNumericScale);
        //setValue(assign, "typeHandler", field::setTypeHandler);
    }

    /**
     * 获取 @TableField 或 @TableId 上定义的属性值
     *
     * @param assign
     * @param method
     * @return
     */
    private static void setValue(JCTree.JCAssign assign, String method, Consumer<String> consumer) {
        if (!(((JCTree.JCIdent) assign.lhs).name).toString().equals(method)) {
            return;
        }
        String value = String.valueOf(((JCTree.JCLiteral) assign.rhs).value);
        consumer.accept(value);
    }
}
