package cn.org.atool.fluent.mybatis.entity.javac;

import cn.org.atool.fluent.mybatis.entity.base.IProcessor;
import cn.org.atool.fluent.mybatis.entity.field.FieldOrMethod;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAssign;
import com.sun.tools.javac.tree.JCTree.JCIdent;

import javax.tools.Diagnostic;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import static cn.org.atool.fluent.mybatis.If.notBlank;

public abstract class BaseParser {
    protected IProcessor processor;

    public BaseParser(IProcessor processor) {
        this.processor = processor;
    }

    protected void setStrArr(FieldOrMethod field, JCAssign assign, String method, String[] _default, Consumer<String[]> consumer) {
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
            this.printError("=====" + field.getName() + ":" + method + "===== error:\n" + e.getMessage());
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
    protected void setValue(FieldOrMethod field, JCAssign assign, String method, String _default, Consumer<String> consumer) {
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
            this.printError("=====" + field.getName() + ":" + method + "===== error:\n" + e.getMessage());
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
    protected void setEnumVal(FieldOrMethod field, JCAssign assign, String method, Consumer<String> consumer) {
        if (!Objects.equals(method(assign), method)) {
            return;
        }
        try {
            String value = ((JCTree.JCFieldAccess) assign.rhs).name.toString();
            consumer.accept(value);
        } catch (Exception e) {
            this.printError("=====" + field.getName() + ":" + method + "===== error:\n" + e.getMessage());
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
    protected void setTypeArg(FieldOrMethod field, JCAssign assign, String method, Consumer<Type> consumer) {
        try {
            if (!Objects.equals(method(assign), method)) {
                return;
            }
            Type value = ((JCTree.JCFieldAccess) assign.rhs).type.getTypeArguments().get(0);
            consumer.accept(value);
        } catch (Exception e) {
            this.printError("=====" + field.getName() + ":" + method + "===== error:\n" + e.getMessage());
            throw e;
        }
    }

    /**
     * 返回主键属性名称
     *
     * @param assign
     * @return
     */
    protected String method(JCAssign assign) {
        return (((JCIdent) assign.lhs).name).toString();
    }

    protected void printError(String message) {
        this.processor.getMessager().printMessage(Diagnostic.Kind.ERROR, message);
    }
}
