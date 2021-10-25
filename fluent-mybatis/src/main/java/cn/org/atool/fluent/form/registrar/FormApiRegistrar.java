package cn.org.atool.fluent.form.registrar;

import cn.org.atool.fluent.form.annotation.ApiScan;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * FormApiRegistrar: 动态注入FormApi Bean到Spring容器中
 *
 * @author wudarui
 */
public class FormApiRegistrar implements ImportBeanDefinitionRegistrar {

    /**
     * 动态置顶扫描包路径下特殊的类加载到Bean中
     * https://zhuanlan.zhihu.com/p/91461558
     *
     * @param importingClassMetadata AnnotationMetadata
     * @param registry               BeanDefinitionRegistry
     */
    @SuppressWarnings("all")
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes aAttrs = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(ApiScan.class.getName()));

        FormApiScanner scanner = new FormApiScanner(registry);
        scanner.registerFilters();
        String[] basePackages = aAttrs.getStringArray("value");
        scanner.doScan(basePackages);
    }
}