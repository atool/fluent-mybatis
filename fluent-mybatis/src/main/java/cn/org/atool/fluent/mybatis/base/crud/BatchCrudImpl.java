package cn.org.atool.fluent.mybatis.base.crud;

import cn.org.atool.fluent.mybatis.base.BatchCrud;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.IHasDbType;
import cn.org.atool.fluent.mybatis.base.IRef;
import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.base.entity.PkGeneratorKits;
import cn.org.atool.fluent.mybatis.base.provider.SqlKit;
import cn.org.atool.fluent.mybatis.base.provider.SqlProvider;
import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.fluent.mybatis.segment.BaseWrapper;
import cn.org.atool.fluent.mybatis.segment.WhereBase;
import cn.org.atool.fluent.mybatis.segment.model.WrapperData;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.*;
import java.util.function.Supplier;

import static cn.org.atool.fluent.mybatis.mapper.StrConstant.MapperSqlProvider;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.assertNotNull;
import static java.lang.String.format;

/**
 * 批量增删改语句构造实现
 *
 * @author wudarui
 */
@Accessors(chain = true)
@SuppressWarnings({"rawtypes", "unchecked"})
public class BatchCrudImpl implements BatchCrud, IHasDbType {
    @Getter
    protected final WrapperData wrapperData;

    private final List<String> list = new ArrayList<>();

    public BatchCrudImpl() {
        this.wrapperData = new WrapperData(this);
    }

    public String batchSql() {
        return String.join(";\n", list);
    }

    @Override
    public BatchCrud addUpdate(IBaseUpdate... updates) {
        for (IBaseUpdate updater : updates) {
            if (!(updater instanceof BaseWrapper)) {
                throw new IllegalArgumentException("the updater should be instance of BaseWrapper");
            }
            SqlProvider provider = this.findSqlProvider(((BaseWrapper) updater).getEntityClass());
            String sql = SqlKit.factory(provider).updateBy(provider, updater.getWrapperData());
            updater.getWrapperData().sharedParameter(wrapperData);
            list.add(sql);
        }
        return this;
    }

    @Override
    public BatchCrud addDelete(IBaseQuery... deletes) {
        for (IBaseQuery query : deletes) {
            if (!(query instanceof BaseWrapper)) {
                throw new IllegalArgumentException("the query should be instance of BaseWrapper");
            }
            SqlProvider provider = this.findSqlProvider(((BaseWrapper) query).getEntityClass());
            String sql = SqlKit.factory(provider).deleteBy(provider, query.getWrapperData());
            query.getWrapperData().sharedParameter(wrapperData);
            list.add(sql);
        }
        return this;
    }

    private static final Map<Class, SqlProvider> SQL_PROVIDER_MAP = new HashMap<>();

    private SqlProvider findSqlProvider(Class<? extends IEntity> klass) {
        if (SQL_PROVIDER_MAP.containsKey(klass)) {
            return SQL_PROVIDER_MAP.get(klass);
        }
        synchronized (SQL_PROVIDER_MAP) {
            if (SQL_PROVIDER_MAP.containsKey(klass)) {
                return SQL_PROVIDER_MAP.get(klass);
            }
            String klassName = buildSqlProviderClassName(klass);
            try {
                Class providerKlass = Class.forName(klassName);
                SqlProvider provider = (SqlProvider) providerKlass.getDeclaredConstructor().newInstance();
                SQL_PROVIDER_MAP.put(klass, provider);
                return provider;
            } catch (Exception e) {
                throw new RuntimeException("findSqlProvider[" + klassName + "] error:" + e.getMessage(), e);
            }
        }
    }

    private String buildSqlProviderClassName(Class<? extends IEntity> klass) {
        Class mapper = IRef.mapper(klass).getClass();
        if (mapper.getName().contains("$Proxy")) {
            mapper = mapper.getInterfaces()[0];
        }
        return mapper.getName() + "$" + MapperSqlProvider;
    }

    private static final String ENTITY_LIST_KEY = "list";

    @Override
    public BatchCrud addInsert(IEntity... entities) {
        for (IEntity entity : entities) {
            if (entity == null) {
                continue;
            }
            if (!wrapperData.getParameters().containsKey(ENTITY_LIST_KEY)) {
                wrapperData.getParameters().put(ENTITY_LIST_KEY, new ArrayList<>());
            }
            SqlProvider provider = this.findSqlProvider(entity.getClass());
            List values = (List) wrapperData.getParameters().get(ENTITY_LIST_KEY);
            int index = values.size();
            values.add(entity);
            String prefix = format("ew.wrapperData.parameters.%s[%d].", ENTITY_LIST_KEY, index);
            PkGeneratorKits.setPkByGenerator(entity);
            String sql = SqlKit.factory(this).insertEntity(provider, prefix, entity, entity.findPk() != null);
            list.add(sql);
        }
        return this;
    }

    @Override
    public BatchCrud addInsertSelect(String insertTable, String[] fields, IQuery query) {
        assertNotNull("query", query);
        query.getWrapperData().sharedParameter(wrapperData);
        String sql = SqlKit.factory(this).insertSelect(insertTable, fields, query);
        list.add(sql);
        return this;
    }

    @Override
    public WhereBase where() {
        throw new IllegalStateException("not supported by BatchUpdater.");
    }

    @Setter
    private DbType dbType;

    public DbType dbType() {
        return dbType == null ? IRef.instance().defaultDbType() : dbType;
    }

    @Override
    public List<String> allFields() {
        throw new RuntimeException("The method is not supported by BatchCrudImpl.");
    }

    @Override
    public Supplier<String> getTable() {
        throw new RuntimeException("The method is not supported by BatchCrudImpl.");
    }

    @Override
    public Optional<IMapping> mapping() {
        throw new RuntimeException("The method is not supported by BatchCrudImpl.");
    }
}