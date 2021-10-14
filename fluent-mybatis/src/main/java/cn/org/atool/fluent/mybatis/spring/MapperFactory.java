package cn.org.atool.fluent.mybatis.spring;

import cn.org.atool.fluent.mybatis.base.IRelation;
import cn.org.atool.fluent.mybatis.base.entity.AMapping;
import cn.org.atool.fluent.mybatis.base.mapper.IEntityMapper;
import cn.org.atool.fluent.mybatis.base.mapper.IWrapperMapper;
import cn.org.atool.fluent.mybatis.refs.RefKit;
import cn.org.atool.fluent.mybatis.utility.MybatisUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ConfigurationKit;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.Collection;
import java.util.Map;

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
    private static boolean isInitialized = false;

    private Collection<SqlSessionFactory> sessionFactories;

    @Autowired
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.context = context;
        this.sessionFactories = context.getBeansOfType(SqlSessionFactory.class).values();
        assertNotEmpty("SqlSessionFactory", this.sessionFactories);
        if (MapperFactory.isInitialized) {
            return;
        }
        this.initBySpring();
        this.initByApp();
        this.initRelation();
        log.info(MybatisUtil.getVersionBanner());
        MapperFactory.isInitialized = true;
    }

    /**
     * 初始化FluentMybatis启动前置步骤
     * <pre>
     * o Entity关联关系实现设置
     * o MapperRef中所有Mapper实例设置
     * o insert, insertBatch, listEntity中主键映射和ResultMap设置
     * o Banner打印
     * </pre>
     */
    private void initBySpring() {
        Map<String, IWrapperMapper> mappers = context.getBeansOfType(IWrapperMapper.class);
        for (IWrapperMapper mapper : mappers.values()) {
            AMapping mapping = (AMapping) mapper.mapping();
            RefKit.ENTITY_MAPPING.put(mapping.entityClass(), mapping);
            RefKit.ENTITY_MAPPER.put(mapping.entityClass(), mapper);
            RefKit.MAPPER_MAPPING.put(mapping.mapperClass(), mapping);
        }
        RefKit.ENTITY_MAPPING.unmodified();
        RefKit.ENTITY_MAPPER.unmodified();
        RefKit.MAPPER_MAPPING.unmodified();

        for (SqlSessionFactory factory : this.sessionFactories) {
            new ConfigurationKit(factory.getConfiguration(), RefKit.MAPPER_MAPPING)
                .inserts()
                .batchInserts()
                .listEntity();
        }
    }

    protected void initByApp() {
    }

    @Override
    public <T extends IEntityMapper> T getMapper(Class<T> mapperClass) {
        return context.getBean(mapperClass);
    }

    private void initRelation() {
        Map<String, IRelation> relations = context.getBeansOfType(IRelation.class);
        for (IRelation relation : relations.values()) {
            relation.initialize();
        }
        RefKit.relations.unmodified();
    }
}