package cn.org.atool.fluent.form;

import java.lang.annotation.*;

/**
 * 表单字段
 *
 * @author darui.wu
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FormItem {
    /**
     * 关联字段
     */
    String field() default "";

    /**
     * 条件操作, 默认相等
     */
    ItemType type() default ItemType.EQ;

    /**
     * 忽略null值
     */
    boolean ignoreNull() default true;
}