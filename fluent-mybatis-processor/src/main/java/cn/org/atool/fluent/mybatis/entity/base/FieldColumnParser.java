package cn.org.atool.fluent.mybatis.entity.base;

import cn.org.atool.fluent.mybatis.annotation.NotField;
import cn.org.atool.fluent.mybatis.annotation.RefField;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.entity.field.BaseField;
import cn.org.atool.fluent.mybatis.entity.field.CommonField;
import cn.org.atool.fluent.mybatis.entity.field.EntityRefField;
import cn.org.atool.fluent.mybatis.entity.field.PrimaryField;
import com.sun.source.tree.Tree;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAssign;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.UnknownTypeHandler;

import javax.lang.model.element.Modifier;
import javax.tools.Diagnostic;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import static cn.org.atool.fluent.mybatis.If.notBlank;

/**
 * 字段解析工具类
 *
 * @author wudarui
 */
public class FieldColumnParser {
    private IProcessor processor;

    public FieldColumnParser(IProcessor processor) {
        this.processor = processor;
    }

    /**
     * 解析字段
     *
     * @param variable
     * @return
     */
    public BaseField valueOf(JCTree.JCVariableDecl variable) {
        String property = variable.getName().toString();
        Type javaType = variable.getType().type;
        BaseField field = null;
        for (JCTree.JCAnnotation annotation : variable.mods.annotations) {
            String type = annotation.type.toString();
            if (type.contains(TableId.class.getSimpleName())) {
                field = new PrimaryField(property, javaType);
            } else if (type.contains(TableField.class.getSimpleName())) {
                field = new CommonField(property, javaType);
            } else if (type.contains(RefField.class.getSimpleName())) {
                field = new EntityRefField(property, javaType);
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
     * 设置@TableId, @TableField, @RefField 属性
     *
     * @param field
     * @param annotation 注解
     */
    private void setTableField(BaseField field, JCTree.JCAnnotation annotation) {
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
            } else if (field instanceof EntityRefField) {
                this.setRefEntity((EntityRefField) field, assign);
            }
        }
    }

    private void setRefEntity(EntityRefField field, JCAssign assign) {
        this.setStrArr(field, assign, "value", new String[0], field::setValue);
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

    private void setStrArr(BaseField field, JCAssign assign, String method, String[] _default, Consumer<String[]> consumer) {
        String[] value = _default;
        try {
            if (Objects.equals(method(assign), method)) {
                List<String> list = new ArrayList<>();
                if (assign.rhs instanceof JCTree.JCNewArray) {
                    JCTree.JCNewArray arr = (JCTree.JCNewArray) assign.rhs;
                    for (JCTree.JCExpression expression : arr.elems) {
                        String exp = expression.toString();
                        list.add(exp.substring(1, exp.length() - 1));
                    }
                } else if (assign.rhs instanceof JCTree.JCLiteral) {
                    Object obj = ((JCTree.JCLiteral) assign.rhs).value;
                    list.add(String.valueOf(obj));
                }
                value = list.toArray(new String[0]);
            }
            if (value != null) {
                consumer.accept(value);
            }
        } catch (Exception e) {
            this.printError("=====" + field.getProperty() + ":" + method + "===== error:\n" + e.getMessage());
            throw e;
        }
    }

    /**
     * 获取 @TableField 或 @TableId 上定义的属性值
     *
     * @param field    字段
     * @param assign
     * @param method
     * @param _default 默认值
     * @param consumer
     * @return
     */
    private void setValue(BaseField field, JCAssign assign, String method, String _default, Consumer<String> consumer) {
        try {
            String value = _default;
            if (Objects.equals(method(assign), method)) {

//            if (assign.rhs instanceof JCTree.JCLiteral) {
                value = String.valueOf(((JCTree.JCLiteral) assign.rhs).value);
//            } else if (assign.rhs instanceof JCTree.JCIdent) {
//                JCIdent ident = (JCIdent) assign.rhs;
//                value = String.valueOf(ident.name);
//                this.printError("=======" + assign.getVariable().toString() + "," + assign.getExpression().toString());
//            }
            }
            if (notBlank(value)) {
                consumer.accept(value);
            }
        } catch (Exception e) {
            this.printError("=====" + field.getProperty() + ":" + method + "===== error:\n" + e.getMessage());
            throw e;
        }
    }

    /**
     * 设置枚举值
     *
     * @param assign
     * @param method
     * @param consumer
     */
    private void setEnumVal(BaseField field, JCAssign assign, String method, Consumer<String> consumer) {
        if (!Objects.equals(method(assign), method)) {
            return;
        }
        try {
            String value = ((JCTree.JCFieldAccess) assign.rhs).name.toString();
            consumer.accept(value);
        } catch (Exception e) {
            this.printError("=====" + field.getProperty() + ":" + method + "===== error:\n" + e.getMessage());
            throw e;
        }
    }

    /**
     * 设置 Class 属性
     *
     * @param assign
     * @param method
     * @param consumer
     */
    private void setTypeArg(BaseField field, JCAssign assign, String method, Consumer<Type> consumer) {
        try {
            if (!Objects.equals(method(assign), method)) {
                return;
            }
            Type value = ((JCTree.JCFieldAccess) assign.rhs).type.getTypeArguments().get(0);
            consumer.accept(value);
        } catch (Exception e) {
            this.printError("=====" + field.getProperty() + ":" + method + "===== error:\n" + e.getMessage());
            throw e;
        }
    }

    /**
     * 返回主键属性名称
     *
     * @param assign
     * @return
     */
    private String method(JCAssign assign) {
        return (((JCIdent) assign.lhs).name).toString();
    }

    private void printError(String message) {
        this.processor.getMessager().printMessage(Diagnostic.Kind.ERROR, message);
    }
}