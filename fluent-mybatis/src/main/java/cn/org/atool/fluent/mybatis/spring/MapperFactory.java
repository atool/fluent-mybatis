package cn.org.atool.fluent.mybatis.spring;

import cn.org.atool.fluent.mybatis.base.IRelation;
import cn.org.atool.fluent.mybatis.base.mapper.IEntityMapper;
import cn.org.atool.fluent.mybatis.utility.MybatisUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.Collection;

import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.assertNotEmpty;

/**
 * 所有Mapper实例登记, 需spring bean初始化
 *
 * @author wudarui
 */
@SuppressWarnings({"all"})
@Slf4j
public class MapperFactory implements IMapperFactory {
    private ApplicationContext context;

    @Getter
    protected static boolean isInitialized = false;

    @Getter
    private Collection<SqlSessionFactory> sessionFactories;

    @Autowired
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.context = context;
        this.sessionFactories = context.getBeansOfType(SqlSessionFactory.class).values();
        assertNotEmpty("SqlSessionFactory", this.sessionFactories);
        this.initial();
    }

    @Override
    public <T extends IEntityMapper> T getMapper(Class<T> mapperClass) {
        return context.getBean(mapperClass);
    }

    @Override
    public IRelation getRelation() {
        try {
            return context.getBean(IRelation.class);
        } catch (NoSuchBeanDefinitionException ignored) {
            // do nothing
            return null;
        }
    }

    @Override
    public void banner() {
        log.info(MybatisUtil.getVersionBanner());
        this.isInitialized = true;
    }
}