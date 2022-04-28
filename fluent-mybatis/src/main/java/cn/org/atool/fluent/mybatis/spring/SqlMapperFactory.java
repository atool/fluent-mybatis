package cn.org.atool.fluent.mybatis.spring;

import cn.org.atool.fluent.common.kits.KeyMap;
import cn.org.atool.fluent.mybatis.base.intf.IRelation;
import cn.org.atool.fluent.mybatis.base.mapper.IEntityMapper;
import cn.org.atool.fluent.mybatis.functions.IExecutor;
import lombok.Getter;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * 非spring环境时代替MapperFactory使用
 *
 * @author wudarui
 */
@SuppressWarnings({"rawtypes", "unused"})
public class SqlMapperFactory implements IMapperFactory {
    @Getter
    private final SqlSessionFactory sessionFactory;

    private final KeyMap<IEntityMapper> mappers = new KeyMap<>();

    @Getter
    private final List<IExecutor> initializers = new ArrayList<>();

    @SafeVarargs
    public SqlMapperFactory(DataSource dataSource, Class<? extends IEntityMapper>... classes) {
        Configuration configuration = this.initConfiguration(dataSource, classes);
        this.sessionFactory = new SqlSessionFactoryBuilder().build(configuration);
        try (SqlSession session = sessionFactory.openSession()) {
            for (Class<? extends IEntityMapper> klass : classes) {
                IEntityMapper mapper = session.getMapper(klass);
                mappers.put(klass, mapper);
            }
        }
        this.init();
        MapperFactory.setInitialized(true);
    }

    private Configuration initConfiguration(DataSource dataSource, Class<? extends IEntityMapper>[] classes) {
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment env = new Environment("DEFAULT", transactionFactory, dataSource);
        Configuration configuration = new Configuration(env);
        for (Class<? extends IEntityMapper> mapper : classes) {
            configuration.addMapper(mapper);
        }
        return configuration;
    }

    @Override
    public Collection<IEntityMapper> getMappers() {
        return mappers.values();
    }

    @Override
    public Collection<IRelation> getRelations() {
        return Collections.emptyList();
    }

    @Override
    public Collection<SqlSessionFactory> getSessionFactories() {
        return Collections.singletonList(sessionFactory);
    }


    public <M extends IEntityMapper> void execute(Class<M> klass, Consumer<M> consumer) {
        try (SqlSession sqlSession = sessionFactory.openSession()) {
            M mapper = sqlSession.getMapper(klass);
            consumer.accept(mapper);
        }
    }
}