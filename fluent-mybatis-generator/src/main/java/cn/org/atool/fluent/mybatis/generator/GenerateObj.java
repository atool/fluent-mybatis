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

    private String camelEntity;

    private String mix;

    private String mixInstance;

    private String mixCleanMethod;

    private String mp;

    private String tableMap;

    private String entityMap;


    public static GenerateObj init(Table table) {
        Map<String, String> config = GenerateObj.currConfig.get();
        currConfig.remove();
        GenerateObj obj = new GenerateObj()
                .setTableName(table.getTableName())
                .setWithoutSuffixEntity(config.get("withoutSuffixEntity"))
                .setMix(config.get("fileTableMix"))
                .setMixCleanMethod(String.format("clean%sTable", config.get("withoutSuffixEntity")))
                .setMp(config.get("fileMP"))
                .setTableMap(config.get("fileTableMap"))
                .setEntityMap(config.get("fileEntityMap"));
        obj.setCamelEntity(obj.withoutSuffixEntity.substring(0, 1).toLowerCase() + obj.withoutSuffixEntity.substring(1));
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

    public static void generate(List<GenerateObj> objs, String output, String testOutput, String basePackage) {
        VelocityTemplateEngine engine = new VelocityTemplateEngine().init(null);
        Map<String, Object> config = new HashMap<>();
        config.put("basePackage", basePackage);
        config.put("objs", objs);
        String templateDir = "/templates/";
        String srcPackDir = String.format("%s/%s/", output, basePackage.replace('.', '/'));
        String testPackDir = String.format("%s/%s/", testOutput, basePackage.replace('.', '/'));

        try {
            engine.writer(config, templateDir + "mix/Mixes.java.vm", testPackDir + "TableMixes.java");
            engine.writer(config, templateDir + "ITable.java.vm", testPackDir + "ITable.java");
            engine.writer(config, templateDir + "DataSourceScript.java.vm", testPackDir + "DataSourceScript.java");

            engine.writer(config, templateDir + "datamap/TM.java.vm", testPackDir + "datamap/TM.java");
            engine.writer(config, templateDir + "datamap/EM.java.vm", testPackDir + "datamap/EM.java");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}