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

    /**
     * 实体映射结果集
     */
    String resultMap() default "";

    /**
     * 是否自动构建 resultMap 并使用
     * 如果设置 resultMap 则不会进行 resultMap 的自动构建并注入
     * 只适合个别字段 设置了 typeHandler 或 jdbcType 的情况
     *
     * @since 3.1.2
     */
    boolean autoResultMap() default false;
}