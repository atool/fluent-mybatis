package cn.org.atool.fluent.mybatis.test;

import cn.org.atool.fluent.form.annotation.FormServiceScan;
import cn.org.atool.fluent.form.annotation.FormServiceScans;
import cn.org.atool.fluent.mybatis.base.mapper.IMapper;
import cn.org.atool.fluent.mybatis.config.SpringConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.test4j.integration.spring.SpringContext;
import org.test4j.junit5.Test4J;
import org.test4j.module.database.proxy.DataSourceCreator;

import javax.sql.DataSource;

@SpringContext(
    classes = {TestSpringConfig.class, SpringConfig.class},
    basePackages = {
        "cn.org.atool.fluent.mybatis.generator.*.dao.impl",
        "cn.org.atool.fluent.mybatis.customize.impl"
    }
)
@MapperScan(basePackages = {
    "cn.org.atool.fluent.mybatis.generator.*.mapper",
    "cn.org.atool.fluent.mybatis.db"
}, markerInterface = IMapper.class)
@MapperScan({
    "cn.org.atool.fluent.mybatis.customize.mapper",
    "cn.org.atool.fluent.mybatis.origin.mapper"
})
@FormServiceScans({@FormServiceScan({
    "cn.org.atool.fluent.mybatis.formservice",
    "cn.org.atool.fluent.mybatis.generator.shared2.dao.impl"})
})
public class BaseTest extends Test4J {
}

@Configuration
class TestSpringConfig {
    @Bean("dataSource")
    public DataSource newDataSource() {
        return DataSourceCreator.create("dataSource");
    }
}
