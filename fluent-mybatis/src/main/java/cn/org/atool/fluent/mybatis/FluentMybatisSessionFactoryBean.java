package cn.org.atool.fluent.mybatis;

import cn.org.atool.fluent.mybatis.mapper.IMapper;
import cn.org.atool.fluent.mybatis.method.InjectMethod;
import cn.org.atool.fluent.mybatis.method.InjectMethods;
import cn.org.atool.fluent.mybatis.method.model.InjectMapperXml;
import cn.org.atool.fluent.mybatis.method.model.InjectMethodResource;
import cn.org.atool.fluent.mybatis.util.MybatisUtil;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * 继承 {@link SqlSessionFactoryBean} 修改方法 afterPropertiesSet() 加载自定义方法
 *
 * @author darui.wu
 */
@Slf4j
@Accessors(chain = true)
public class FluentMybatisSessionFactoryBean extends SqlSessionFactoryBean {
    @Autowired
    private ListableBeanFactory beanFactory;

    private Resource[] mapperLocations;

    @Setter
    private boolean banner = true;

    @Setter
    private InjectMethods injectMethods = new InjectMethods.DefaultInjectMethods();

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
        this.addInjectMethod();
        super.afterPropertiesSet();
        if (banner) {
            System.out.println(MybatisUtil.getVersionBanner());
        }
    }

    private void addInjectMethod() {
        List<Resource> mappers = new ArrayList<>();
        if (this.mapperLocations != null) {
            Stream.of(this.mapperLocations).forEach(mappers::add);
        }

        String[] mapperBeans = beanFactory.getBeanNamesForType(IMapper.class);
        for (String mapper : mapperBeans) {
            Class type = beanFactory.getType(mapper);
            String xml = InjectMapperXml.buildMapperXml(type, this.fluentMethods());
            Resource resource1 = new InjectMethodResource(type, xml);
            mappers.add(resource1);
        }
        this.setMapperLocations(mappers.toArray(new Resource[0]));
    }

    private List<InjectMethod> fluentMethods() {
        if (this.injectMethods == null) {
            this.injectMethods = new InjectMethods.DefaultInjectMethods();
        }
        return this.injectMethods.methods();
    }
}