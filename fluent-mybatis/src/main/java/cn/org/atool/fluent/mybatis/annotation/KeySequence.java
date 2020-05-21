package cn.org.atool.fluent.mybatis.annotation;

import java.lang.annotation.*;

/**
 * 序列主键策略
 *
 * @author darui.wu
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface KeySequence {

    /**
     * 序列名
     */
    String value() default "";

    /**
     * id的类型
     */
    Class<?> clazz() default Long.class;
}