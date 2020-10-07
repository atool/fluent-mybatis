package cn.org.atool.fluent.mybatis.generator.template.dao;

import org.test4j.generator.mybatis.config.constant.OutputDir;
import org.test4j.generator.mybatis.config.impl.TableSetter;
import org.test4j.generator.mybatis.template.BaseTemplate;

import java.util.Map;

public class DaoImplTemplate extends BaseTemplate {
    public DaoImplTemplate() {
        super("templates/dao/DaoImpl.java.vm", "impl/*DaoImpl.java");
        super.outputDir = OutputDir.Dao;
    }

    @Override
    public String getTemplateId() {
        return "daoImpl";
    }

    protected String getPackage(TableSetter table) {
        int index = this.fileNameReg.lastIndexOf('/');
        String sub = "";
        if (index > 0) {
            sub = this.fileNameReg.substring(0, index).replace('/', '.');
        }
        return table.getDaoPackage() + "." + sub;
    }

    @Override
    protected void templateConfigs(TableSetter table, Map<String, Object> context) {
        context.put("baseDaoName", table.getEntityPrefix() + "BaseDao");
        context.put("baseDaoPack", table.getBasePackage() + ".dao");
    }
}