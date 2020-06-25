package cn.org.atool.fluent.mybatis.generator.template.helper;

import org.test4j.generator.mybatis.config.impl.TableInfoSet;
import org.test4j.generator.mybatis.template.BaseTemplate;

import java.util.Map;


public class EntityHelperTemplate extends BaseTemplate {
    public EntityHelperTemplate() {
        super("templates/helper/EntityHelper.java.vm", "helper/*EntityHelper.java");
    }

    @Override
    public String getTemplateId() {
        return "entityHelper";
    }

    @Override
    protected void templateConfigs(TableInfoSet table, Map<String, Object> context) {
    }
}