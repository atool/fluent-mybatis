package cn.org.atool.fluent.mybatis.spring;

import cn.org.atool.fluent.mybatis.base.mapper.IEntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 所有Mapper实例登记, 需spring bean初始化
 *
 * @author wudarui
 */
@SuppressWarnings({"rawtypes"})
@Component
public class MapperFactory {
    private ApplicationContext applicationContext;

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 返回Mapper实例
     *
     * @param mapperInterface IEntityMapper接口类
     * @param <T>             IEntityMapper类型
     * @return IEntityMapper实例
     */
    public <T extends IEntityMapper> T getBean(Class<T> mapperInterface) {
        return applicationContext.getBean(mapperInterface);
    }
}
