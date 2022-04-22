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
     * 操作表Entity表名称
     */
    String table() default "";

    /**
     * 操作的表Entity类
     */
    Class entity() default Object.class;

    /**
     * Service描述
     */
    String desc() default "";

    /**
     * true: 使用代理方式加载bean
     * false: 使用annotation processor方式生成字节码方式
     */
    boolean proxy() default true;
}