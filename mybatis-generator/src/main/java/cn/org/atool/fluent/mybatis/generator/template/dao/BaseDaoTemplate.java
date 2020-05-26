package cn.org.atool.fluent.mybatis.generator.template.dao;

import org.test4j.generator.mybatis.config.constant.OutputDir;
import org.test4j.generator.mybatis.config.TableInfo;
import org.test4j.generator.mybatis.template.BaseTemplate;

import java.util.Map;

import static java.util.stream.Collectors.joining;

public class BaseDaoTemplate extends BaseTemplate {
    public BaseDaoTemplate() {
        super("templates/dao/BaseDao.java.vm", "dao/base/*BaseDao.java");
        this.outputDir = OutputDir.Base;
    }

    @Override
    public String getTemplateId() {
        return "baseDao";
    }

    @Override
    protected void templateConfigs(TableInfo table, Map<String, Object> context) {
        Map<String, String> interfaces = table.getBaseDaoInterfaces();
        if (interfaces != null && interfaces.size() > 0) {
            context.put("interface",
                interfaces.keySet().stream()
                    .map(str -> str.replace("${entity}", super.getConfig(table.getContext(), "entity.name")))
                    .map(str -> str.replace("${query}", super.getConfig(table.getContext(), "entityQuery.name")))
                    .map(str -> str.replace("${update}", super.getConfig(table.getContext(), "entityUpdate.name")))
                    .collect(joining(", ", ", ", "")));
            context.put("interfaceImports",
                interfaces.values().stream().map(i -> "import " + i + ";").collect(joining("\n")));
        }
    }
}
