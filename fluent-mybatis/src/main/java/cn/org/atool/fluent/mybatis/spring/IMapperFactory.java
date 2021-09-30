package cn.org.atool.fluent.mybatis.spring;

import cn.org.atool.fluent.mybatis.base.IRelation;
import cn.org.atool.fluent.mybatis.base.mapper.IEntityMapper;
import cn.org.atool.fluent.mybatis.refs.RefKit;
import org.apache.ibatis.session.ConfigurationKit;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.Collection;

/**
 * IMapperFactory
 *
 * @author darui.wu
 */
public interface IMapperFactory {
    /**
     * Banner输出
     */
    default void banner() {
    }

    /**
     * 返回Mapper实例
     *
     * @param mapperClass IEntityMapper接口类
     * @param <T>         IEntityMapper类型
     * @return IEntityMapper实例
     */
    <T extends IEntityMapper> T getMapper(Class<T> mapperClass);

    /**
     * 从spring容器中获取IEntityRelation bean
     *
     * @return IEntityRelation bean
     */
    IRelation getRelation();

    /**
     * 返回SqlSessionFactory
     *
     * @return SqlSessionFactory
     */
    Collection<SqlSessionFactory> getSessionFactories();

    /**
     * 初始化FluentMybatis启动前置步骤
     * <pre>
     * o Entity关联关系实现设置
     * o MapperRef中所有Mapper实例设置
     * o insert, insertBatch, listEntity中主键映射和ResultMap设置
     * o Banner打印
     * </pre>
     */
    default void initial() {
        if (MapperFactory.isInitialized) {
            return;
        }
        RefKit.initialize(this);
        for (SqlSessionFactory factory : this.getSessionFactories()) {
            new ConfigurationKit(factory.getConfiguration())
                .inserts()
                .batchInserts()
                .listEntity();
        }
        this.banner();
        MapperFactory.isInitialized = true;
    }
}