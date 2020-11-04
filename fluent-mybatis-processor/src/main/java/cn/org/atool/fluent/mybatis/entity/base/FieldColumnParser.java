package cn.org.atool.fluent.mybatis.entity.base;

import cn.org.atool.fluent.mybatis.annotation.NotField;
import cn.org.atool.fluent.mybatis.annotation.RefMethod;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import com.sun.source.tree.Tree;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAssign;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.UnknownTypeHandler;

import java.util.Objects;
import java.util.function.Consumer;

import static cn.org.atool.fluent.mybatis.If.notBlank;

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
            .setJavaType(variable.getType().type);

        for (JCTree.JCAnnotation annotation : variable.mods.annotations) {
            String type = annotation.type.toString();
            if (type.contains(TableId.class.getSimpleName())) {
                setTableField(field, true, annotation);
                return field;
            } else if (type.contains(TableField.class.getSimpleName())) {
                setTableField(field, false, annotation);
                return field;
            } else if (type.contains(RefMethod.class.getSimpleName())) {
                setRefEntity(field, annotation);
                return null;//TODO
            } else if (type.contains(NotField.class.getSimpleName())) {
                return null;
            }
        }
        return field;
    }

    private static void setRefEntity(FieldColumn field, JCTree.JCAnnotation annotation) {
        //TODO
    }

    /**
     * 设置@TableId 和 @TableField属性
     *
     * @param field
     * @param isPrimary  是否@TableId
     * @param annotation 注解
     */
    private static void setTableField(FieldColumn field, boolean isPrimary, JCTree.JCAnnotation annotation) {
        field.setPrimary(isPrimary);
        for (JCTree.JCExpression expression : annotation.args) {
            if (!expression.getKind().equals(Tree.Kind.ASSIGNMENT)) {
                continue;
            }
            JCAssign assign = (JCAssign) expression;
            if (!assign.lhs.getKind().equals(Tree.Kind.IDENTIFIER)) {
                continue;
            }
            setValue(assign, "value", "", v -> {
                if (notBlank(v)) {
                    field.setColumn(v);
                }
            });
            if (isPrimary) {
                setPrimaryField(field, assign);
            } else {
                setField(field, assign);
            }
        }
    }

    /**
     * 设置主键属性
     *
     * @param field
     * @param assign
     */
    private static void setPrimaryField(FieldColumn field, JCAssign assign) {
        setValue(assign, "auto", "true", v -> field.setAutoIncrease(Boolean.valueOf(v)));
        setValue(assign, "seqName", "", field::setSeqName);
        setValue(assign, "before", "false", v -> field.setSeqIsBeforeOrder(Boolean.valueOf(v)));
    }

    private static void setField(FieldColumn field, JCAssign assign) {
        setValue(assign, "insert", "", field::setInsert);
        setValue(assign, "update", "", field::setUpdate);
        setValue(assign, "notLarge", "true", v -> field.setNotLarge(Boolean.valueOf(v)));
        setValue(assign, "numericScale", "", field::setNumericScale);
        setEnumVal(assign, "jdbcType", value -> {
            if (!Objects.equals(JdbcType.UNDEFINED.name(), value)) {
                field.setJdbcType(value);
            }
        });
        setTypeArg(assign, "typeHandler", type -> {
            if (type != null && !type.toString().endsWith(UnknownTypeHandler.class.getSimpleName())) {
                field.setTypeHandler(type);
            }
        });
    }

    /**
     * 获取 @TableField 或 @TableId 上定义的属性值
     *
     * @param assign
     * @param method
     * @param _default 默认值
     * @param consumer
     * @return
     */
    private static void setValue(JCAssign assign, String method, String _default, Consumer<String> consumer) {
        String value = _default;
        if (Objects.equals(method(assign), method)) {
            value = String.valueOf(((JCTree.JCLiteral) assign.rhs).value);
        }
        if (notBlank(value)) {
            consumer.accept(value);
        }
    }

    /**
     * 设置枚举值
     *
     * @param assign
     * @param method
     * @param consumer
     */
    private static void setEnumVal(JCAssign assign, String method, Consumer<String> consumer) {
        if (!Objects.equals(method(assign), method)) {
            return;
        }
        String value = ((JCTree.JCFieldAccess) assign.rhs).name.toString();
        consumer.accept(value);
    }

    /**
     * 设置 Class 属性
     *
     * @param assign
     * @param method
     * @param consumer
     */
    private static void setTypeArg(JCAssign assign, String method, Consumer<Type> consumer) {
        if (!Objects.equals(method(assign), method)) {
            return;
        }
        Type value = ((JCTree.JCFieldAccess) assign.rhs).type.getTypeArguments().get(0);
        consumer.accept(value);
    }

    /**
     * 返回主键属性名称
     *
     * @param assign
     * @return
     */
    private static String method(JCAssign assign) {
        return (((JCIdent) assign.lhs).name).toString();
    }
}