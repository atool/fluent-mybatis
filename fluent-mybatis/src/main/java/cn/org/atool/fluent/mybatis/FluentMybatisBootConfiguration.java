package cn.org.atool.fluent.mybatis;

import cn.org.atool.fluent.mybatis.injector.FluentMybatisSqlInjector;
import cn.org.atool.fluent.mybatis.injector.ISqlInjector;
import cn.org.atool.fluent.mybatis.mapper.IMapper;
import cn.org.atool.fluent.mybatis.util.GlobalConfig;
import cn.org.atool.fluent.mybatis.util.GlobalConfigUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.MybatisSqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * MybatisPlusBootConfiguration
 *
 * @author darui.wu
 */
@Configuration
@ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class})
@ConditionalOnSingleCandidate(DataSource.class)
public class FluentMybatisBootConfiguration {
    @Bean
    @ConditionalOnMissingBean({MybatisSqlSessionFactoryBean.class, SqlSessionFactory.class})
    public MybatisSqlSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource, ISqlInjector sqlInjector) {
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        {
            bean.setDataSource(dataSource);
            GlobalConfig gc = GlobalConfigUtils.defaults();
            gc.setSqlInjector(sqlInjector);
            gc.setSuperMapperClass(IMapper.class);
            bean.setGlobalConfig(gc);

        }
        return bean;
    }

    @Bean
    @ConditionalOnMissingBean
    public ISqlInjector sqlInjector() {
        return new FluentMybatisSqlInjector();
    }
}