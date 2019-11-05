package cn.org.atool.mybatis.fluent.test;

import cn.org.atool.mybatis.fluent.MybatisPlusBootConfiguration;
import cn.org.atool.mybatis.fluent.demo.dm.ITable;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.test4j.junit.Test4J;
import org.test4j.module.database.sql.Test4JDataSourceHelper;

import javax.sql.DataSource;

@ContextConfiguration(classes = {
        TestSpringConfig.class,
        MybatisPlusBootConfiguration.class
})
public abstract class BaseTest extends Test4J implements ITable {
}

@Configuration
@ComponentScan(basePackages = {"cn.org.atool.mybatis.fluent.demo"})
@MapperScan("cn.org.atool.mybatis.fluent.demo.mapper")
class TestSpringConfig {
    @Bean("dataSource")
    public DataSource newDataSource() {
        return Test4JDataSourceHelper.createLocalDataSource("dataSource");
    }
}
