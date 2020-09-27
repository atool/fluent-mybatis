package cn.org.atool.fluent.mybatis.annotation;

import java.lang.annotation.*;

/**
 * 显式指定表名
 *
 * @author darui.wu
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TableName {

    /**
     * 实体对应的表名
     */
    String value();

    /**
     * schema
     *
     * @return
     */
    String schema() default "";
}