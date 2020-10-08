package cn.org.atool.fluent.mybatis;

import cn.org.atool.fluent.mybatis.customize.IBaseEntity;
import cn.org.atool.fluent.mybatis.customize.MyCustomerInterface;
import cn.org.atool.fluent.mybatis.generator.EntityGenerator;
import cn.org.atool.fluent.mybatis.generator.generator.EntityAnnotationGenerator;
import cn.org.atool.fluent.mybatis.generator.generator.EntityApiGenerator;
import cn.org.atool.fluent.mybatis.generator.annoatation.Column;
import cn.org.atool.fluent.mybatis.generator.annoatation.Table;
import cn.org.atool.fluent.mybatis.generator.annoatation.Tables;

public class FluentMyBatisGeneratorMain {
    public static final String URL = "jdbc:mysql://localhost:3306/fluent_mybatis?useUnicode=true&characterEncoding=utf8";

    public static final Class dao_interface = MyCustomerInterface.class;

    /**
     * 使用main函数，是避免全量跑test时，误执行生成代码
     *
     * @param args
     */
    public static void main(String[] args) {
        EntityGenerator.byAnnotation(GenerateInfo.class);
//        generate();
    }

    private static void generate() {
        String outputDir = System.getProperty("user.dir") + "/fluent-mybatis-test/src/main/java";
        EntityGenerator.build()
            .globalConfig(config -> config.setOutputDir(outputDir)
                .setDataSource(URL, "root", "password")
                .setBasePackage("cn.org.atool.fluent.mybatis.generate.entity"))
            .tables(config -> config
                .table("address")
                .table("t_user", t -> t.enablePartition()
                    .setColumn("version", f -> f.setLarge()))
                .foreach(t -> t
                    .setColumn("gmt_created", "gmt_modified", "is_deleted")
                    .addBaseDaoInterface(dao_interface)
                    .setTablePrefix("t_")
                    .addEntityInterface(IBaseEntity.class)
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

    @Tables(url = URL, username = "root", password = "password",
        srcDir = "fluent-mybatis-test/src/main/java",
        entityPack = "cn.org.atool.fluent.mybatis.generate.entity",
        daoPack = "cn.org.atool.fluent.mybatis.generate.dao",
        tables = {
            @Table(value = {"address", "t_user"},
                tablePrefix = "t_",
                gmtCreated = "gmt_created",
                gmtModified = "gmt_modified",
                logicDeleted = "is_deleted",
                mapperPrefix = "my",
                daoInterface = MyCustomerInterface.class,
                entityInterface = IBaseEntity.class,
                columns = @Column(value = "version", isLarge = true)
            ),
            @Table(value = "no_auto_id", mapperPrefix = "new", seqName = "SELECT LAST_INSERT_ID() AS ID"),
            @Table(value = "no_primary", mapperPrefix = "new")})
    public static class GenerateInfo {
    }
}