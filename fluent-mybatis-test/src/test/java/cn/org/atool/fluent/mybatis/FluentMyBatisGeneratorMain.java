package cn.org.atool.fluent.mybatis;

import cn.org.atool.fluent.mybatis.customize.IBaseEntity;
import cn.org.atool.fluent.mybatis.customize.MyCustomerInterface;
import cn.org.atool.generator.FileGenerator;
import cn.org.atool.generator.annotation.Column;
import cn.org.atool.generator.annotation.Table;
import cn.org.atool.generator.annotation.Tables;

public class FluentMyBatisGeneratorMain {
    public static final String URL = "jdbc:mysql://localhost:3306/fluent_mybatis?useUnicode=true&characterEncoding=utf8";

    public static final Class dao_interface = MyCustomerInterface.class;

    /**
     * 使用main函数，是避免全量跑test时，误执行生成代码
     *
     * @param args
     */
    public static void main(String[] args) {
        FileGenerator.build(Abc.class);
//        generate();
    }

    @Tables(url = URL, username = "root", password = "password",
        srcDir = "fluent-mybatis-test/src/main/java",
        daoDir = "fluent-mybatis-test/src/main/java",
        testDir = "fluent-mybatis-test/src/test/java",
        basePack = "cn.org.atool.fluent.mybatis.generate",
        gmtCreated = "gmt_created", gmtModified = "gmt_modified", logicDeleted = "is_deleted",
        tables = {
            @Table(value = {"address", "t_user", "student_score"},
                tablePrefix = "t_", mapperPrefix = "my",
                dao = MyCustomerInterface.class,
                entity = IBaseEntity.class,
                columns = @Column(value = "version", isLarge = true)
            ),
            @Table(value = "no_auto_id", mapperPrefix = "new", seqName = "SELECT LAST_INSERT_ID() AS ID"),
            @Table(value = "no_primary", mapperPrefix = "new")})
    static class Abc {
    }


    private static void generate() {
        String outputDir = System.getProperty("user.dir") + "/fluent-mybatis-test/src/main/java";
        FileGenerator.build(true, false)
            .globalConfig(config -> config.setOutputDir(outputDir)
                .setDataSource(URL, "root", "password")
                .setBasePackage("cn.org.atool.fluent.mybatis.generate.entity")
                .setDaoPackage("cn.org.atool.fluent.mybatis.generate.dao"))
            .tables(config -> config
                .table("address")
                .table("t_user", t -> t.enablePartition()
                    .setColumn("version", f -> f.setLarge()))
                .foreach(t -> t
                    .setColumn("gmt_created", "gmt_modified", "is_deleted")
                    .addBaseDaoInterface(dao_interface)
                    .setTablePrefix("t_")
                    .setMapperPrefix("my")
                    .addEntityInterface(IBaseEntity.class)
                )
            )
            .tables(config -> config
                .table("no_auto_id", t -> t.setSeqName("SELECT LAST_INSERT_ID() AS ID"))
                .table("no_primary")
                .foreach(t -> t
                    .setMapperPrefix("new")
                )
            )
            .execute();
    }
}
