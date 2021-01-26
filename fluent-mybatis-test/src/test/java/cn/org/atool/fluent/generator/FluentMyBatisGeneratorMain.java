package cn.org.atool.fluent.generator;

import cn.org.atool.fluent.mybatis.customize.MyCustomerInterface;
import cn.org.atool.fluent.mybatis.customize.MyEntity;
import cn.org.atool.generator.FileGenerator;
import cn.org.atool.generator.annotation.*;
import org.test4j.module.database.proxy.DataSourceCreatorFactory;

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
        DataSourceCreatorFactory.create("dataSource");
        FileGenerator.build(
            Empty1.class,
            Empty2.class,
            Empty3.class
        );
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
            @Table(value = {"home_address", "student", "student_score"},
                tablePrefix = "t_", mapperPrefix = "my",
                defaults = MyCustomerInterface.class,
                entity = MyEntity.class,
                columns = @Column(value = "version", isLarge = true)
            )},
        relations = {
            @Relation(source = "student", target = "student_score", type = RelationType.TwoWay_1_N,
                where = "id=student_id && env=env && is_deleted=is_deleted"),
            @Relation(method = "findEnglishScore", source = "student", target = "student_score", type = RelationType.OneWay_0_1)
        })
    static class Empty2 {
    }

    @Tables(
        /** 数据库连接信息 **/
        url = URL, username = "root", password = "password",
        /** Entity类parent package路径 **/
        srcDir = SrcDir, testDir = TestDir, basePack = BasePack,
        /** 如果表定义记录创建，记录修改，逻辑删除字段 **/
        gmtCreated = "gmt_create", gmtModified = "gmt_modified", logicDeleted = "is_deleted",
        /** 需要生成文件的表 ( 表名称:对应的Entity名称 ) **/
        tables = @Table(value = {"t_member", "t_member_love", "t_member_favorite"}, tablePrefix = "t_"),
        relations = {
            @Relation(method = "findMyFavorite", source = "t_member", target = "t_member_favorite", type = RelationType.OneWay_0_N
                , where = "id=member_id && is_deleted=is_deleted"),
            @Relation(method = "findExFriends", source = "t_member", target = "t_member", type = RelationType.OneWay_0_N),
            @Relation(method = "findCurrFriend", source = "t_member", target = "t_member", type = RelationType.OneWay_0_1)
        }
    )
    static class Empty3 {
    }
}
