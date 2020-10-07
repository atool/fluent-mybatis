package cn.org.atool.fluent.mybatis.test;

import cn.org.atool.fluent.mybatis.generate.datamap.ITable;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.test4j.junit5.Test4J;

import javax.sql.DataSource;

@ContextConfiguration(classes = {
    TestSpringConfig.class
})
public abstract class BaseTest extends Test4J implements ITable {
}

@Configuration
@ComponentScan(basePackages = {
    "cn.org.atool.fluent.mybatis.generate.dao.impl",
    "cn.org.atool.fluent.mybatis.customize.impl"
})
@MapperScan({"cn.org.atool.fluent.mybatis.generate.entity.mapper",
    "cn.org.atool.fluent.mybatis.customize.mapper"
})
class TestSpringConfig {
    @Bean("dataSource")
    public DataSource newDataSource() {
        return null;
    }

    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean() throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(newDataSource());
        bean.setMapperLocations(new ClassPathResource("mapper/MyXmlMapper.xml"));
        return bean;
    }
}