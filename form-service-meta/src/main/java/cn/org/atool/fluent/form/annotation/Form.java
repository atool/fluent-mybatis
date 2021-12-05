package cn.org.atool.fluent.form.annotation;

import java.lang.annotation.*;

/**
 * 标识Form Object
 *
 * @author darui.wu
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface Form {
    /**
     * 和前置条件关联方式, true: 以and方式; false: 以or方式
     */
    boolean and() default true;
}