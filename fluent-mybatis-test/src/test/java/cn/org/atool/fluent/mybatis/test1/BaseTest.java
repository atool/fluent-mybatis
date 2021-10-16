package cn.org.atool.fluent.mybatis.test1;

import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.fluent.mybatis.spring.MapperFactory;
import cn.org.atool.fluent.mybatis.base.mapper.IMapper;
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
import java.util.Arrays;

import static cn.org.atool.fluent.mybatis.metadata.feature.PagedFormat.ORACLE_LIMIT;

@SpringContext(
    classes = TestSpringConfig.class,
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
public abstract class BaseTest extends Test4J {
}

@SuppressWarnings("unchecked")
@Configuration
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
        // 以下部分根据自己的实际情况配置
        // 如果有mybatis原生文件, 请在这里加载
        bean.setMapperLocations(resolver.getResources("classpath*:mapper/*.xml"));
        /* bean.setMapperLocations(
        /*      new ClassPathResource("mapper/xml1.xml"),
        /*      new ClassPathResource("mapper/xml2.xml")
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
        return new MapperFactory()
            .dbType(DbType.MYSQL)
            .tableSupplier(t -> "fluent_mybatis." + t, StudentEntity.class)
            .register("java.util.List<java.lang.String>", obj -> Arrays.asList(String.valueOf(obj).split(";")))
            .initializer(() -> {
                DbType.ORACLE.setEscapeExpress("[?]"); // 只是示例, ORACLE的转义方式不是[?], SQL Server才是
                DbType.ORACLE.setPagedFormat(ORACLE_LIMIT.getFormat() + "/**测试而已**/");
            });
    }
}
