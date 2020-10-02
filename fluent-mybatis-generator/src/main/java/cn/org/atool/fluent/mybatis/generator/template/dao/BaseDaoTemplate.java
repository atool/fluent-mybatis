package cn.org.atool.fluent.mybatis.generator.template.dao;

import org.test4j.generator.mybatis.config.constant.OutputDir;
import org.test4j.generator.mybatis.config.impl.TableSetter;
import org.test4j.generator.mybatis.template.BaseTemplate;

import java.util.Map;

import static java.util.stream.Collectors.joining;

public class BaseDaoTemplate extends BaseTemplate {
    public BaseDaoTemplate() {
        super("templates/dao/BaseDao.java.vm", "dao/*BaseDao.java");
        this.outputDir = OutputDir.Base;
    }

    @Override
    public String getTemplateId() {
        return "baseDao";
    }

    @Override
    protected void templateConfigs(TableSetter table, Map<String, Object> context) {
        Map<String, String> interfaces = table.getBaseDaoInterfaces();
        if (interfaces != null && interfaces.size() > 0) {
            context.put("interfaces",
                interfaces.values().stream().map(i -> "import " + i + ";").collect(joining("\n")));
            context.put("interfaceName",
                interfaces.keySet().stream()
                    .map(str -> super.replace(str, table.getContext(), "${entity}", "${query}", "${update}"))
                    .collect(joining(", ", ", ", "")));
        }
    }
}