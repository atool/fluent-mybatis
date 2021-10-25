package cn.org.atool.fluent.form.annotation;

import cn.org.atool.fluent.mybatis.base.IEntity;

import java.lang.annotation.*;

/**
 * Form Api接口方法声明
 *
 * @author wudarui
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ApiMethod {
    /**
     * 操作的表名称
     */
    Class<? extends IEntity> entity();

    /**
     * 操作方法
     */
    MethodType type() default MethodType.ListEntity;
}