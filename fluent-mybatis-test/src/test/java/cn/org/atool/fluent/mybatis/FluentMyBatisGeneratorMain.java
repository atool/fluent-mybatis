package cn.org.atool.fluent.mybatis;

import cn.org.atool.fluent.mybatis.customize.MyCustomerInterface;
import cn.org.atool.fluent.mybatis.generator.EntityGenerator2;


public class FluentMyBatisGeneratorMain {
    public static final String URL = "jdbc:mysql://localhost:3306/fluent_mybatis?useUnicode=true&characterEncoding=utf8";

    public static final String dao_interface = MyCustomerInterface.class.getName();

    /**
     * 使用main函数，是避免全量跑test时，误执行生成代码
     *
     * @param args
     */
    public static void main(String[] args) {
        String outputDir = System.getProperty("user.dir") + "/fluent-mybatis-test/src/main/java";
        EntityGenerator2.build()
            .globalConfig(config -> config.setOutputDir(outputDir)
                .setDataSource(URL, "root", "password")
                .setBasePackage("cn.org.atool.fluent.mybatis.generate.entity"))
            .tables(config -> config
                .table("address")
                .table("t_user", t -> t.enablePartition()
                    .setColumn("version", f -> f.setLarge()))
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