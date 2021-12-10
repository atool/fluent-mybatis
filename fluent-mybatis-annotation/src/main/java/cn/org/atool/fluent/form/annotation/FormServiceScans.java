package cn.org.atool.fluent.form.annotation;

import cn.org.atool.fluent.form.registrar.FormServiceRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(FormServiceRegistrar.RepeatingRegistrar.class)
public @interface FormServiceScans {
    FormServiceScan[] value();
}
