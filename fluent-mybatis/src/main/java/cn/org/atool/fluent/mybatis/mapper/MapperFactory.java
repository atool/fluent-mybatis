package cn.org.atool.fluent.mybatis.mapper;

import cn.org.atool.fluent.mybatis.base.mapper.IEntityMapper;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 所有Mapper实例登记, 需spring bean初始化
 *
 * @author wudarui
 */
public class MapperFactory implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 返回Mapper实例
     *
     * @param mapperInterface
     * @param <T>
     * @return
     */
    public <T extends IEntityMapper> T getBean(Class<T> mapperInterface) {
        return applicationContext.getBean(mapperInterface);
    }
}
