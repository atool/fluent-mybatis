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
    /**
     * Messager
     */
    protected final Messager messager;

    /**
     * 构造函数
     *
     * @param messager Messager
     */
    protected AScanner(Messager messager) {
        this.messager = messager;
    }

    /**
     * 扫描
     *
     * @param element TypeElement
     * @return AScanner
     */
    public abstract AScanner scan(TypeElement element);

    /**
     * 转换为TypeElement
     *
     * @param typeMirror TypeMirror
     * @return TypeElement
     */
    protected TypeElement asTypeElement(TypeMirror typeMirror) {
        return (TypeElement) ((DeclaredType) typeMirror).asElement();
    }
}
