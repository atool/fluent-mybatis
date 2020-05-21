package cn.org.atool.fluent.mybatis.annotation;

import java.lang.annotation.*;

/**
 * 表主键标识
 *
 * @author darui.wu
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface TableId {

    /**
     * 字段值
     */
    String value();

    /**
     * 主键ID
     * {@link IdType}
     */
    IdType type() default IdType.NONE;
}