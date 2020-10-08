package cn.org.atool.fluent.mybatis.generator.template.dao;

import org.test4j.generator.mybatis.config.constant.OutputDir;
import org.test4j.generator.mybatis.config.impl.TableSetter;
import org.test4j.generator.mybatis.template.BaseTemplate;

import java.util.Map;

import static org.test4j.generator.mybatis.config.constant.ConfigKey.KEY_OVER_WRITE;

public class DaoImplTemplate extends BaseTemplate {
    public DaoImplTemplate() {
        super("templates/dao/DaoImpl.java.vm", "impl/*DaoImpl.java");
        super.outputDir = OutputDir.Dao;
    }

    @Override
    public String getTemplateId() {
        return "daoImpl";
    }

    @Override
    protected void templateConfigs(TableSetter table, Map<String, Object> parent, Map<String, Object> context) {
        context.put("baseDaoName", table.getEntityPrefix() + "BaseDao");
        context.put("baseDaoPack", table.getBasePackage() + ".dao");
        context.put(KEY_OVER_WRITE, Boolean.FALSE.toString());
    }

    @Override
    protected String getPackage(TableSetter table) {
        int index = this.fileNameReg.lastIndexOf('/');
        String sub = "";
        if (index > 0) {
            sub = this.fileNameReg.substring(0, index).replace('/', '.');
        }
        return table.getDaoPackage() + "." + sub;
    }
}