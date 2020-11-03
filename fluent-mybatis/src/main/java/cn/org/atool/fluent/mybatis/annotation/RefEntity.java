package cn.org.atool.fluent.mybatis.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RefEntity {
    String[] value() default {};

    String join() default "";
}