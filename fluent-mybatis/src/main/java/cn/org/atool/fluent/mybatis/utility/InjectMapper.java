package cn.org.atool.fluent.mybatis.utility;

import cn.org.atool.fluent.mybatis.base.IMapper;
import cn.org.atool.fluent.mybatis.base.ISharing;
import cn.org.atool.fluent.mybatis.method.InjectMethod;
import cn.org.atool.fluent.mybatis.method.InjectMethods;
import cn.org.atool.fluent.mybatis.method.metadata.DbType;
import cn.org.atool.fluent.mybatis.method.model.InjectMapperXml;
import cn.org.atool.fluent.mybatis.method.model.InjectMethodResource;
import lombok.Setter;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static cn.org.atool.fluent.mybatis.utility.Predicates.notBlank;


/**
 * SessionFactoryHelper
 *
 * @author darui.wu
 */
public class InjectMapper {
    private ConfigurableListableBeanFactory beanFactory;

    private DbType dbType;

    @Setter
    private InjectMethods injectMethods = new InjectMethods.DefaultInjectMethods();

    public InjectMapper(DbType dbType, ConfigurableListableBeanFactory beanFactory) {
        this.dbType = dbType;
        if (dbType == null) {
            this.dbType = DbType.MYSQL;
        }
        this.beanFactory = beanFactory;
    }

    /**
     * 返回fluent mybatis构造的自动注入Mapper Resource资源
     * 再加上bean中定义好的Resource资源
     *
     * @param dbType          数据库类型
     * @param beanFactory
     * @param mapperLocations
     * @return
     */
    public static Resource[] getMapperResource(DbType dbType, ConfigurableListableBeanFactory beanFactory, Resource[] mapperLocations) {
        InjectMapper injectMapper = new InjectMapper(dbType, beanFactory);
        List<Resource> resources = injectMapper.addInjectMethod();
        if (mapperLocations != null) {
            Stream.of(mapperLocations).forEach(resources::add);
        }
        return resources.toArray(new Resource[0]);
    }

    public List<Resource> addInjectMethod() {
        List<Resource> resources = new ArrayList<>();
        List<Class> mapperKlass = this.findMapperClass();
        for (Class klass : mapperKlass) {
            boolean isSharing = ISharing.class.isAssignableFrom(klass);
            String xml = InjectMapperXml.buildMapperXml(klass, this.fluentMethods(isSharing, this.dbType));
            if (notBlank(xml)) {
                Resource resource = new InjectMethodResource(klass, xml);
                resources.add(resource);
            }
        }
        return resources;
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