package cn.org.atool.fluent.mybatis.generator.template.query;

import org.test4j.generator.mybatis.config.impl.TableInfoSet;
import org.test4j.generator.mybatis.template.BaseTemplate;

import java.util.Map;

public class EntityUpdateTemplate extends BaseTemplate {
    public EntityUpdateTemplate() {
        super("templates/query/EntityUpdate.java.vm", "query/*Update.java");
    }

    @Override
    public String getTemplateId() {
        return "entityUpdate";
    }

    @Override
    protected void templateConfigs(TableInfoSet table, Map<String, Object> context) {
    }
}