package cn.org.atool.fluent.mybatis.generator.template.mapper;

import org.test4j.generator.mybatis.config.impl.TableSetter;
import org.test4j.generator.mybatis.template.BaseTemplate;

import java.util.Map;

public class SharingTemplate extends BaseTemplate {
    public SharingTemplate() {
        super("templates/mapper/Sharing.java.vm", "mapper/*Sharing.java");
        super.setPartition(true);
    }

    @Override
    public String getTemplateId() {
        return "sharing";
    }

    @Override
    protected void templateConfigs(TableSetter table, Map<String, Object> parent, Map<String, Object> context) {
    }
}