package cn.org.atool.fluent.mybatis.generator.annoatation;

import cn.org.atool.fluent.mybatis.annotation.ParaType;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Interface {
    Class value();

    ParaType[] types() default {ParaType.Entity};
}
