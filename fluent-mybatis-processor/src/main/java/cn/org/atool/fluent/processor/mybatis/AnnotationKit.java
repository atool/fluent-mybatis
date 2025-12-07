package cn.org.atool.fluent.processor.mybatis;

import com.palantir.javapoet.ClassName;

public class AnnotationKit {
    public static ClassName getPostConstructClass() {
        if (FluentMybatisProcessor.useJakartaAnnotation) {
            return ClassName.get("jakarta.annotation", "PostConstruct");
        } else {
            return ClassName.get("javax.annotation", "PostConstruct");
        }
    }

    public static ClassName getResourceClass() {
        if (FluentMybatisProcessor.useJakartaAnnotation) {
            return ClassName.get("jakarta.annotation", "Resource");
        } else {
            return ClassName.get("javax.annotation", "Resource");
        }
    }
}
