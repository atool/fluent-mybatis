package cn.org.atool.fluent.mybatis.annotation;

import java.lang.annotation.*;

/**
 * 数据库表相关
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
     * @since 3.1.1
     */
    String schema() default "";
}