package cn.org.atool.fluent.form.registrar;

import cn.org.atool.fluent.form.annotation.FormServiceScan;
import cn.org.atool.fluent.form.annotation.FormServiceScans;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * FormServiceRegistrar: 动态注入FormService Bean到Spring容器中
 *
 * @author wudarui
 */
@SuppressWarnings("rawtypes")
public class FormServiceRegistrar implements ImportBeanDefinitionRegistrar {

    /**
     * 动态置顶扫描包路径下特殊的类加载到Bean中
     *
     * @param importingClassMetadata AnnotationMetadata
     * @param registry               BeanDefinitionRegistry
     */
    @SuppressWarnings("all")
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes aAttrs = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(FormServiceScan.class.getName()));
        this.registerBean(registry, aAttrs);
    }

    private static void registerBean(BeanDefinitionRegistry registry, AnnotationAttributes aAttrs) {
        FormServiceScanner scanner = new FormServiceScanner(registry);
        scanner.registerFilters();
        String[] basePackages = aAttrs.getStringArray("value");
        Class aClass = aAttrs.getClass("aop");
        scanner.doScan(aClass, basePackages);
    }

    public static class RepeatingRegistrar extends FormServiceRegistrar {
        @Override
        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
            AnnotationAttributes attributes = AnnotationAttributes
                .fromMap(importingClassMetadata.getAnnotationAttributes(FormServiceScans.class.getName()));
            if (attributes == null) {
                return;
            }
            AnnotationAttributes[] annotations = attributes.getAnnotationArray("value");
            for (AnnotationAttributes aAttrs : annotations) {
                registerBean(registry, aAttrs);
            }
        }
    }
}