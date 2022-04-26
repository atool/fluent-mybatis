package cn.org.atool.fluent.processor.mybatis.scanner;

import cn.org.atool.fluent.mybatis.annotation.*;
import cn.org.atool.fluent.mybatis.base.crud.IDefaultSetter;
import cn.org.atool.fluent.mybatis.base.mapper.IMapper;
import cn.org.atool.fluent.processor.mybatis.entity.CommonField;
import cn.org.atool.fluent.processor.mybatis.entity.EntityRefMethod;
import cn.org.atool.fluent.processor.mybatis.entity.FluentEntity;
import cn.org.atool.fluent.processor.mybatis.entity.PrimaryField;
import cn.org.atool.fluent.processor.mybatis.filer.ClassNames2;
import cn.org.atool.generator.database.model.FieldType;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import lombok.Getter;

import javax.annotation.processing.Messager;
import javax.lang.model.element.*;
import javax.lang.model.util.ElementScanner8;
import javax.tools.Diagnostic;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import static cn.org.atool.fluent.processor.mybatis.filer.ClassNames2.CN_Long;
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
        visitMethod(this.fluent, element);
        return super.visitExecutable(element, aVoid);
    }

    public static void visitMethod(FluentEntity fluent, ExecutableElement element) {
        if (element.getModifiers().contains(Modifier.STATIC) ||
            element.getModifiers().contains(Modifier.ABSTRACT) ||
            !element.getModifiers().contains(Modifier.PUBLIC)) {
            return;
        }
        RefMethod ref = element.getAnnotation(RefMethod.class);
        if (ref == null) {
            return;
        }
        String methodName = element.getSimpleName().toString();
        EntityRefMethod method = new EntityRefMethod(methodName, ClassName.get(element.getReturnType()));
        method.setValue(ref.value());
        fluent.addMethod(method);
    }

    @Override
    public Void visitVariable(VariableElement element, Void aVoid) {
        visitVariable(this.fluent, element);
        return super.visitVariable(element, aVoid);
    }

    public static void visitVariable(FluentEntity fluent, VariableElement element) {
        if (element.getKind() != ElementKind.FIELD ||
            element.getModifiers().contains(Modifier.STATIC) ||
            element.getModifiers().contains(Modifier.TRANSIENT) ||
            element.getAnnotation(NotField.class) != null) {
            return;
        }
        TableId tableId = element.getAnnotation(TableId.class);
        String fieldName = element.getSimpleName().toString();

        if (tableId == null) {
            CommonField field = parseCommonField(fieldName, element);
            fluent.addField(field);
            setFieldType(fluent, field, element);
        } else {
            PrimaryField field = parsePrimaryField(fieldName, element, tableId);
            field.setType(field.isAutoIncrease() ? FieldType.PrimaryId : FieldType.PrimaryKey);
            fluent.addField(field);
        }
    }

    private static void setFieldType(FluentEntity fluent, CommonField field, VariableElement element) {
        if (element.getAnnotation(LogicDelete.class) != null) {
            fluent.setLogicDelete(field.getName());
            fluent.setLongTypeOfLogicDelete(Objects.equals(field.getJavaType(), CN_Long));
            field.setType(FieldType.IsDeleted);
        }
        if (element.getAnnotation(Version.class) != null) {
            fluent.setVersionField(field.getName());
            field.setType(FieldType.Version);
        }
        if (element.getAnnotation(GmtCreate.class) != null) {
            field.setType(FieldType.GmtCreate);
        }
        if (element.getAnnotation(GmtModified.class) != null) {
            field.setType(FieldType.GmtModified);
        }
    }

    /**
     * 解析普通字段注解信息
     *
     * @param fieldName name of field
     * @param var       var
     * @return ignore
     */
    public static CommonField parseCommonField(String fieldName, VariableElement var) {
        CommonField field = new CommonField(fieldName, getJavaType(var));
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
        field.setTypeHandler(getTypeHandler(var, TableField.class.getSimpleName()));

        return field;
    }

    private static TypeName getJavaType(VariableElement var) {
        TypeName type = ClassName.get(var.asType());
        if (type instanceof ParameterizedTypeName) {
            return ((ParameterizedTypeName) type).rawType;
        } else {
            return type;
        }
    }

    /**
     * 解析主键主机信息
     *
     * @param fieldName name of field
     * @param var       var
     * @param tableId   Annotation
     * @return ignore
     */
    private static PrimaryField parsePrimaryField(String fieldName, VariableElement var, TableId tableId) {
        PrimaryField field = new PrimaryField(fieldName, getJavaType(var));
        field.setColumn(tableId.value());
        field.setAutoIncrease(tableId.auto());
        field.setSeqIsBeforeOrder(tableId.before());
        field.setJdbcType(tableId.jdbcType().name());
        field.setTypeHandler(getTypeHandler(var, TableId.class.getSimpleName()));
        field.setSeqName(tableId.seqName());
        return field;
    }

    /**
     * 获取 {@link TableId}和{@link TableField}上的typeHandler属性
     *
     * @param var   注解实例
     * @param aName "TableId" or "TableField"
     * @return TypeName
     */
    private static TypeName getTypeHandler(VariableElement var, String aName) {
        for (AnnotationMirror annotationMirror : var.getAnnotationMirrors()) {
            String aTypeName = annotationMirror.getAnnotationType().toString();
            if (!aTypeName.contains(aName)) {
                continue;
            }
            Map<ExecutableElement, AnnotationValue> values = (Map<ExecutableElement, AnnotationValue>) annotationMirror.getElementValues();
            for (Map.Entry<ExecutableElement, AnnotationValue> entry : values.entrySet()) {
                String method = entry.getKey().getSimpleName().toString();
                AnnotationValue value = entry.getValue();
                if ("typeHandler".equals(method)) {
                    return ClassNames2.getClassName(value.getValue().toString());
                }
            }
        }
        return null;
    }
}