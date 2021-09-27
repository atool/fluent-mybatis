package org.apache.ibatis.session;

import cn.org.atool.fluent.mybatis.base.IRef;
import cn.org.atool.fluent.mybatis.base.entity.AMapping;
import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.base.model.KeyMap;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.base.provider.StatementBuilder;
import org.apache.ibatis.mapping.MappedStatement;

import java.util.HashMap;
import java.util.Map;

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

    public ConfigurationKit(Configuration configuration) {
        this.configuration = configuration;
        KeyMap<AMapping> mappers = IRef.instance().mapperMapping();
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
            IMapping mapping = entry.getValue();
            FieldMapping primary = mapping.primaryMapping();
            if (primary == null) {
                continue;
            }
            MappedStatement statement = this.configuration.getMappedStatement(entry.getKey());
            MappedStatement replaced = new StatementBuilder(mapping, statement).insertStatement();
            this.replaced(replaced);
        }
        this.inserts.clear();
        return this;
    }

    public ConfigurationKit batchInserts() {
        for (Map.Entry<String, IMapping> entry : batchInserts.entrySet()) {
            IMapping mapping = entry.getValue();
            FieldMapping primary = mapping.primaryMapping();
            if (primary == null) {
                continue;
            }
            MappedStatement statement = this.configuration.getMappedStatement(entry.getKey());
            MappedStatement replaced = new StatementBuilder(mapping, statement).insertBatchStatement();
            this.replaced(replaced);
        }
        this.batchInserts.clear();
        return this;
    }

    public ConfigurationKit listEntity() {
        for (Map.Entry<String, IMapping> entry : listEntities.entrySet()) {
            IMapping mapping = entry.getValue();

            MappedStatement statement = this.configuration.getMappedStatement(entry.getKey());
            MappedStatement replaced = new StatementBuilder(mapping, statement).listEntityStatement();
            this.replaced(replaced);
        }
        this.listEntities.clear();
        return this;
    }

    private void replaced(MappedStatement statement) {
        configuration.mappedStatements.remove(statement.getId());
        configuration.addMappedStatement(statement);
    }
}