package cn.org.atool.fluent.mybatis;

import cn.org.atool.fluent.mybatis.customize.IBaseEntity;
import cn.org.atool.fluent.mybatis.customize.MyCustomerInterface;
import cn.org.atool.generator.FileGenerator;
import cn.org.atool.generator.annotation.*;

public class FluentMyBatisGeneratorMain {
    static final String URL = "jdbc:mysql://localhost:3306/fluent_mybatis?useUnicode=true&characterEncoding=utf8";

    static final String SrcDir = "fluent-mybatis-test/src/main/java";

    static final String TestDir = "fluent-mybatis-test/src/test/java";

    static final String BasePack = "cn.org.atool.fluent.mybatis.generate";

    /**
     * 使用main函数，是避免全量跑test时，误执行生成代码
     *
     * @param args
     */
    public static void main(String[] args) {
        FileGenerator.build(Empty1.class, Empty2.class);
    }

    @Tables(url = URL, username = "root", password = "password",
        srcDir = SrcDir, testDir = TestDir, basePack = BasePack,
        gmtCreated = "gmt_created", gmtModified = "gmt_modified", logicDeleted = "is_deleted",
        tables = {
            @Table(value = "no_auto_id", mapperPrefix = "new", seqName = "SELECT LAST_INSERT_ID() AS ID"),
            @Table(value = "no_primary", mapperPrefix = "new")
        })
    static class Empty1 {
    }

    @Tables(url = URL, username = "root", password = "password",
        srcDir = SrcDir, testDir = TestDir, basePack = BasePack,
        gmtCreated = "gmt_created", gmtModified = "gmt_modified", logicDeleted = "is_deleted",
        tables = {
            @Table(value = {"home_address", "t_student", "student_score"},
                tablePrefix = "t_", mapperPrefix = "my",
                defaults = MyCustomerInterface.class,
                entity = IBaseEntity.class,
                columns = @Column(value = "version", isLarge = true)
            )},
        relations = {
            @Relation(source = "t_student", target = "student_score", type = RelationType.TwoWay_1_N,
                where = "id=student_id && env=env && is_deleted=is_deleted"),
            @Relation(method = "findEnglishScore", source = "t_student", target = "student_score", type = RelationType.OneWay_0_1)
        })
    static class Empty2 {
    }
}