package cn.org.atool.fluent.mybatis.generator.template.mapping;

import org.test4j.generator.mybatis.config.impl.TableInfoSet;
import org.test4j.generator.mybatis.template.BaseTemplate;

import java.util.Map;

import static org.test4j.generator.mybatis.config.constant.ConfigKey.KEY_MAPPING;

public class MappingTemplate extends BaseTemplate {
    public MappingTemplate() {
        super("templates/mapping/TableMapping.java.vm", "mapping/*MP.java");
    }

    @Override
    public String getTemplateId() {
        return KEY_MAPPING;
    }

    @Override
    protected void templateConfigs(TableInfoSet table, Map<String, Object> context) {
    }
}