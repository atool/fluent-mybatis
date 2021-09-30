package cn.org.atool.fluent.mybatis.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * spring boot环境下自动装配{@link MapperFactory} spring bean.
 *
 * @author darui.wu
 */
@Configuration
public class FluentMybatisAutoConfig {
    @Bean
    public MapperFactory mapperFactory() {
        return new MapperFactory();
    }
}