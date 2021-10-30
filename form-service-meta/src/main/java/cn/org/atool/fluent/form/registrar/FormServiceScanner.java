package cn.org.atool.fluent.form.registrar;

import cn.org.atool.fluent.form.annotation.FormService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.Arrays;
import java.util.Set;

/**
 * FormServiceScanner: FormService扫描器
 *
 * @author wudarui
 */
@SuppressWarnings({"rawtypes", "UnusedReturnValue"})
@Slf4j
@Setter
public class FormServiceScanner extends ClassPathBeanDefinitionScanner {

    public FormServiceScanner(BeanDefinitionRegistry registry) {
        super(registry, false);
    }

    /**
     * 注册条件过滤器，将 {@link FormService} 注解加入允许扫描的过滤器中，
     * 如果加入排除扫描的过滤器集合excludeFilter中，则不会扫描
     */
    protected void registerFilters() {
        super.addIncludeFilter(new AnnotationTypeFilter(FormService.class));
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
    }

    /**
     * 重写类扫描包路径加载器，调用父类受保护的扫描方法 doScan
     *
     * @param basePackages 扫描路径
     * @return ignore
     */
    public Set<BeanDefinitionHolder> doScan(Class aopClass, String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
        if (beanDefinitions.isEmpty()) {
            log.warn("No FormService was found in '" + Arrays.toString(basePackages)
                + "' package. Please check your configuration.");
            return beanDefinitions;
        }
        for (BeanDefinitionHolder holder : beanDefinitions) {
            this.processBeanDefinition(aopClass, (AbstractBeanDefinition) holder.getBeanDefinition());
        }
        return beanDefinitions;
    }

    private void processBeanDefinition(Class aopClass, AbstractBeanDefinition definition) {
        String apiInterface = definition.getBeanClassName();
        assert apiInterface != null;
        definition.setBeanClass(FormServiceFactoryBean.class);
        ConstructorArgumentValues constructor = definition.getConstructorArgumentValues();
        constructor.addIndexedArgumentValue(0, apiInterface);
        constructor.addIndexedArgumentValue(1, aopClass);

        definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
        definition.setScope(ConfigurableBeanFactory.SCOPE_SINGLETON);
    }
}