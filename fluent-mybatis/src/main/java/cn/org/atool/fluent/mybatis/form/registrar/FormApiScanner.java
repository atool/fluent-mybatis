package cn.org.atool.fluent.mybatis.form.registrar;

import cn.org.atool.fluent.form.FormApi;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.Set;

/**
 * FormApiScanner: FormApi扫描器
 *
 * @author wudarui
 */
public class FormApiScanner extends ClassPathBeanDefinitionScanner {
    public FormApiScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters) {
        super(registry, useDefaultFilters);
    }

    /**
     * 注册条件过滤器，将 @FormApi 注解加入允许扫描的过滤器中，
     * 如果加入排除扫描的过滤器集合excludeFilter中，则不会扫描
     */
    protected void registerFilters() {
        super.addIncludeFilter(new AnnotationTypeFilter(FormApi.class));
    }

    /**
     * 重写类扫描包路径加载器，调用父类受保护的扫描方法 doScan
     *
     * @param basePackages
     * @return
     */
    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        return super.doScan(basePackages);
    }
}
