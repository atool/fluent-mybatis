package cn.org.atool.fluent.mybatis.processor.scanner;

import cn.org.atool.fluent.mybatis.annotation.*;
import cn.org.atool.fluent.mybatis.base.crud.IDefaultSetter;
import cn.org.atool.fluent.mybatis.base.mapper.IMapper;
import cn.org.atool.fluent.mybatis.processor.entity.CommonField;
import cn.org.atool.fluent.mybatis.processor.entity.EntityRefMethod;
import cn.org.atool.fluent.mybatis.processor.entity.FluentEntity;
import cn.org.atool.fluent.mybatis.processor.entity.PrimaryField;
import cn.org.atool.fluent.mybatis.processor.filer.ClassNames2;
import com.squareup.javapoet.ClassName;
import lombok.Getter;

import javax.lang.model.element.*;
import javax.lang.model.util.ElementScanner8;
import java.util.Map;
import java.util.function.Consumer;

import static cn.org.atool.fluent.mybatis.processor.scanner.ClassAttrParser.ATTR_DEFAULTS;
import static cn.org.atool.fluent.mybatis.processor.scanner.ClassAttrParser.ATTR_SUPER_MAPPER;

public class FluentScanner extends ElementScanner8<Void, Void> {
    final Consumer<String> logger;

    @Getter
    private FluentEntity fluent;

    public FluentScanner(Consumer<String> logger) {
        super();
        this.logger = logger;
        this.fluent = new FluentEntity();
    }

    @Override
    public Void visitType(TypeElement entity, Void aVoid) {
        ClassName className = ClassNames2.getClassName(entity.getQualifiedName().toString());
        this.fluent.setClassName(className.packageName(), className.simpleName());
        FluentMybatis fluentMybatis = entity.getAnnotation(FluentMybatis.class);
        String defaults = ClassAttrParser.getClassAttr(entity, ATTR_DEFAULTS, IDefaultSetter.class);
        String superMapper = ClassAttrParser.getClassAttr(entity, ATTR_SUPER_MAPPER, IMapper.class);
        this.fluent.setFluentMyBatis(fluentMybatis, defaults, superMapper);
        return super.visitType(entity, aVoid);
    }

    @Override
    public Void visitExecutable(ExecutableElement element, Void aVoid) {
        if (element.getModifiers().contains(Modifier.STATIC) ||
            element.getModifiers().contains(Modifier.ABSTRACT) ||
            !element.getModifiers().contains(Modifier.PUBLIC)) {
            return super.visitExecutable(element, aVoid);
        }
        RefMethod ref = element.getAnnotation(RefMethod.class);
        if (ref == null) {
            return super.visitExecutable(element, aVoid);
        }
        String methodName = element.getSimpleName().toString();
        EntityRefMethod method = new EntityRefMethod(methodName, ClassName.get(element.getReturnType()));
        method.setValue(ref.value());
        this.fluent.addMethod(method);
        return super.visitExecutable(element, aVoid);
    }

    @Override
    public Void visitVariable(VariableElement element, Void aVoid) {
        if (element.getKind() != ElementKind.FIELD ||
            element.getModifiers().contains(Modifier.STATIC) ||
            element.getModifiers().contains(Modifier.TRANSIENT) ||
            element.getAnnotation(NotField.class) != null) {
            return super.visitVariable(element, aVoid);
        }
        TableId tableId = element.getAnnotation(TableId.class);
        String fieldName = element.getSimpleName().toString();

        if (tableId == null) {
            CommonField field = parseCommonField(fieldName, element);
            this.fluent.addField(field);
            Version version = element.getAnnotation(Version.class);
            if (version != null) {
                this.fluent.setVersionField(field.getColumn());
            }
        } else {
            PrimaryField field = this.parsePrimaryField(fieldName, element, tableId);
            this.fluent.addField(field);
        }
        return super.visitVariable(element, aVoid);
    }

    /**
     * 解析普通字段注解信息
     *
     * @param fieldName
     * @param var
     * @return
     */
    private CommonField parseCommonField(String fieldName, VariableElement var) {
        CommonField field = new CommonField(fieldName, ClassName.get(var.asType()));
        TableField tableField = var.getAnnotation(TableField.class);
        if (tableField == null) {
            return field;
        }
        field.setColumn(tableField.value());
        field.setInsert(tableField.insert());
        field.setUpdate(tableField.update());
        field.setNotLarge(tableField.notLarge());
        field.setNumericScale(tableField.numericScale());
        field.setJdbcType(tableField.jdbcType().name());
        for (AnnotationMirror annotationMirror : var.getAnnotationMirrors()) {
            if (!annotationMirror.getAnnotationType().toString().contains(TableField.class.getSimpleName())) {
                continue;
            }
            Map<ExecutableElement, AnnotationValue> values = (Map<ExecutableElement, AnnotationValue>) annotationMirror.getElementValues();
            for (Map.Entry<ExecutableElement, AnnotationValue> entry : values.entrySet()) {
                String method = entry.getKey().getSimpleName().toString();
                AnnotationValue value = entry.getValue();
                if ("typeHandler".equals(method)) {
                    field.setTypeHandler(ClassNames2.getClassName(value.getValue().toString()));
                }
            }
        }
        return field;
    }

    /**
     * 解析主键主机信息
     *
     * @param fieldName
     * @param var
     * @param tableId
     * @return
     */
    private PrimaryField parsePrimaryField(String fieldName, VariableElement var, TableId tableId) {
        PrimaryField field = new PrimaryField(fieldName, ClassName.get(var.asType()));
        field.setColumn(tableId.value());
        field.setAutoIncrease(tableId.auto());
        field.setSeqIsBeforeOrder(tableId.before());
        field.setJdbcType(tableId.jdbcType().name());
        field.setSeqName(tableId.seqName());
        return field;
    }
}