package cn.org.atool.fluent.mybatis.generator.template.mapper;

import org.test4j.generator.mybatis.config.impl.TableSetter;
import org.test4j.generator.mybatis.template.BaseTemplate;

import java.util.Map;

public class MapperTemplate extends BaseTemplate {
    public MapperTemplate() {
        super("templates/mapper/Mapper.java.vm", "mapper/*Mapper.java");
    }

    @Override
    public String getTemplateId() {
        return "mapper";
    }

    @Override
    protected void templateConfigs(TableSetter table, Map<String, Object> context) {
        String prefix = table.getMapperBeanPrefix();
        if (prefix != null && !"".equals(prefix.trim())) {
            context.put("prefix", prefix);
        }
    }
}