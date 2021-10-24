package cn.org.atool.fluent.mybatis.form.registrar;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;

/**
 * FormApiRegistrar: 动态注入FormApi Bean到Spring容器中
 *
 * @author wudarui
 */
public class FormApiRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {
    private ResourceLoader resourceLoader;

    /**
     * 动态置顶扫描包路径下特殊的类加载到Bean中
     * https://zhuanlan.zhihu.com/p/91461558
     *
     * @param importingClassMetadata
     * @param registry
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        FormApiScanner scanner = new FormApiScanner(registry, false);
        scanner.setResourceLoader(resourceLoader);
        scanner.registerFilters();
        scanner.doScan("com.example.bean.testScan");
    }

    /**
     * 获取Spring中的元数据
     *
     * @param resourceLoader
     */
    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
