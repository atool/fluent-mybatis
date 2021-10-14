package cn.org.atool.fluent.mybatis.spring;

import cn.org.atool.fluent.mybatis.base.IRelation;
import cn.org.atool.fluent.mybatis.base.mapper.IEntityMapper;
import cn.org.atool.fluent.mybatis.functions.IExecutor;
import cn.org.atool.fluent.mybatis.utility.MybatisUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.assertNotEmpty;

/**
 * 所有Mapper实例登记, 需spring bean初始化
 *
 * @author wudarui
 */
@SuppressWarnings({"all"})
@Slf4j
@Accessors(chain = true)
public class MapperFactory implements IMapperFactory {
    private ApplicationContext context;

    @Getter
    private static boolean isInitialized = false;

    @Getter
    private Collection<SqlSessionFactory> sessionFactories;

    @Getter
    @Setter
    private IExecutor initializer;

    @Autowired
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.context = context;
        this.sessionFactories = context.getBeansOfType(SqlSessionFactory.class).values();
        assertNotEmpty("SqlSessionFactory", this.sessionFactories);
        if (MapperFactory.isInitialized) {
            return;
        }
        this.init();

        log.info(MybatisUtil.getVersionBanner());
        MapperFactory.isInitialized = true;
    }

    @Override
    public Set<IEntityMapper> getMappers() {
        Map<String, IEntityMapper> mappers = context.getBeansOfType(IEntityMapper.class);
        return new HashSet<>(mappers.values());
    }

    @Override
    public Set<IRelation> getRelations() {
        Map<String, IRelation> relations = context.getBeansOfType(IRelation.class);
        return new HashSet<>(relations.values());
    }
}