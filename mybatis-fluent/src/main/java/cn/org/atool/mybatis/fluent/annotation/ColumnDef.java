package cn.org.atool.mybatis.fluent.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ColumnDef {
    /**
     * 数据库字段类型
     *
     * @return
     */
    String type();

    /**
     * 是否主键
     *
     * @return
     */
    boolean primary() default false;

    /**
     * 允许字段为null
     *
     * @return
     */
    boolean notNull() default false;
}
