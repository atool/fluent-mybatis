package cn.org.atool.fluent.processor.mybatis.scanner;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.processor.mybatis.entity.FluentEntity;
import lombok.Getter;

import javax.annotation.processing.Messager;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementScanner8;
import javax.tools.Diagnostic;
import java.util.function.Supplier;

/**
 * FluentScanner 对{@link FluentMybatis}Entity上的注解进行解析
 *
 * @author darui.wu
 */
public class FluentScanner extends ElementScanner8<Void, Void> {
    @Getter
    private final FluentEntity fluent;

    private final Supplier<Messager> messager;

    /**
     * 构造函数
     *
     * @param messager Messager Supplier
     */
    public FluentScanner(Supplier<Messager> messager) {
        super();
        this.messager = messager;
        this.fluent = new FluentEntity();
    }

    @Override
    public Void visitType(TypeElement entity, Void aVoid) {
        this.fluent.parseEntity(entity, str -> messager.get().printMessage(Diagnostic.Kind.ERROR, str));
        return super.visitType(entity, aVoid);
    }

    @Override
    public Void visitExecutable(ExecutableElement element, Void aVoid) {
        this.fluent.addMethod(element);
        return super.visitExecutable(element, aVoid);
    }

    @Override
    public Void visitVariable(VariableElement element, Void aVoid) {
        this.fluent.visitVariable(element);
        return super.visitVariable(element, aVoid);
    }
}