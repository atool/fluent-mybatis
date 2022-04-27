package cn.org.atool.fluent.mybatis.test.where.ifs;

import cn.org.atool.fluent.mybatis.If;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generator.shared2.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentUpdate;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("all")
public class IfsTest extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void test_multiIf() {
        long id = 2L;
        StudentUpdate update = StudentUpdate.emptyUpdater()
            .set.address().is("address 1", v -> id == 1)
            .set.address().is("address 2", v -> id == 2)
            .set.address().is("address 3", v -> id == 3)
            .end()
            .where.id().eq(id).end();

        mapper.updateBy(update);
        // 验证SQL语句
        db.sqlList().wantFirstSql().eq("" +
                "UPDATE fluent_mybatis.student " +
                "SET `gmt_modified` = now(), `address` = ? " +
                "WHERE `id` = ?",
            StringMode.SameAsSpace);
        // 验证参数
        db.sqlList().wantFirstPara().eqReflect(new Object[]{"address 2", 2L});
    }

    @Test
    public void test_Ifs() {
        long id = 2L;
        StudentUpdate update = StudentUpdate.emptyUpdater()
            .set.address().is(If.test()
                .when(v -> id == 1, "address 1")
                .when(v -> id == 2, "address 2")
                .when(v -> id == 3, "address 3"))
            .end()
            .where.id().eq(id).end();

        mapper.updateBy(update);
        // 验证SQL语句
        db.sqlList().wantFirstSql().eq("" +
                "UPDATE fluent_mybatis.student " +
                "SET `gmt_modified` = now(), `address` = ? " +
                "WHERE `id` = ?",
            StringMode.SameAsSpace);
        // 验证参数
        db.sqlList().wantFirstPara().eqReflect(new Object[]{"address 2", 2L});
    }

    @Test
    public void test_Ifs2() {
        long id = 2L;
        StudentUpdate update = StudentUpdate.emptyUpdater()
            .set.address().is(If.test()
                .when(v -> id == 1, "address 1")
                .when(v -> id == 2, "address 2")
                .other("address3")
            )
            .end()
            .where.id().eq(If.test()
                .when(v -> id == 1, 1)
                .when(v -> id == 2, 2))
            .end();

        mapper.updateBy(update);

        // 验证SQL语句
        db.sqlList().wantFirstSql().eq("" +
                "UPDATE fluent_mybatis.student " +
                "SET `gmt_modified` = now(), `address` = ? " +
                "WHERE `id` = ?",
            StringMode.SameAsSpace);
        // 验证参数
        db.sqlList().wantFirstPara().eqReflect(new Object[]{"address 2", 2L});
    }

    @Test
    public void test_InIfs() {
        int[] ids = {2, 3};
        StudentUpdate update = StudentUpdate.emptyUpdater()
            .set.address().is("address")
            .end()
            .where.id().in(If.testIn()
                .when(list -> list.contains(1), ids)
                .when(list -> list.contains(2), ids)
                .other(ids))
            .end();

        mapper.updateBy(update);

        // 验证SQL语句
        db.sqlList().wantFirstSql().eq("" +
                "UPDATE fluent_mybatis.student SET `gmt_modified` = now(), `address` = ? " +
                "WHERE `id` IN (?, ?)",
            StringMode.SameAsSpace);
        // 验证参数
        db.sqlList().wantFirstPara().eqReflect(new Object[]{"address", 2, 3});
    }


    @Test
    public void test_InIfs2() {
        List ids = Arrays.asList(2, 3);
        StudentUpdate update = StudentUpdate.emptyUpdater()
            .set.address().is("address")
            .end()
            .where.id().in(If.testIn()
                .when(list -> list.contains(1), ids)
                .when(list -> list.contains(2), ids)
                .other(ids))
            .end();

        mapper.updateBy(update);

        // 验证SQL语句
        db.sqlList().wantFirstSql().eq("" +
                "UPDATE fluent_mybatis.student " +
                "SET `gmt_modified` = now(), `address` = ? WHERE `id` IN (?, ?)",
            StringMode.SameAsSpace);
        // 验证参数
        db.sqlList().wantFirstPara().eqReflect(new Object[]{"address", 2, 3});
    }


    @Test
    public void test_object_is_null() {
        StudentEntity student = null;
        StudentUpdate update = StudentUpdate.updater()
            .set.address().is("address")
            .end()
            .where.id().eq(Optional.ofNullable(student).map(StudentEntity::getId).orElse(null), If::notNull)
            .end();

        mapper.updateBy(update);

        // 验证SQL语句
        db.sqlList().wantFirstSql().eq("" +
            "UPDATE fluent_mybatis.student " +
            "SET `gmt_modified` = now(), `address` = ? " +
            "WHERE `is_deleted` = ? AND `env` = ?");
    }

    @Test
    public void test_object_not_null() {
        StudentEntity student = new StudentEntity().setId(2L).setAge(30);
        StudentUpdate update = StudentUpdate.updater()
            .set.address().is("address")
            .end()
            .where.applyIf(student != null,
                c -> c.id().eq(student.getId())
                    .and.age().between(student.getAge() - 10, student.getAge() + 10)
                    .and.address().like(student.getAddress(), If::notBlank)
            )
            .end();

        mapper.updateBy(update);

        // 验证SQL语句
        db.sqlList().wantFirstSql().eq("" +
            "UPDATE fluent_mybatis.student " +
            "SET `gmt_modified` = now(), `address` = ? " +
            "WHERE `is_deleted` = ? AND `env` = ? " +
            "AND `id` = ? " +
            "AND `age` BETWEEN ? AND ?");
    }

    @Test
    public void test_object_is_null2() {
        StudentEntity student = null;
        StudentUpdate update = StudentUpdate.updater()
            .set.address().is("address")
            .end()
            .where.applyIf(student != null, c -> c.id().eq(student.getId()))
            .end();

        mapper.updateBy(update);

        // 验证SQL语句
        db.sqlList().wantFirstSql().eq("" +
            "UPDATE fluent_mybatis.student " +
            "SET `gmt_modified` = now(), `address` = ? " +
            "WHERE `is_deleted` = ? AND `env` = ?");
    }

    @Test
    public void test_condition_apply() {
        StudentEntity student = new StudentEntity().setId(2L);
        StudentUpdate update = StudentUpdate.updater()
            .set.address().is("address")
            .end()
            .where.applyIf(student != null, c ->
                c.id().between(student.getId(), student.getId() + 100)
            ).end();
        mapper.updateBy(update);

        // 验证SQL语句
        db.sqlList().wantFirstSql().eq("" +
            "UPDATE fluent_mybatis.student " +
            "SET `gmt_modified` = now(), `address` = ? " +
            "WHERE `is_deleted` = ? AND `env` = ? AND `id` BETWEEN ? AND ?");
    }
}
