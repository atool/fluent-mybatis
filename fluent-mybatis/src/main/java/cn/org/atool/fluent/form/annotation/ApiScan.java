package cn.org.atool.fluent.form.annotation;

import cn.org.atool.fluent.form.registrar.FormApiRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 扫描api定义路径
 *
 * @author wudarui
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(FormApiRegistrar.class)
public @interface ApiScan {
    String[] value() default {};
}