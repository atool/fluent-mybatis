package cn.org.atool.fluent.mybatis.base.crud;

import cn.org.atool.fluent.mybatis.base.BatchCrud;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.segment.BaseWrapper;
import cn.org.atool.fluent.mybatis.segment.WhereBase;
import cn.org.atool.fluent.mybatis.segment.model.WrapperData;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.assertNotNull;
import static java.lang.String.format;

/**
 * 批量增删改语句构造实现
 *
 * @author wudarui
 */
public class BatchCrudImpl implements BatchCrud {
    @Getter
    protected final WrapperData wrapperData = new WrapperData();

    private final List<String> list = new ArrayList<>();

    public String batchSql() {
        return list.stream().collect(Collectors.joining(";\n"));
    }

    @Override
    public BatchCrud addUpdate(IBaseUpdate... updates) {
        for (IBaseUpdate updater : updates) {
            if (!(updater instanceof BaseWrapper)) {
                throw new IllegalArgumentException("the updater should be instance of BaseWrapper");
            }
            BaseSqlProvider provider = this.findSqlProvider(updater.getWrapperData().getEntityClass());
            String sql = provider.buildUpdaterSql(updater.getWrapperData());
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
            BaseSqlProvider provider = this.findSqlProvider(query.getWrapperData().getEntityClass());
            String sql = provider.buildDeleteSql(query.getWrapperData(), false);
            query.getWrapperData().sharedParameter(wrapperData);
            list.add(sql);
        }
        return this;
    }

    private static final Map<Class, BaseSqlProvider> SQL_PROVIDER_MAP = new HashMap<>();

    private BaseSqlProvider findSqlProvider(Class<? extends IEntity> klass) {
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
                BaseSqlProvider provider = (BaseSqlProvider) providerKlass.getDeclaredConstructor().newInstance();
                SQL_PROVIDER_MAP.put(klass, provider);
                return provider;
            } catch (Exception e) {
                throw new RuntimeException("findSqlProvider[" + klassName + "] error:" + e.getMessage(), e);
            }
        }
    }

    private String buildSqlProviderClassName(Class<? extends IEntity> klass) {
        String pack = klass.getPackage().getName();
        pack = pack.substring(0, pack.length() - ".entity".length()) + ".helper";
        String name = klass.getSimpleName();
        name = name.substring(0, name.length() - "Entity".length()) + "SqlProvider";
        String klassName = pack + "." + name;
        return klassName;
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
            BaseSqlProvider provider = this.findSqlProvider(entity.getClass());
            List values = (List) wrapperData.getParameters().get(ENTITY_LIST_KEY);
            int index = values.size();
            values.add(entity);
            String prefix = format("ew.wrapperData.parameters.%s[%d].", ENTITY_LIST_KEY, index);
            String sql = provider.buildInsertSql(prefix, entity, entity.findPk() != null);
            list.add(sql);
        }
        return this;
    }

    @Override
    public BatchCrud addInsertSelect(String insertTable, String[] fields, IQuery query) {
        assertNotNull("query", query);
        query.getWrapperData().sharedParameter(wrapperData);
        String sql = BaseSqlProvider.buildInsertSelect(insertTable, fields, query);
        list.add(sql);
        return this;
    }

    @Override
    public WhereBase where() {
        throw new IllegalStateException("not supported by BatchUpdater.");
    }
}