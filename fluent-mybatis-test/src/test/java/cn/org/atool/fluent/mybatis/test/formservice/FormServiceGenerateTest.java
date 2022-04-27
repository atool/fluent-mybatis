package cn.org.atool.fluent.mybatis.test.formservice;

import cn.org.atool.fluent.mybatis.formservice.service.StudentServiceFormService;
import org.apache.ibatis.annotations.ConstructorArgs;
import org.apache.ibatis.annotations.Result;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.transaction.annotation.Transactional;
import org.test4j.integration.DataProvider;
import org.test4j.junit5.Test4J;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;

class FormServiceGenerateTest extends Test4J {
    @ParameterizedTest
    @MethodSource("annotationOfMethod")
    void test(Class<? extends Annotation> klass, String method) {
        String result = Arrays.stream(StudentServiceFormService.class.getMethods())
            .filter(m -> m.getAnnotation(klass) != null)
            .map(Method::getName)
            .findFirst().orElse(null);
        want.string(result).eq(method);
    }

    public static DataProvider annotationOfMethod() {
        return new DataProvider()
            .data(Transactional.class, "saveStudent")
            .data(ConstructorArgs.class, "updateStudent")
            .data(Result.class, "deleteById");
    }
}
