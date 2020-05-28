package cn.org.atool.fluent.mybatis.annotation;

import org.apache.ibatis.type.JdbcType;

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
     * 是否自增主键
     */
    boolean auto() default true;

    /**
     * JDBC类型 (该默认值不代表会按照该值生效)
     */
    JdbcType jdbcType() default JdbcType.UNDEFINED;

    /**
     * 自增主键产生的sequence name
     *
     * @return
     */
    String seqName() default "";
}