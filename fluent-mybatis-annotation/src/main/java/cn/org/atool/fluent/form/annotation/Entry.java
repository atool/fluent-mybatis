package cn.org.atool.fluent.form.annotation;

import java.lang.annotation.*;

/**
 * 表单项声明
 *
 * @author darui.wu
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
public @interface Entry {
    /**
     * 关联的Entity字段名称
     * 未定义则取字段名称
     *
     * @return value
     */
    String value() default "";

    /**
     * 条件操作, 默认相等(赋值)
     *
     * @return type
     */
    EntryType type() default EntryType.EQ;

    /**
     * 忽略null值
     *
     * @return ignoreNull
     */
    boolean ignoreNull() default true;

    /**
     * 字段描述
     *
     * @return desc
     */
    String desc() default "";
}