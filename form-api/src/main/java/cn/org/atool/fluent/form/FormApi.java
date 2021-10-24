package cn.org.atool.fluent.form;

import java.lang.annotation.*;

/**
 * 标识接口是Form Api
 *
 * @author wudarui
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FormApi {
    /**
     * FormProcessor bean name
     *
     * @return
     */
    String processor() default "";
}
