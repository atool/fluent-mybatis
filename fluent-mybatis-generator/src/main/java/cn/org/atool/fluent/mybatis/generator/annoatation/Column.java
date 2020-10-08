package cn.org.atool.fluent.mybatis.generator.annoatation;

import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.UnknownTypeHandler;
import org.test4j.generator.mybatis.db.IFieldCategory;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Column {
    /**
     * 数据库字段名称
     *
     * @return
     */
    String value();

    /**
     * 指定Entity属性名称, 默认下划线转驼峰命名
     *
     * @return
     */
    String property() default "";

    /**
     * insert的默认值
     *
     * @return
     */
    String insert() default "";

    /**
     * update的默认值
     *
     * @return
     */
    String update() default "";

    /**
     * 是否大字段
     *
     * @return
     */
    boolean isLarge() default false;

    /**
     * 显式指定字段对应的java类型
     *
     * @return
     */
    Class javaType() default Object.class;

    /**
     * type handler
     *
     * @return
     */
    Class<? extends TypeHandler> typeHandler() default UnknownTypeHandler.class;

    /**
     * 指定字段类型
     *
     * @return
     */
    IFieldCategory category() default IFieldCategory.Common;
}
