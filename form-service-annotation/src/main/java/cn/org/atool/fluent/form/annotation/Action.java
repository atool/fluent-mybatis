package cn.org.atool.fluent.form.annotation;

import java.lang.annotation.*;

/**
 * Form Api接口方法操作类型声明
 *
 * @author wudarui
 */
@SuppressWarnings({"unused", "rawtypes"})
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.PARAMETER})
public @interface Action {
    /**
     * 操作的表Entity
     */
    Class entityClass() default Object.class;

    /**
     * 操作表Entity表名称
     */
    String entityTable() default "";

    /**
     * 操作方法, Save操作时需要显式指定
     */
    ActionType type() default ActionType.Auto;
}