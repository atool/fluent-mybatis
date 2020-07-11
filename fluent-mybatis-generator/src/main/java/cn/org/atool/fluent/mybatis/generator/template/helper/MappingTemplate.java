package cn.org.atool.fluent.mybatis.generator.template.helper;

import org.test4j.generator.mybatis.config.impl.TableSetter;
import org.test4j.generator.mybatis.template.BaseTemplate;

import java.util.Map;

import static org.test4j.generator.mybatis.config.constant.ConfigKey.KEY_MAPPING;

public class MappingTemplate extends BaseTemplate {
    public MappingTemplate() {
        super("templates/helper/TableMapping.java.vm", "helper/*Mapping.java");
    }

    @Override
    public String getTemplateId() {
        return KEY_MAPPING;
    }

    @Override
    protected void templateConfigs(TableSetter table, Map<String, Object> context) {
    }
}