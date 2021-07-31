package cn.org.atool.fluent.mybatis.test;

import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.fluent.mybatis.refs.Refs;
import cn.org.atool.fluent.mybatis.spring.MapperFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.test4j.integration.spring.SpringContext;
import org.test4j.junit5.Test4J;
import org.test4j.module.database.proxy.DataSourceCreator;

import javax.sql.DataSource;

import static cn.org.atool.fluent.mybatis.metadata.feature.PagedFormat.ORACLE_LIMIT;

@SpringContext(
    classes = TestSpringConfig.class,
    basePackages = {
        "cn.org.atool.fluent.mybatis.generate.dao.impl",
        "cn.org.atool.fluent.mybatis.customize.impl"
    }
)
@MapperScan({"cn.org.atool.fluent.mybatis.generate.mapper",
    "cn.org.atool.fluent.mybatis.customize.mapper",
    "cn.org.atool.fluent.mybatis.origin.mapper",
    "cn.org.atool.fluent.mybatis.db"
})
public abstract class BaseTest extends Test4J {

}

@Configuration
class TestSpringConfig {
    static {
        Refs.Query.student.setTableDynamic(t -> "fluent_mybatis." + t);
        DbType.ORACLE.setEscapeExpress("[?]"); // 只是示例, ORACLE的转义方式不是[?], SQL Server才是
        DbType.ORACLE.setPagedFormat(ORACLE_LIMIT.getFormat() + "/**测试而已**/");
    }

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
        /* bean.setMapperLocations(
        /*      new ClassPathResource("mapper/MyXmlMapper.xml"),
        /*      new ClassPathResource("mapper/BatchUpdate.xml")
        /* );
        */
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