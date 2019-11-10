package cn.org.atool.fluent.mybatis.test;

import cn.org.atool.fluent.mybatis.MybatisPlusBootConfiguration;
import cn.org.atool.fluent.mybatis.demo.generate.ITable;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.test4j.junit5.Test4J;
import org.test4j.module.database.sql.DataSourceCreatorFactory;
import org.test4j.module.database.sql.DataSourceDefaultCreator;

import javax.sql.DataSource;

@ContextConfiguration(classes = {
        TestSpringConfig.class,
        MybatisPlusBootConfiguration.class
})
public abstract class BaseTest extends Test4J implements ITable {
}

@Configuration
@ComponentScan(basePackages = {
        "cn.org.atool.fluent.mybatis.demo.generate",
        "cn.org.atool.fluent.mybatis.demo.notgen"
})
@MapperScan("cn.org.atool.fluent.mybatis.demo.generate.mapper")
class TestSpringConfig {
    @Bean("dataSource")
    public DataSource newDataSource() {
        return null;
    }
}
