package cn.org.atool.fluent.mybatis.entity.javac;

import com.sun.source.tree.Tree;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;

import javax.lang.model.element.Modifier;

import static com.sun.tools.javac.tree.JCTree.JCClassDecl;
import static com.sun.tools.javac.tree.JCTree.JCVariableDecl;

/**
 * 遍历字段
 *
 * @author wudarui
 */
public class FieldTreeTranslator extends TreeTranslator {
    private Name rootClazzName;

    private List<JCVariableDecl> mFields = List.nil();

    public FieldTreeTranslator(Name rootClazzName) {
        this.rootClazzName = rootClazzName;
    }

    public List<JCVariableDecl> accept(JCTree curTree) {
        curTree.accept(this);
        return this.mFields;
    }

    @Override
    public void visitClassDef(JCClassDecl jcClassDecl) {
        if (jcClassDecl.name == rootClazzName) {
            mFields = List.nil();
            jcClassDecl.defs.stream()
                .filter(it -> it.getKind() == Tree.Kind.VARIABLE)
                .map(it -> (JCVariableDecl) it)
                .filter(it -> !it.getModifiers().getFlags().contains(Modifier.STATIC))
                .forEach(it -> mFields = mFields.append(it));
        }
        super.visitClassDef(jcClassDecl);
    }
}