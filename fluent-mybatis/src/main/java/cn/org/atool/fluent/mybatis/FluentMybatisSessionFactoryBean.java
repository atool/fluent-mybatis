package cn.org.atool.fluent.mybatis;

import cn.org.atool.fluent.mybatis.base.IMapper;
import cn.org.atool.fluent.mybatis.base.ISharing;
import cn.org.atool.fluent.mybatis.method.InjectMethod;
import cn.org.atool.fluent.mybatis.method.InjectMethods;
import cn.org.atool.fluent.mybatis.method.metadata.DbType;
import cn.org.atool.fluent.mybatis.method.model.InjectMapperXml;
import cn.org.atool.fluent.mybatis.method.model.InjectMethodResource;
import cn.org.atool.fluent.mybatis.utility.MybatisUtil;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * 继承 {@link SqlSessionFactoryBean} 修改方法 afterPropertiesSet() 加载自定义方法
 *
 * @author darui.wu
 */
@Accessors(chain = true)
public class FluentMybatisSessionFactoryBean extends SqlSessionFactoryBean {
    @Autowired
    private ConfigurableListableBeanFactory beanFactory;

    private Resource[] mapperLocations;

    @Setter
    private boolean banner = true;

    @Setter
    private InjectMethods injectMethods = new InjectMethods.DefaultInjectMethods();

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
        if (dbType == null) {
            dbType = DbType.MYSQL;
        }
        List<Class> mapperKlass = this.findMapperClass();
        for (Class klass : mapperKlass) {
            boolean isSharing = ISharing.class.isAssignableFrom(klass);
            String xml = InjectMapperXml.buildMapperXml(klass, this.fluentMethods(isSharing, this.dbType));
            if (xml == null) {
                continue;
            }
            Resource resource = new InjectMethodResource(klass, xml);
            mappers.add(resource);
        }
        this.setMapperLocations(mappers.toArray(new Resource[0]));
    }

    private List<Class> findMapperClass() {
        List<Class> classes = new ArrayList<>();
        String[] beans = beanFactory.getBeanDefinitionNames();
        for (String bean : beans) {
            BeanDefinition bd = beanFactory.getBeanDefinition(bean);
            if (!(bd instanceof AnnotatedBeanDefinition)) {
                continue;
            }
            AnnotationMetadata metadata = ((AnnotatedBeanDefinition) bd).getMetadata();
            Set<String> annotations = metadata.getAnnotationTypes();
            if (!annotations.contains(Mapper.class.getName())) {
                continue;
            }
            Class klass = this.findMapperClass(metadata.getClassName());
            if (IMapper.class.isAssignableFrom(klass)) {
                classes.add(klass);
            }
        }
        return classes;
    }

    private Class findMapperClass(String mapperKlassName) {
        try {
            return ClassUtils.forName(mapperKlassName, ClassUtils.getDefaultClassLoader());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 返回方法列表
     *
     * @param isSharing 是否分表分库方法列表
     * @param dbType    数据库类型
     * @return 返回方法列表
     */
    private List<InjectMethod> fluentMethods(boolean isSharing, DbType dbType) {
        if (this.injectMethods == null) {
            this.injectMethods = new InjectMethods.DefaultInjectMethods();
        }
        return isSharing ? this.injectMethods.sharingMethods(dbType) : this.injectMethods.methods(dbType);
    }
}