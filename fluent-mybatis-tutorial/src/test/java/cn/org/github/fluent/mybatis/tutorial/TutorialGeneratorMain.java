package cn.org.github.fluent.mybatis.tutorial;

import cn.org.atool.fluent.mybatis.generator.MybatisGenerator;
import org.test4j.generator.mybatis.db.ColumnJavaType;

public class TutorialGeneratorMain {
    static String url = "jdbc:mysql://localhost:3306/fluent_mybatis_tutorial?useUnicode=true&characterEncoding=utf8";

    static String srcDir = System.getProperty("user.dir") + "/fluent-mybatis-tutorial/src/";

    static String mainJavaDir = srcDir + "main/java";

    static String testJavaDir = srcDir + "test/java";

    public static void main(String[] args) {
        MybatisGenerator.build()
            .globalConfig(config -> config.setOutputDir(mainJavaDir, testJavaDir, mainJavaDir)
                .setDataSource(url, "root", "password")
                .setBasePackage("cn.org.atool.fluent.mybatis.tutorial"))
            .tables(config -> config
                .table("user")
                .table("receiving_address")
                .foreach(t -> t
                    .setColumn("gmt_created", "gmt_modified", "is_deleted")
                    .setColumnType("is_deleted", ColumnJavaType.BOOLEAN)
                )
            )
            .execute();
    }
}
