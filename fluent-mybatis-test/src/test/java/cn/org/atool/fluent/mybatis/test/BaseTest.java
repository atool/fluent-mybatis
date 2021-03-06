package cn.org.atool.fluent.mybatis.test;

import cn.org.atool.fluent.mybatis.spring.MapperFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.test.context.ContextConfiguration;
import org.test4j.junit5.Test4J;
import org.test4j.module.database.proxy.DataSourceCreator;

import javax.sql.DataSource;

@ContextConfiguration(classes = {
    TestSpringConfig.class
})
public abstract class BaseTest extends Test4J {
}

@Configuration
@ComponentScan(basePackages = {
    "cn.org.atool.fluent.mybatis.generate.dao.impl",
    "cn.org.atool.fluent.mybatis.customize.impl"
})
@MapperScan({"cn.org.atool.fluent.mybatis.generate.mapper",
    "cn.org.atool.fluent.mybatis.customize.mapper",
    "cn.org.atool.fluent.mybatis.origin.mapper"
})
class TestSpringConfig {
    @Bean("dataSource")
    public DataSource newDataSource() {
        return DataSourceCreator.create("dataSource");
    }

    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean() throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(newDataSource());
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        // 路径正则表达式方式加载
        bean.setMapperLocations(resolver.getResources("classpath*:mapper/*.xml"));
        // 一个个文件加载
        // bean.setMapperLocations(
        //      new ClassPathResource("mapper/MyXmlMapper.xml"),
        //      new ClassPathResource("mapper/BatchUpdate.xml")
        // );
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setLazyLoadingEnabled(true);
        configuration.setMapUnderscoreToCamelCase(true);
        bean.setConfiguration(configuration);
        return bean;
    }

    @Bean
    public MapperFactory mapperFactory() {
        return new MapperFactory();
    }
}