package cn.org.atool.fluent.form.annotation;

import java.lang.annotation.*;

/**
 * 表单字段
 *
 * @author darui.wu
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Entry {
    /**
     * 关联字段
     */
    String value() default "";

    /**
     * 条件操作, 默认相等
     */
    EntryType type() default EntryType.EQ;

    /**
     * 忽略null值
     */
    boolean ignoreNull() default true;
}