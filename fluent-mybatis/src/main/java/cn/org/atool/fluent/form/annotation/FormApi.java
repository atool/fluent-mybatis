package cn.org.atool.fluent.form.annotation;

import cn.org.atool.fluent.mybatis.base.IEntity;

import java.lang.annotation.*;

/**
 * 标识接口是Form Api
 *
 * @author wudarui
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FormApi {
    /**
     * 操作的表Entity
     */
    Class<? extends IEntity> entity() default IEntity.class;
}