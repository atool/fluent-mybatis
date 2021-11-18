package cn.org.atool.fluent.form.annotation;

import cn.org.atool.fluent.form.IMethodAround;
import cn.org.atool.fluent.form.kits.NoMethodAround;
import cn.org.atool.fluent.form.registrar.FormServiceRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 扫描api定义路径
 *
 * @author wudarui
 */
@SuppressWarnings("unused")
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
     * 切面处理
     */
    Class<? extends IMethodAround> around() default NoMethodAround.class;
}