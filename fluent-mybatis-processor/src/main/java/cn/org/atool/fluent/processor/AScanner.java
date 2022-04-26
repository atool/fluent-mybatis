package cn.org.atool.fluent.processor;

import javax.annotation.processing.Messager;
import javax.lang.model.element.TypeElement;

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

    public abstract void scan(TypeElement element);
}
