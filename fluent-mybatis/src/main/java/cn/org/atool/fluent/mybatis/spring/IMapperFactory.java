package cn.org.atool.fluent.mybatis.spring;

import cn.org.atool.fluent.mybatis.base.IRelation;
import cn.org.atool.fluent.mybatis.base.entity.AMapping;
import cn.org.atool.fluent.mybatis.base.mapper.IEntityMapper;
import cn.org.atool.fluent.mybatis.functions.IExecutor;
import cn.org.atool.fluent.mybatis.refs.RefKit;
import org.apache.ibatis.session.ConfigurationKit;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.Collection;

/**
 * IMapperFactory
 *
 * @author darui.wu
 */
@SuppressWarnings({"unused", "rawtypes"})
public interface IMapperFactory {
    /**
     * 返回Mapper实例列表
     *
     * @return IEntityMapper实例列表
     */
    Collection<IEntityMapper> getMappers();

    /**
     * 返回实体关系定义
     *
     * @return IEntityRelation关系列表
     */
    Collection<IRelation> getRelations();

    /**
     * 返回所有SqlSessionFactory列表
     */
    Collection<SqlSessionFactory> getSessionFactories();

    /**
     * 初始化环境定义
     */
    IExecutor getInitializer();

    /**
     * 初始化FluentMybatis启动前置步骤
     * <pre>
     * o Entity关联关系实现设置
     * o MapperRef中所有Mapper实例设置
     * o insert, insertBatch, listEntity中主键映射和ResultMap设置
     * o Banner打印
     * </pre>
     */
    default void init() {
        // 设置Mapper和IMapping关联关系
        Collection<IEntityMapper> mappers = this.getMappers();
        for (IEntityMapper mapper : mappers) {
            AMapping mapping = (AMapping) mapper.mapping();
            RefKit.ENTITY_MAPPING.put(mapping.entityClass(), mapping);
            RefKit.ENTITY_MAPPER.put(mapping.entityClass(), mapper);
            RefKit.MAPPER_MAPPING.put(mapping.mapperClass(), mapping);
        }
        RefKit.ENTITY_MAPPING.unmodified();
        RefKit.ENTITY_MAPPER.unmodified();
        RefKit.MAPPER_MAPPING.unmodified();
        // 执行初始化环境方法
        if (this.getInitializer() != null) {
            this.getInitializer().execute();
        }
        // 设置实体关联关系方法
        Collection<IRelation> relations = this.getRelations();
        for (IRelation relation : relations) {
            relation.initialize();
        }
        RefKit.relations.unmodified();
        // 初始化SqlProvider方法定义
        Collection<SqlSessionFactory> factories = this.getSessionFactories();
        for (SqlSessionFactory factory : factories) {
            new ConfigurationKit(factory.getConfiguration(), RefKit.MAPPER_MAPPING)
                .inserts()
                .batchInserts()
                .listEntity();
        }
    }
}