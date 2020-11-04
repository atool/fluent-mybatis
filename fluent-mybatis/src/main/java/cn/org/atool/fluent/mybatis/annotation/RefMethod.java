package cn.org.atool.fluent.mybatis.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface RefMethod {
    /**
     * 映射关系
     *
     * @return
     */
    String value();

    /**
     * 是否使用mybatis @One, @Many方式
     *
     * @return
     */
    boolean mybatis() default false;
}