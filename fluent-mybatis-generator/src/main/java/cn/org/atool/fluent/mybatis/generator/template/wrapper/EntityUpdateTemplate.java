package cn.org.atool.fluent.mybatis.generator.template.wrapper;

import org.test4j.generator.mybatis.config.impl.TableSetter;
import org.test4j.generator.mybatis.template.BaseTemplate;

import java.util.Map;

public class EntityUpdateTemplate extends BaseTemplate {
    public EntityUpdateTemplate() {
        super("templates/wrapper/EntityUpdate.java.vm", "wrapper/*Update.java");
    }

    @Override
    public String getTemplateId() {
        return "entityUpdate";
    }

    @Override
    protected void templateConfigs(TableSetter table, Map<String, Object> context) {
    }
}