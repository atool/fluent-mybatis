package cn.org.atool.fluent.mybatis.entity.javac;

import com.sun.source.tree.Tree;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;

import javax.lang.model.element.Modifier;
import java.util.Set;

import static com.sun.tools.javac.tree.JCTree.JCClassDecl;

/**
 * 查找@RefMethod method
 *
 * @author wudarui
 */
public class MethodTreeTranslator extends TreeTranslator {
    private Name rootClazzName;

    private List<JCMethodDecl> mMethods = List.nil();

    public MethodTreeTranslator(Name rootClazzName) {
        this.rootClazzName = rootClazzName;
    }

    public List<JCMethodDecl> accept(JCTree curTree) {
        curTree.accept(this);
        return this.mMethods;
    }

    @Override
    public void visitClassDef(JCClassDecl jcClassDecl) {
        if (jcClassDecl.name == rootClazzName) {
            mMethods = List.nil();
            jcClassDecl.defs.stream()
                .filter(it -> it.getKind() == Tree.Kind.METHOD)
                .map(it -> (JCMethodDecl) it)
                .filter(it -> {
                    Set<Modifier> modifiers = it.getModifiers().getFlags();
                    if (modifiers.contains(Modifier.STATIC) ||
                        modifiers.contains(Modifier.ABSTRACT) ||
                        !modifiers.contains(Modifier.PUBLIC)
                    ) {
                        return false;
                    } else {
                        return true;
                    }
                })
                .forEach(it -> mMethods = mMethods.append(it));
        }
        super.visitClassDef(jcClassDecl);
    }
}