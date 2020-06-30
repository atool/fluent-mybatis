package cn.org.atool.fluent.mybatis;

import org.apache.ibatis.session.SqlSessionFactory;
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
    @ConditionalOnMissingBean({FluentMybatisSessionFactoryBean.class, SqlSessionFactory.class})
    public FluentMybatisSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource) throws Exception {
        FluentMybatisSessionFactoryBean bean = new FluentMybatisSessionFactoryBean();
        bean.setDataSource(dataSource);
        return bean;
    }
}