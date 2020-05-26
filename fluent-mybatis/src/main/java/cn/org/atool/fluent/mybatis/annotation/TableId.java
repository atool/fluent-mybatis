package cn.org.atool.fluent.mybatis.annotation;

import org.apache.ibatis.type.JdbcType;

import java.lang.annotation.*;
import java.sql.JDBCType;

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

    /**
     * JDBC类型 (该默认值不代表会按照该值生效)
     */
    JdbcType jdbcType() default JdbcType.UNDEFINED;
}