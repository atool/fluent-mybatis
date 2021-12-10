package cn.org.atool.fluent.form.annotation;

import cn.org.atool.fluent.form.registrar.FormServiceRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 扫描api定义路径
 *
 * @author wudarui
 */
@SuppressWarnings({"unused", "rawtypes"})
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(FormServiceRegistrar.class)
@Repeatable(FormServiceScans.class)
public @interface FormServiceScan {
    /**
     * api接口定义路径列表
     */
    String[] value() default {};

    /**
     * 切面处理, 继承 IMethodAround 接口类
     */
    Class around() default Object.class;
}