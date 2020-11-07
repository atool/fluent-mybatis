package cn.org.atool.fluent.mybatis.entity.javac;

import cn.org.atool.fluent.mybatis.annotation.RefMethod;
import cn.org.atool.fluent.mybatis.entity.base.IProcessor;
import cn.org.atool.fluent.mybatis.entity.field.EntityRefMethod;
import cn.org.atool.fluent.mybatis.entity.field.FieldOrMethod;
import com.sun.source.tree.Tree;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAssign;

/**
 * 方法解析工具类
 *
 * @author wudarui
 */
public class MethodParser extends BaseParser {

    public MethodParser(IProcessor processor) {
        super(processor);
    }

    /**
     * 解析字段
     *
     * @param methodDecl
     * @return
     */
    public FieldOrMethod valueOf(JCTree.JCMethodDecl methodDecl) {
        String methodName = methodDecl.getName().toString();
        if (methodDecl.getReturnType() == null) {
            return null;
        }
        Type returnType = methodDecl.getReturnType().type;
        for (JCTree.JCAnnotation annotation : methodDecl.mods.annotations) {
            String type = annotation.type.toString();
            if (type.contains(RefMethod.class.getSimpleName())) {
                EntityRefMethod method = new EntityRefMethod(methodName, returnType);
                setTableMethod(method, annotation);
                return method;
            }
        }
        return null;
    }

    /**
     * 设置 @RefField 属性
     *
     * @param method
     * @param annotation 注解
     */
    private void setTableMethod(FieldOrMethod method, JCTree.JCAnnotation annotation) {
        for (JCTree.JCExpression expression : annotation.args) {
            if (!expression.getKind().equals(Tree.Kind.ASSIGNMENT)) {
                continue;
            }
            JCAssign assign = (JCAssign) expression;
            if (!assign.lhs.getKind().equals(Tree.Kind.IDENTIFIER)) {
                continue;
            }
            if (method instanceof EntityRefMethod) {
                this.setRefMethod((EntityRefMethod) method, assign);
            }
        }
    }

    private void setRefMethod(EntityRefMethod method, JCAssign assign) {
        this.setValue(method, assign, "value", "", method::setValue);
    }
}