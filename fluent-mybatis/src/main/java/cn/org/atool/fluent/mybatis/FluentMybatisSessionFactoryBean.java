package cn.org.atool.fluent.mybatis;

import cn.org.atool.fluent.mybatis.test.method.metadata.DbType;
import cn.org.atool.fluent.mybatis.utility.InjectMapper;
import cn.org.atool.fluent.mybatis.utility.MybatisUtil;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;

/**
 * 继承 {@link SqlSessionFactoryBean} 修改方法 afterPropertiesSet() 加载自定义方法
 *
 * @author darui.wu
 */
@Accessors(chain = true)
public class FluentMybatisSessionFactoryBean extends SqlSessionFactoryBean implements ApplicationContextAware {

    private Resource[] mapperLocations;

    @Setter
    private boolean banner = true;

    @Setter
    private DbType dbType;

    @Override
    public void setConfigLocation(Resource configLocation) {
        super.setConfigLocation(configLocation);
    }

    @Override
    public void setMapperLocations(Resource... mapperLocations) {
        super.setMapperLocations(mapperLocations);
        this.mapperLocations = mapperLocations;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();
        this.setMapperLocations(InjectMapper.getMapperResource(this.dbType, beanFactory, this.mapperLocations));
        super.afterPropertiesSet();
        if (banner) {
            System.out.println(MybatisUtil.getVersionBanner());
        }
    }

    private ConfigurableListableBeanFactory getBeanFactory() {
        return (ConfigurableListableBeanFactory) this.applicationContext.getAutowireCapableBeanFactory();
    }

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}