package cn.org.atool.fluent.generator;

import cn.org.atool.fluent.mybatis.customize.ICustomizedMapper;
import cn.org.atool.fluent.mybatis.customize.MyCustomerInterface;
import cn.org.atool.fluent.mybatis.customize.MyEntity;
import cn.org.atool.fluent.mybatis.customize.model.MyEnum;
import cn.org.atool.generator.FileGenerator;
import cn.org.atool.generator.annotation.*;
import org.apache.ibatis.type.BlobTypeHandler;
import org.apache.ibatis.type.EnumOrdinalTypeHandler;
import org.apache.ibatis.type.EnumTypeHandler;
import org.apache.ibatis.type.LongTypeHandler;
import org.test4j.module.database.proxy.DataSourceCreator;

public class FluentMyBatisGeneratorMain {
    static final String URL = "jdbc:mysql://localhost:3306/fluent_mybatis?useUnicode=true&characterEncoding=utf8";

    static final String SrcDir = "fluent-mybatis-test/src/main/java";

    static final String TestDir = "fluent-mybatis-test/src/test/java";

    static final String BasePack = "cn.org.atool.fluent.mybatis.generator.shared";

    /**
     * 使用main函数，是避免全量跑test时，误执行生成代码
     *
     * @param args command line args
     */
    public static void main(String[] args) {
        DataSourceCreator.create("dataSource");
        FileGenerator.build(
            MapperPrefix_Version.class,
            RelationDef1.class,
            RelationDef2.class,
            EntitySuffix_TypeHandler_CustomizedMapper.class,
            UpdateDefault.class
        );
    }

    @Tables(url = URL, username = "root", password = "password",
        srcDir = SrcDir, daoDir = SrcDir, testDir = TestDir, basePack = BasePack + 1,
        gmtCreated = "gmt_created", gmtModified = "gmt_modified", logicDeleted = "is_deleted",
        tables = {
            @Table(value = "no_auto_id", mapperPrefix = "new",
                seqName = "SELECT LAST_INSERT_ID() AS ID", version = "lock_version"),
            @Table(value = "no_primary", mapperPrefix = "new")
        }, alphabetOrder = false)
    static class MapperPrefix_Version {
    }

    @Tables(url = URL, username = "root", password = "password",
        srcDir = SrcDir, daoDir = SrcDir, testDir = TestDir, basePack = BasePack + 2,
        gmtCreated = "gmt_created", gmtModified = "gmt_modified", logicDeleted = "is_deleted",
        tables = {
            @Table(value = {"home_address", "student", "student_score"},
                tablePrefix = "t_", mapperPrefix = "my",
                defaults = MyCustomerInterface.class,
                entity = MyEntity.class,
                columns = @Column(value = "version", isLarge = true)
            ),
            @Table(value = {"teacher", "student_teacher_relation"})},
        relations = {
            @Relation(method = "findDeskMate", type = RelationType.OneWay_0_1,
                source = "student", target = "student", where = "id=desk_mate_id"),
            @Relation(source = "student", target = "student_score", type = RelationType.TwoWay_1_N,
                where = "id=student_id && env=env && is_deleted=is_deleted"),
            @Relation(method = "findEnglishScore", source = "student", target = "student_score", type = RelationType.OneWay_0_1),
            @Relation(source = "student", target = "teacher", type = RelationType.TwoWay_N_N)
        })
    static class RelationDef1 {
    }

    @Tables(
        /* 数据库连接信息 **/
        url = URL, username = "root", password = "password",
        /* Entity类parent package路径 **/
        srcDir = SrcDir, daoDir = SrcDir, testDir = TestDir, basePack = BasePack + 3,
        /* 如果表定义记录创建，记录修改，逻辑删除字段 **/
        gmtCreated = "gmt_created", gmtModified = "gmt_modified", logicDeleted = "is_deleted",
        /* 需要生成文件的表 ( 表名称:对应的Entity名称 ) **/
        tables = @Table(value = {"t_member", "t_member_love", "t_member_favorite"}, tablePrefix = "t_"),
        relations = {
            @Relation(method = "findMyFavorite", source = "t_member", target = "t_member_favorite", type = RelationType.OneWay_0_N
                , where = "id=member_id && is_deleted=is_deleted"),
            @Relation(method = "findExFriends", source = "t_member", target = "t_member", type = RelationType.OneWay_0_N),
            @Relation(method = "findCurrFriend", type = RelationType.OneWay_0_1,
                source = "t_member", target = "t_member"
            )
        }
    )
    static class RelationDef2 {
    }

    @Tables(url = URL, username = "root", password = "password",
        srcDir = SrcDir, daoDir = SrcDir, testDir = TestDir, basePack = BasePack + 4,
        tables = {
            @Table(value = "blob_value",
                columns = @Column(value = "blob_value", typeHandler = BlobTypeHandler.class),
                superMapper = ICustomizedMapper.class),
            @Table(value = "my_enum_type",
                columns = {
                    @Column(value = "enum_string", javaType = MyEnum.class, typeHandler = EnumTypeHandler.class),
                    @Column(value = "enum-num", javaType = MyEnum.class, typeHandler = EnumOrdinalTypeHandler.class),
                    @Column(value = "id", typeHandler = LongTypeHandler.class)
                }, logicDeleted = "is_deleted", useDao = false)
        }, entitySuffix = "PoJo")
    static class EntitySuffix_TypeHandler_CustomizedMapper {
    }

    @Tables(url = URL, username = "root", password = "password",
        srcDir = SrcDir, daoDir = SrcDir, testDir = TestDir, basePack = BasePack + 5,
        tables = {
            @Table(value = "idcard",
                columns = {@Column(value = "is_deleted", javaType = Long.class)},
                seqName = "SELECT NEXTVAL('testSeq')")
        }, logicDeleted = "is_deleted", version = "version",
        useCached = true, isRichEntity = false)
    static class UpdateDefault {
    }
}