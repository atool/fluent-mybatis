package cn.org.atool.fluent.mybatis.generator.template.query;

import org.test4j.generator.mybatis.config.TableInfo;
import org.test4j.generator.mybatis.template.BaseTemplate;

import java.util.Map;

public class EntityUpdateTemplate extends BaseTemplate {
    public EntityUpdateTemplate() {
        super("templates/query/EntityUpdate.java.vm", "query/*EntityUpdate.java");
    }

    @Override
    public String getTemplateId() {
        return "entityUpdate";
    }

    @Override
    protected void templateConfigs(TableInfo table, Map<String, Object> context) {
    }
}
