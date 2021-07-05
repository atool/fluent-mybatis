package cn.org.atool.fluent.mybatis.spring;

import cn.org.atool.fluent.mybatis.base.IRefs;
import cn.org.atool.fluent.mybatis.base.mapper.IEntityMapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 所有Mapper实例登记, 需spring bean初始化
 *
 * @author wudarui
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class MapperFactory implements ApplicationContextAware, InitializingBean {
    private ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.context = context;
    }

    /**
     * 返回Mapper实例
     *
     * @param mapperInterface IEntityMapper接口类
     * @param <T>             IEntityMapper类型
     * @return IEntityMapper实例
     */
    public <T extends IEntityMapper> T getBean(Class<T> mapperInterface) {
        return context.getBean(mapperInterface);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        IRefs refs = IRefs.instance();
        Object relation = this.findEntityRelation();
        refs.setEntityRelation(relation, this);
        refs.wiredMapper();
    }

    /**
     * 从spring容器中获取IEntityRelation bean
     *
     * @return IEntityRelation bean
     * @throws ClassNotFoundException 编译问题
     */
    private Object findEntityRelation() throws ClassNotFoundException {
        try {
            Class klass = Class.forName(IRefs.Fix_Package + ".IEntityRelation");
            return context.getBean(klass);
        } catch (NoSuchBeanDefinitionException be) {
            // do nothing
            return null;
        }
    }
}