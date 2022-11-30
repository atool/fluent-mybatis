package cn.org.atool.fluent.mybatis.test.method;

import cn.org.atool.fluent.mybatis.generator.shared2.Ref;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generator.shared2.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentUpdate;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

@SuppressWarnings("unchecked")
public class UpdateByEntity extends BaseTest {
    @Autowired
    StudentMapper mapper;

    @Test
    void byEntity() {
        StudentEntity student = new StudentEntity()
            .setId(1L)
            .setUserName("test")
            .setAddress("test");
        mapper.updateBy(mapper.emptyUpdater()
            .set.byEntity(student).end()
            .where.id().eq(1).end()
        );
        db.sqlList().wantFirstSql().eq("" +
            "UPDATE fluent_mybatis.student " +
            "SET `gmt_modified` = now(), `address` = ?, `user_name` = ? " +
            "WHERE `id` = ?");
        db.sqlList().wantFirstPara().eqList("test", "test", 1);
    }

    @DisplayName("按Entity指定字段列表更新")
    @Test
    void byEntity_spec() {
        StudentEntity student = new StudentEntity()
            .setId(1L)
            .setUserName("test")
            .setAddress("test");

        StudentUpdate updater = mapper.emptyUpdater()
            .set.byEntity(student, Ref.Field.Student.userName, Ref.Field.Student.grade).end()
            .where.id().eq(1).end();
        mapper.updateBy(updater);

        db.sqlList().wantFirstSql().eq("" +
            "UPDATE fluent_mybatis.student " +
            "SET `gmt_modified` = now(), `user_name` = ?, `grade` = ? " +
            "WHERE `id` = ?");
        db.sqlList().wantFirstPara().eqList("test", null, 1);
    }

    @Test
    void byEntity_Getter() {
        StudentEntity student = new StudentEntity()
            .setId(1L)
            .setUserName("test")
            .setAddress("test");

        StudentUpdate updater = mapper.emptyUpdater()
            .set.byEntity(student, StudentEntity::getUserName, StudentEntity::getGrade).end()
            .where.id().eq(1).end();
        mapper.updateBy(updater);

        db.sqlList().wantFirstSql().eq("" +
            "UPDATE fluent_mybatis.student " +
            "SET `gmt_modified` = now(), `user_name` = ?, `grade` = ? " +
            "WHERE `id` = ?");
        db.sqlList().wantFirstPara().eqList("test", null, 1);
    }

    @DisplayName("按Entity排除字段更新, 未指定排除")
    @Test
    void byEntity_Exclude() {
        StudentEntity student = new StudentEntity()
            .setId(1L)
            .setUserName("test")
            .setAddress("test")
            .setTenant(122L);

        StudentUpdate updater = mapper.emptyUpdater()
            .set.byExclude(student).end()
            .where.id().eq(1).end();
        mapper.updateBy(updater);

        db.sqlList().wantFirstSql()
            .contains("`user_name` = ?,")
            .contains("`gmt_modified` = ?,")
            .contains("`age` = ?,")
            .contains("`birthday` = ?,")
            .notContain(", `id` = ?,")
            .notContain("`gmt_modified` = now(),")
            .end("WHERE `id` = ?")
        ;
    }

    @DisplayName("按Entity排除字段更新")
    @Test
    void byEntity_ExcludeSpec() {
        StudentEntity student = new StudentEntity()
            .setId(1L)
            .setUserName("test")
            .setAddress("test");

        StudentUpdate updater = mapper.emptyUpdater()
            .set.byExclude(student,
                Ref.Field.Student.id,
                Ref.Field.Student.address,
                Ref.Field.Student.tenant,
                Ref.Field.Student.birthday).end()
            .where.id().eq(1).end();
        mapper.updateBy(updater);

        db.sqlList().wantFirstSql()
            .contains("`user_name` = ?,")
            .contains("`gmt_modified` = ?,")
            .notContain(", `id` = ?,")
            .notContain("`address` = ?,")
            .notContain("`birthday` = ?,")
            .end("WHERE `id` = ?")
        ;
    }

    @DisplayName("按BiPredicate结果更新")
    @Test
    void byEntity_BiPredicate() {
        StudentEntity student = new StudentEntity()
            .setId(1L)
            .setUserName("test")
            .setAddress("test");
        StudentUpdate updater = mapper.emptyUpdater()
            .set.byEntity(student, (k, v) -> v != null && !Arrays.asList("address", "tenant").contains(k)).end()
            .where.eqByEntity(student, (k, v) -> "id".equals(k)).end();
        mapper.updateBy(updater);

        db.sqlList().wantFirstSql()
            .contains("`gmt_modified` = now(),")
            .notContain(", `id` = ?,")
            .notContain("`address` = ?,")
            .notContain("`birthday` = ?,")
            .end("WHERE `id` = ?")
        ;
    }

    @DisplayName("按Entity排除字段更新")
    @Test
    void byEntity_ExcludeByGetter() {
        StudentEntity student = new StudentEntity()
            .setUserName("test")
            .setTenant(124L)
            .setAddress("test");

        StudentUpdate updater = mapper.emptyUpdater()
            .set.byExclude(student,
                StudentEntity::getAddress,
                StudentEntity::getBirthday,
                StudentEntity::getGmtModified).end()
            .where.id().eq(1).end();
        mapper.updateBy(updater);

        db.sqlList().wantFirstSql()
            .contains("`user_name` = ?,")
            .contains("`gmt_modified` = now(),")
            .notContain(", `id` = ?,")
            .notContain("`address` = ?,")
            .notContain("`birthday` = ?,")
            .end("WHERE `id` = ?")
        ;
    }
}
