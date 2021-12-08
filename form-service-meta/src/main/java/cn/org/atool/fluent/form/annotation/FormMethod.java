package cn.org.atool.fluent.form.annotation;

import java.lang.annotation.*;

/**
 * Form Service接口方法操作声明
 *
 * @author wudarui
 */
@SuppressWarnings({"unused", "rawtypes"})
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface FormMethod {
    /**
     * 操作方法, Save操作时需要显式指定
     */
    MethodType type() default MethodType.Auto;

    /**
     * 操作表Entity表名称
     */
    String table() default "";

    /**
     * 操作的表Entity
     */
    Class entity() default Object.class;
}