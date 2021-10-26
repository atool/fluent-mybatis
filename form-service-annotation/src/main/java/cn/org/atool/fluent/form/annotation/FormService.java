package cn.org.atool.fluent.form.annotation;

import java.lang.annotation.*;

/**
 * 标识接口是Form Service Api
 *
 * @author wudarui
 */
@SuppressWarnings("rawtypes")
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FormService {
    /**
     * 操作的表Entity
     */
    Class entityClass() default Object.class;

    /**
     * 操作表Entity表名称
     */
    String entityTable() default "";
}