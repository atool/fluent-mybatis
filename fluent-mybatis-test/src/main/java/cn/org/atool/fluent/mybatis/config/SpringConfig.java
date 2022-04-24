package cn.org.atool.fluent.mybatis.config;

import cn.org.atool.fluent.form.FormKit;
import cn.org.atool.fluent.form.annotation.FormServiceScan;
import cn.org.atool.fluent.form.annotation.FormServiceScans;
import cn.org.atool.fluent.mybatis.base.mapper.IMapper;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.fluent.mybatis.spring.MapperFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.sql.DataSource;
import java.util.Arrays;

import static cn.org.atool.fluent.mybatis.metadata.feature.PagedFormat.ORACLE_LIMIT;

@SuppressWarnings({"unchecked"})
@Configuration
@MapperScan(basePackages = {
    "cn.org.atool.fluent.mybatis.generator.*.mapper",
    "cn.org.atool.fluent.mybatis.db"
}, markerInterface = IMapper.class)
@MapperScan({
    "cn.org.atool.fluent.mybatis.customize.mapper",
    "cn.org.atool.fluent.mybatis.origin.mapper"
})
@FormServiceScans({
    @FormServiceScan({"cn.org.atool.fluent.mybatis.formservice"})
})
public class SpringConfig {
    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        // 以下部分根据自己的实际情况配置
        // 如果有mybatis原生文件, 请在这里加载
        bean.setMapperLocations(resolver.getResources("classpath*:mapper/*.xml"));
        /* bean.setMapperLocations(
        /*      new ClassPathResource("mapper/xml1.xml"),
        /*      new ClassPathResource("mapper/xml2.xml")
        /* );
        */
        MapperFactory.setMapUnderscoreToCamelCase(bean);
        return bean;
    }

    @Bean
    public MapperFactory mapperFactory() {
        return new MapperFactory()
            .dbType(DbType.MYSQL)
            .tableSupplier((t, v) -> "fluent_mybatis." + t, StudentEntity.class)
            .register("java.util.List<java.lang.String>", obj -> Arrays.asList(String.valueOf(obj).split(";")))
            .initializer(() -> {
                DbType.ORACLE.setEscapeExpress("[?]"); // 只是示例, ORACLE的转义方式不是[?], SQL Server才是
                DbType.ORACLE.setPagedFormat(ORACLE_LIMIT.getFormat() + "/**测试而已**/");
                /* student有多个Entity映射 */
                FormKit.mapping("student", StudentEntity.class);
            });
    }
}