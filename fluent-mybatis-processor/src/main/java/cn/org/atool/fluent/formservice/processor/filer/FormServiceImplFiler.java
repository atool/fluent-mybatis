package cn.org.atool.fluent.formservice.processor.filer;

import cn.org.atool.fluent.form.meta.MethodMeta;
import cn.org.atool.fluent.form.registrar.FormServiceKit;
import com.squareup.javapoet.*;

import javax.lang.model.element.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.org.atool.fluent.form.meta.ClassKit.FormServiceBeanSuffix;

public class FormServiceImplFiler {
    private final TypeElement element;
    private final ClassName className;
    private final List<ExecutableElement> methods;

    public FormServiceImplFiler(TypeElement element, List<ExecutableElement> methods) {
        this.element = element;
        this.className = ClassName.get(element);
        this.methods = methods;
    }

    public JavaFile javaFile() {
        ClassName metaKit = ClassName.get(this.className.packageName(), this.className.simpleName() + FormServiceBeanSuffix);
        TypeSpec.Builder type = TypeSpec.classBuilder(metaKit).addModifiers(Modifier.PUBLIC)
            .addJavadoc("$T\n@author powered by FluentMybatis", metaKit)
            .addAnnotation(AnnotationSpec.builder(SuppressWarnings.class).addMember("value", "{$S}", "unused").build());
        if (element.getKind().isInterface()) {
            type.addSuperinterface(this.className);
        } else {
            type.superclass(this.className);
        }
        for (ExecutableElement method : methods) {
            type.addMethod(this.m_implements(method));
        }

        return JavaFile.builder(this.className.packageName(), type.build())
            .skipJavaLangImports(true)
            .build();
    }

    private MethodSpec m_implements(ExecutableElement method) {
        String methodName = method.getSimpleName().toString();
        MethodSpec.Builder spec = MethodSpec.methodBuilder(methodName)
            .addModifiers(Modifier.PUBLIC);
        spec.returns(ClassName.get(method.getReturnType()));
        for (AnnotationMirror annotationMirror : method.getAnnotationMirrors()) {
            this.addAnnotations(spec, annotationMirror);
        }
        List<String> pNames = new ArrayList<>();
        List<String> pTypes = new ArrayList<>();
        for (VariableElement para : method.getParameters()) {
            String pName = para.getSimpleName().toString();
            TypeName pType = ClassName.get(para.asType());
            spec.addParameter(pType, pName);
            pNames.add(pName);
            pTypes.add(typeName(pType) + ".class");
        }
        spec.addStatement("$T meta = $T.meta($T.class, $S$L)", MethodMeta.class, FormServiceKit.class, className,
                methodName, pTypes.stream().collect(Collectors.joining(", ", ", ", "")))
            .addStatement("return $T.invoke(meta, new Object[]{$L})", FormServiceKit.class, String.join(", ", pNames));
        return spec.build();
    }

    /**
     * copy 接口方法上定义的注解
     *
     * @param mBuilder         MethodSpec.Builder
     * @param annotationMirror 方法注解
     */
    private void addAnnotations(MethodSpec.Builder mBuilder, AnnotationMirror annotationMirror) {
        TypeName typeName = ClassName.get(annotationMirror.getAnnotationType());
        AnnotationSpec.Builder spec = AnnotationSpec.builder((ClassName) typeName);
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : annotationMirror.getElementValues().entrySet()) {
            String key = entry.getKey().getSimpleName().toString();
            spec.addMember(key, "$L", entry.getValue().toString());
        }
        mBuilder.addAnnotation(spec.build());
    }

    private String typeName(TypeName type) {
        if (type instanceof ParameterizedTypeName) {
            return ((ParameterizedTypeName) type).rawType.simpleName();
        } else if (type instanceof ClassName) {
            return ((ClassName) type).simpleName();
        } else if (type instanceof ArrayTypeName) {
            TypeName cType = ((ArrayTypeName) type).componentType;
            return this.typeName(cType) + "[]";
        } else {
            return type.toString();
        }
    }
}