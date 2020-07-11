package cn.org.atool.fluent.mybatis.demo;

import cn.org.atool.fluent.mybatis.generator.MybatisGenerator;
import org.test4j.generator.mybatis.db.ColumnJavaType;

public class FluentMyBatisGeneratorMain {
    static String url = "jdbc:mysql://localhost:3306/fluent_mybatis?useUnicode=true&characterEncoding=utf8";

    static String dao_interface = "cn.org.atool.fluent.mybatis.demo.MyCustomerInterface";

    /**
     * 使用main函数，是避免全量跑test时，误执行生成代码
     *
     * @param args
     */
    public static void main(String[] args) {
        String outputDir = System.getProperty("user.dir") + "/fluent-mybatis/src/test/java";
        MybatisGenerator.build()
            .globalConfig(config -> config.setOutputDir(outputDir)
                .setDataSource(url, "root", "password")
                .setBasePackage("cn.org.atool.fluent.mybatis.demo.generate"))
            .tables(config -> config
                .table("address")
                .table("t_user", t -> t.enablePartition()
                    .setColumn("version", f->f.setLarge()))
                .foreach(t -> t
                    .setColumn("gmt_created", "gmt_modified", "is_deleted")
                    .addBaseDaoInterface(dao_interface, "${entity}")
                    .setTablePrefix("t_")
                    .addEntityInterface("cn.org.atool.fluent.mybatis.demo.IBaseEntity", "${entity}")
                )
            )
            .tables(config -> config
                .table("no_auto_id", t -> t.setSeqName("test"))
                .table("no_primary")
                .foreach(t -> t
                    .setMapperPrefix("new")
                )
            )
            .execute();
    }
}