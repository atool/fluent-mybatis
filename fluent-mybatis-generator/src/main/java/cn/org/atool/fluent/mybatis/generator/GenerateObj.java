package cn.org.atool.fluent.mybatis.generator;

import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 生成的对象
 *
 * @author darui.wu
 * @create 2019/10/17 1:39 下午
 */
@Data
@Accessors(chain = true)
public class GenerateObj {

    private String tableName;

    private String withoutSuffixEntity;

    private String mix;

    private String mixInstance;

    private String mixCleanMethod;

    private String mp;

    private String tableMap;


    public static GenerateObj init(Table table) {
        Map<String, String> config = GenerateObj.currConfig.get();
        currConfig.remove();
        GenerateObj obj = new GenerateObj()
                .setTableName(table.getTableName())
                .setWithoutSuffixEntity(config.get("withoutSuffixEntity"))
                .setMix(config.get("fileTableMix"))
                .setMixCleanMethod(String.format("clean%sTable", config.get("withoutSuffixEntity")))
                .setMp(config.get("fileMP"))
                .setTableMap(config.get("fileTableMap"));
        obj.mixInstance = obj.mix.substring(0, 1).toLowerCase() + obj.mix.substring(1);
        if (obj.tableName.startsWith("t_")) {
            obj.tableName = obj.tableName.substring(2);
        }
        return obj;
    }

    static ThreadLocal<Map<String, String>> currConfig = new ThreadLocal<>();

    public static void setCurrConfig(Map currConfig) {
        GenerateObj.currConfig.set(currConfig);
    }

    public static void generate(List<GenerateObj> objs, String output, String basePackage) {
        VelocityTemplateEngine engine = new VelocityTemplateEngine().init(null);
        Map<String, Object> config = new HashMap<>();
        config.put("basePackage", basePackage);
        config.put("objs", objs);
        String templateDir = "/templates/single/";
        String packDir = String.format("%s/%s/", output, basePackage.replace('.', '/'));
        try {
            engine.writer(config, templateDir + "Mixes.java.vm", packDir + "TableMixes.java");
            engine.writer(config, templateDir + "ITable.java.vm", packDir + "ITable.java");
            engine.writer(config, templateDir + "DataSourceScript.java.vm", packDir + "DataSourceScript.java");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}