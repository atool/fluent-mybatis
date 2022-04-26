package cn.org.atool.fluent.processor.mybatis.filer;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Modifier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * FilerKit
 *
 * @author wudarui
 */
@SuppressWarnings("rawtypes")
public interface FilerKit {

    Modifier[] PUBLIC_STATIC_FINAL = {Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL};

    Modifier[] PUBLIC_FINAL = {Modifier.PUBLIC, Modifier.FINAL};

    Modifier[] PRIVATE_STATIC_FINAL = {Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL};

    Modifier[] PRIVATE_STATIC = {Modifier.PRIVATE, Modifier.STATIC};

    Modifier[] PUBLIC_STATIC = {Modifier.PUBLIC, Modifier.STATIC};

    static MethodSpec.Builder staticMethod(String methodName, TypeName returnKlass) {
        return publicMethod(methodName, false, returnKlass)
            .addModifiers(Modifier.STATIC);
    }

    static MethodSpec.Builder staticMethod(String methodName, Class returnKlass) {
        return publicMethod(methodName, false, ClassName.get(returnKlass))
            .addModifiers(Modifier.STATIC);
    }

    /**
     * 定义方式如下的方法
     * <pre>
     * public abstract Xyz methodName(...);
     * </pre>
     *
     * @param methodName name of method
     * @param isOverride 是否注解@Override
     * @return ignore
     */
    static MethodSpec.Builder publicMethod(String methodName, boolean isOverride, TypeName returnKlass) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(methodName);
        if (isOverride) {
            builder.addAnnotation(Override.class);
        }
        if (returnKlass != null) {
            builder.returns(returnKlass);
        }
        builder.addModifiers(Modifier.PUBLIC);
        return builder;
    }

    static MethodSpec.Builder publicMethod(String methodName, Class returnKlass) {
        return publicMethod(methodName, true, returnKlass == null ? null : ClassName.get(returnKlass));
    }

    static MethodSpec.Builder publicMethod(String methodName, TypeName returnKlass) {
        return publicMethod(methodName, true, returnKlass);
    }

    static MethodSpec.Builder protectMethod(String methodName, Class returnKlass) {
        return protectMethod(methodName, ClassName.get(returnKlass));
    }

    static MethodSpec.Builder protectMethod(String methodName, TypeName returnKlass) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(methodName);
        builder.addAnnotation(Override.class);
        if (returnKlass != null) {
            builder.returns(returnKlass);
        }
        builder.addModifiers(Modifier.PROTECTED);
        return builder;
    }

    @SuppressWarnings("all")
    static AnnotationSpec suppressWarnings(String... values) {
        String format = Stream.of(values).map(s -> "$S")
            .collect(Collectors.joining(", ", "{", "}"));
        return AnnotationSpec.builder(SuppressWarnings.class)
            .addMember("value", format, values)
            .build();
    }
}