package cn.org.atool.fluent.mybatis.generator.template.query;

import org.test4j.generator.mybatis.config.TableInfo;
import org.test4j.generator.mybatis.template.BaseTemplate;

import java.util.Map;

import static java.util.stream.Collectors.joining;

public class EntityWrapperHelperTemplate extends BaseTemplate {
    public EntityWrapperHelperTemplate() {
        super("templates/query/EntityWrapperHelper.java.vm", "query/*EntityWrapperHelper.java");
    }

    @Override
    public String getTemplateId() {
        return "wrapperHelper";
    }

    @Override
    protected void templateConfigs(TableInfo table, Map<String, Object> context) {
    }
}
