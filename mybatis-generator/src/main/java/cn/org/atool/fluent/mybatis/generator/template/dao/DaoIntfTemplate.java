package cn.org.atool.fluent.mybatis.generator.template.dao;

import org.test4j.generator.mybatis.config.constant.OutputDir;
import org.test4j.generator.mybatis.config.TableInfo;
import org.test4j.generator.mybatis.template.BaseTemplate;

import java.util.Map;

public class DaoIntfTemplate extends BaseTemplate {
    public DaoIntfTemplate() {
        super("templates/dao/DaoIntf.java.vm", "dao/intf/*Dao.java");
        super.outputDir = OutputDir.Dao;
    }

    @Override
    public String getTemplateId() {
        return "daoIntf";
    }

    @Override
    protected void templateConfigs(TableInfo table, Map<String, Object> context) {
    }
}
