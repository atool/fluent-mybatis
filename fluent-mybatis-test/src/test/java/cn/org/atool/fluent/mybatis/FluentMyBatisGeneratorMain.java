package cn.org.atool.fluent.mybatis;

import cn.org.atool.fluent.mybatis.customize.IBaseEntity;
import cn.org.atool.fluent.mybatis.customize.MyCustomerInterface;
import cn.org.atool.generator.FileGenerator;
import cn.org.atool.generator.annotation.Column;
import cn.org.atool.generator.annotation.Table;
import cn.org.atool.generator.annotation.Tables;

public class FluentMyBatisGeneratorMain {
    public static final String URL = "jdbc:mysql://localhost:3306/fluent_mybatis?useUnicode=true&characterEncoding=utf8";

    /**
     * 使用main函数，是避免全量跑test时，误执行生成代码
     *
     * @param args
     */
    public static void main(String[] args) {
        FileGenerator.build(Abc.class);
    }

    @Tables(url = URL, username = "root", password = "password",
        srcDir = "fluent-mybatis-test/src/main/java",
        testDir = "fluent-mybatis-test/src/test/java",
        basePack = "cn.org.atool.fluent.mybatis.generate",
        gmtCreated = "gmt_created", gmtModified = "gmt_modified", logicDeleted = "is_deleted",
        tables = {
            @Table(value = {"home_address", "t_student", "student_score"},
                tablePrefix = "t_", mapperPrefix = "my",
                defaults = MyCustomerInterface.class,
                entity = IBaseEntity.class,
                columns = @Column(value = "version", isLarge = true)
            ),
            @Table(value = "no_auto_id", mapperPrefix = "new", seqName = "SELECT LAST_INSERT_ID() AS ID"),
            @Table(value = "no_primary", mapperPrefix = "new")})
    static class Abc {
    }
}
