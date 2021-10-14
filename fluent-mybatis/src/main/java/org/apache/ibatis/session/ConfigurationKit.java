package org.apache.ibatis.session;

import cn.org.atool.fluent.mybatis.base.entity.AMapping;
import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.base.model.KeyMap;
import cn.org.atool.fluent.mybatis.base.provider.StatementBuilder;
import org.apache.ibatis.mapping.MappedStatement;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static cn.org.atool.fluent.mybatis.mapper.FluentConst.*;

/**
 * 工具类: 替换 {@link Configuration}中的 MappedStatement
 * 为了不用反射访问{@link Configuration#mappedStatements}, 只能定义在 org.apache.ibatis.session 路径下
 *
 * @author darui.wu
 */
@SuppressWarnings("rawtypes")
public class ConfigurationKit {
    private final Configuration configuration;

    private final Map<String, IMapping> inserts = new HashMap<>();
    private final Map<String, IMapping> batchInserts = new HashMap<>();
    private final Map<String, IMapping> listEntities = new HashMap<>();

    public ConfigurationKit(Configuration configuration, KeyMap<AMapping> mappers) {
        this.configuration = configuration;
        for (Map.Entry<String, AMapping> entry : mappers.entrySet()) {
            inserts.put(entry.getKey() + "." + M_Insert, entry.getValue());
            batchInserts.put(entry.getKey() + "." + M_InsertBatch, entry.getValue());
            listEntities.put(entry.getKey() + "." + M_listEntity, entry.getValue());
        }
    }

    /**
     * 重建insert MappedStatement
     *
     * @return ignore
     */
    public ConfigurationKit inserts() {
        for (Map.Entry<String, IMapping> entry : inserts.entrySet()) {
            IMapping m = entry.getValue();
            if (m.primaryMapping() != null) {
                this.replaced(entry.getKey(), m, StatementBuilder::insertStatement);
            }
        }
        this.inserts.clear();
        return this;
    }

    public ConfigurationKit batchInserts() {
        for (Map.Entry<String, IMapping> entry : batchInserts.entrySet()) {
            IMapping m = entry.getValue();
            if (m.primaryMapping() != null) {
                this.replaced(entry.getKey(), m, StatementBuilder::insertBatchStatement);
            }
        }
        this.batchInserts.clear();
        return this;
    }

    public ConfigurationKit listEntity() {
        for (Map.Entry<String, IMapping> entry : listEntities.entrySet()) {
            IMapping m = entry.getValue();
            this.replaced(entry.getKey(), m, StatementBuilder::listEntityStatement);
        }
        this.listEntities.clear();
        return this;
    }

    private void replaced(String statementId, IMapping mapping, Function<StatementBuilder, MappedStatement> replaced) {
        if (this.configuration.hasStatement(statementId, false)) {
            MappedStatement existed = this.configuration.getMappedStatement(statementId, false);
            MappedStatement newer = replaced.apply(new StatementBuilder(mapping, existed));
            configuration.mappedStatements.remove(statementId);
            configuration.addMappedStatement(newer);
        }
    }
}