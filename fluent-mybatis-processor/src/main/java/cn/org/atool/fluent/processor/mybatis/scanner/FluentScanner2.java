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
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.NoType;
import javax.lang.model.type.TypeMirror;
import java.util.Objects;

public class FluentScanner2 extends AScanner {
    @Getter
    private final FluentEntity fluent = new FluentEntity();

    public FluentScanner2(Messager messager) {
        super(messager);
    }

    @Override
    public void scan(TypeElement element) {
        this.visitType(element);
        while (!this.isBaseEntity(element)) {
            this.parseEntity(element);
            TypeMirror typeMirror = element.getSuperclass();
            if (typeMirror instanceof NoType) {
                break;
            }
            element = this.asTypeElement(typeMirror);
        }
    }

    private void visitType(TypeElement element) {
        FluentScanner.visitEntity(this.fluent, element, this.messager);
    }

    private void parseEntity(TypeElement element) {
        for (Element item : element.getEnclosedElements()) {
            if (item instanceof VariableElement) {
                FluentScanner.visitVariable(this.fluent, (VariableElement) item);
            }
            if (item instanceof ExecutableElement) {
                this.fluent.addMethod((ExecutableElement) item);
            }
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

    private TypeElement asTypeElement(TypeMirror typeMirror) {
        return (TypeElement) ((DeclaredType) typeMirror).asElement();
    }
}