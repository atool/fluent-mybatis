package cn.org.atool.fluent.processor.mybatis.scanner;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.base.crud.IDefaultSetter;
import cn.org.atool.fluent.mybatis.base.mapper.IMapper;
import cn.org.atool.fluent.processor.mybatis.entity.FluentEntity;
import cn.org.atool.fluent.processor.mybatis.filer.ClassNames2;
import com.squareup.javapoet.ClassName;
import lombok.Getter;

import javax.annotation.processing.Messager;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementScanner8;
import javax.tools.Diagnostic;
import java.util.function.Supplier;

import static cn.org.atool.fluent.processor.mybatis.scanner.ClassAttrParser.ATTR_DEFAULTS;
import static cn.org.atool.fluent.processor.mybatis.scanner.ClassAttrParser.ATTR_SUPER_MAPPER;

/**
 * FluentScanner 对{@link FluentMybatis}Entity上的注解进行解析
 *
 * @author darui.wu
 */
@SuppressWarnings({"unchecked"})
public class FluentScanner extends ElementScanner8<Void, Void> {
    @Getter
    private final FluentEntity fluent;

    private final Supplier<Messager> messager;

    public FluentScanner(Supplier<Messager> messager) {
        super();
        this.messager = messager;
        this.fluent = new FluentEntity();
    }

    @Override
    public Void visitType(TypeElement entity, Void aVoid) {
        visitEntity(this.fluent, entity, messager.get());
        return super.visitType(entity, aVoid);
    }

    public static void visitEntity(FluentEntity fluent, TypeElement entity, Messager messager) {
        FluentMybatis fluentMybatis = entity.getAnnotation(FluentMybatis.class);
        if (fluentMybatis == null) {
            messager.printMessage(Diagnostic.Kind.ERROR, "Error in: " + entity.getQualifiedName().toString());
        } else {
            ClassName className = ClassNames2.getClassName(entity.getQualifiedName().toString());
            fluent.setClassName(className.packageName(), className.simpleName());
            String defaults = ClassAttrParser.getClassAttr(entity, FluentMybatis.class, ATTR_DEFAULTS, IDefaultSetter.class);
            String superMapper = ClassAttrParser.getClassAttr(entity, FluentMybatis.class, ATTR_SUPER_MAPPER, IMapper.class);
            fluent.setFluentMyBatis(fluentMybatis, defaults, superMapper);
        }
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