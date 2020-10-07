package cn.org.atool.fluent.mybatis.generator.annoatation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Table {
    /**
     * 表名称
     *
     * @return
     */
    String value();

    /**
     * 排除字段列表
     *
     * @return
     */
    String[] excludes() default {};

    /**
     * 显式指定字段转换属性
     *
     * @return
     */
    Column[] columns() default {};
}
