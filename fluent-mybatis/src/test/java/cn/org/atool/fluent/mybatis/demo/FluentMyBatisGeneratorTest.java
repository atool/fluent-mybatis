package cn.org.atool.fluent.mybatis.demo;

import org.test4j.generator.mybatis.Generator;
import org.test4j.generator.mybatis.db.ColumnType;

/**
 * FluentMyBatisGeneratorTest：生成fluent mybatis文件
 * 使用main函数，是避免全量跑test时，误执行生成代码
 *
 * @author wudarui
 */
public class FluentMyBatisGeneratorTest {
    private static String url = "jdbc:mysql://localhost:3306/fluent_mybatis?useUnicode=true&characterEncoding=utf8";

    public static void main(String[] args) {
        String outputDir = System.getProperty("user.dir") + "/fluent-mybatis/src/test/java";
        Generator.fluentMybatis()
            .globalConfig(config -> config.setOutputDir(outputDir, outputDir, outputDir)
                .setDataSource(url, "root", "password")
                .setBasePackage("cn.org.atool.fluent.mybatis.demo.generate"))
            .tables(config -> config
                .setTablePrefix("t_")
                .addTable("address")
                .addTable("t_user", true)
                .allTable(table -> {
                    table.setColumn("gmt_created", "gmt_modified", "is_deleted")
                        .column("is_deleted", ColumnType.BOOLEAN)
                        .addBaseDaoInterface("MyCustomerInterface<${entity}, ${query}, ${update}>", MyCustomerInterface.class.getName())
                    ;
                })
            )
            .tables(config -> config
                .addTable("no_auto_id")
                .addTable("no_primary")
                .allTable(table -> table.setMapperPrefix("new"))
            )
            .execute();
    }
}