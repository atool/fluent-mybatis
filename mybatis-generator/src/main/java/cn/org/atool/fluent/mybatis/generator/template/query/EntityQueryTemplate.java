package cn.org.atool.fluent.mybatis.generator.template.query;

import org.test4j.generator.mybatis.config.TableInfo;
import org.test4j.generator.mybatis.template.BaseTemplate;

import java.util.Map;

public class EntityQueryTemplate extends BaseTemplate {
    public EntityQueryTemplate() {
        super("templates/query/EntityQuery.java.vm", "query/*EntityQuery.java");
    }

    @Override
    public String getTemplateId() {
        return "entityQuery";
    }

    @Override
    protected void templateConfigs(TableInfo table, Map<String, Object> context) {
    }
}
