package cn.org.atool.fluent.processor.mybatis.scanner;

import cn.org.atool.fluent.mybatis.base.BaseEntity;
import cn.org.atool.fluent.mybatis.base.RichEntity;
import cn.org.atool.fluent.processor.AScanner;
import cn.org.atool.fluent.processor.mybatis.entity.FluentEntity;
import lombok.Getter;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.NoType;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import java.util.Objects;

public class FluentScanner2 extends AScanner {
    @Getter
    private final FluentEntity fluent = new FluentEntity();

    public FluentScanner2(Messager messager) {
        super(messager);
    }

    @Override
    public FluentScanner2 scan(TypeElement element) {
        this.fluent.parseEntity(element, err -> this.messager.printMessage(Diagnostic.Kind.ERROR, err));
        while (!this.isBaseEntity(element)) {
            for (Element item : element.getEnclosedElements()) {
                this.visitElement(item);
            }
            TypeMirror typeMirror = element.getSuperclass();
            if (typeMirror instanceof NoType) {
                break;
            }
            element = this.asTypeElement(typeMirror);
        }
        return this;
    }

    private void visitElement(Element item) {
        if (item instanceof VariableElement) {
            this.fluent.visitVariable((VariableElement) item);
        }
        if (item instanceof ExecutableElement) {
            this.fluent.addMethod((ExecutableElement) item);
        }
    }

    private boolean isBaseEntity(TypeElement element) {
        String name = element.toString();
        if (Objects.equals(BaseEntity.class.getName(), name)) {
            return true;
        } else if (Objects.equals(RichEntity.class.getName(), name)) {
            return true;
        } else {
            return Objects.equals(Object.class.getName(), name);
        }
    }
}