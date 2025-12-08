package cn.org.atool.fluent.processor.mybatis.filer;

import com.palantir.javapoet.AnnotationSpec;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.TypeName;

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

    /**
     * PUBLIC STATIC FINAL
     */
    Modifier[] PUBLIC_STATIC_FINAL = { Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL };

    /**
     * PUBLIC FINAL
     */
    Modifier[] PUBLIC_FINAL = { Modifier.PUBLIC, Modifier.FINAL };

    /**
     * PRIVATE STATIC FINAL
     */
    Modifier[] PRIVATE_STATIC_FINAL = { Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL };

    /**
     * PRIVATE STATIC
     */
    Modifier[] PRIVATE_STATIC = { Modifier.PRIVATE, Modifier.STATIC };

    /**
     * PUBLIC STATIC
     */
    Modifier[] PUBLIC_STATIC = { Modifier.PUBLIC, Modifier.STATIC };

    /**
     * static method
     *
     * @param methodName  method name
     * @param returnKlass return type
     * @return MethodSpec.Builder
     */
    static MethodSpec.Builder staticMethod(String methodName, TypeName returnKlass) {
        return publicMethod(methodName, false, returnKlass)
                .addModifiers(Modifier.STATIC);
    }

    /**
     * static method
     *
     * @param methodName  method name
     * @param returnKlass return type
     * @return MethodSpec.Builder
     */
    static MethodSpec.Builder staticMethod(String methodName, Class returnKlass) {
        return publicMethod(methodName, false, ClassName.get(returnKlass))
                .addModifiers(Modifier.STATIC);
    }

    /**
     * 定义方式如下的方法
     * 
     * <pre>
     * public abstract Xyz methodName(...);
     * </pre>
     *
     * @param methodName  name of method
     * @param isOverride  是否注解@Override
     * @param returnKlass return type
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

    /**
     * public method
     *
     * @param methodName  name of method
     * @param returnKlass return type
     * @return MethodSpec.Builder
     */
    static MethodSpec.Builder publicMethod(String methodName, Class returnKlass) {
        return publicMethod(methodName, true, returnKlass == null ? null : ClassName.get(returnKlass));
    }

    /**
     * public method
     *
     * @param methodName  name of method
     * @param returnKlass return type
     * @return MethodSpec.Builder
     */
    static MethodSpec.Builder publicMethod(String methodName, TypeName returnKlass) {
        return publicMethod(methodName, true, returnKlass);
    }

    /**
     * protected method
     *
     * @param methodName  name of method
     * @param returnKlass return type
     * @return MethodSpec.Builder
     */
    static MethodSpec.Builder protectMethod(String methodName, Class returnKlass) {
        return protectMethod(methodName, ClassName.get(returnKlass));
    }

    /**
     * protected method
     *
     * @param methodName  name of method
     * @param returnKlass return type
     * @return MethodSpec.Builder
     */
    static MethodSpec.Builder protectMethod(String methodName, TypeName returnKlass) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(methodName);
        builder.addAnnotation(Override.class);
        if (returnKlass != null) {
            builder.returns(returnKlass);
        }
        builder.addModifiers(Modifier.PROTECTED);
        return builder;
    }

    /**
     * suppress warnings
     *
     * @param values warnings
     * @return AnnotationSpec
     */
    @SuppressWarnings("all")
    static AnnotationSpec suppressWarnings(String... values) {
        String format = Stream.of(values).map(s -> "$S")
                .collect(Collectors.joining(", ", "{", "}"));
        return AnnotationSpec.builder(SuppressWarnings.class)
                .addMember("value", format, values)
                .build();
    }
}