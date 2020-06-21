package cn.org.atool.fluent.mybatis.generator.template.query;

import org.test4j.generator.mybatis.config.impl.TableInfoSet;
import org.test4j.generator.mybatis.template.BaseTemplate;

import java.util.Map;

public class EntityWrapperHelperTemplate extends BaseTemplate {
    public EntityWrapperHelperTemplate() {
        super("templates/query/EntityWrapperHelper.java.vm", "query/*WrapperHelper.java");
    }

    @Override
    public String getTemplateId() {
        return "wrapperHelper";
    }

    @Override
    protected void templateConfigs(TableInfoSet table, Map<String, Object> context) {
    }
}