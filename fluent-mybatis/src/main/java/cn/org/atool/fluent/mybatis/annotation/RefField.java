package cn.org.atool.fluent.mybatis.annotation;

import java.lang.annotation.*;

/**
 * 关联关系字段
 *
 * @author darui.wu
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface RefField {
    /**
     * 自动映射关联关系, 框架生成代码
     *
     * @return
     */
    String[] value() default {};
}