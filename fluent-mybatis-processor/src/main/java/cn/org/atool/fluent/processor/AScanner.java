package cn.org.atool.fluent.processor;

import javax.annotation.processing.Messager;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

/**
 * Scanner基类
 *
 * @author wudarui
 */
public abstract class AScanner {
    protected final Messager messager;

    protected AScanner(Messager messager) {
        this.messager = messager;
    }

    public abstract AScanner scan(TypeElement element);

    protected TypeElement asTypeElement(TypeMirror typeMirror) {
        return (TypeElement) ((DeclaredType) typeMirror).asElement();
    }
}
