package cn.org.atool.mbplus;

import cn.org.atool.mbplus.mapper.IMapper;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.core.toolkit.GlobalConfigUtils;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class})
@ConditionalOnSingleCandidate(DataSource.class)
public class MybatisPlusBootConfiguration {
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
        return new MybatisPlusSqlInjector();
    }
}
