package cn.org.atool.fluent.mybatis.test.entity;

import cn.org.atool.fluent.form.Form;
import cn.org.atool.fluent.mybatis.generator.ATM;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.model.StdPagedList;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

@SuppressWarnings({"rawtypes"})
public class FormDemo extends BaseTest {
    @Test
    public void formDemo1() {
        ATM.dataMap.student.table().clean();
        StudentEntity student = this.newStudent();

        Form form = Form.with(student, apply -> apply
            .eq("userName")
            .eq("age")
        );
        if (form.count() > 0) {
            throw new RuntimeException("出BUG了!");
        }
        student.save();
        want.bool(form.count() > 0).is(true);
        Stream.of(new Object[10]).forEach(o -> student.setId(null).save());

        StdPagedList<StudentEntity> list = form
            .setPage(0, 10)
            .stdPage();

        want.list(list.getData()).eqDataMap(ATM.dataMap.student.entity(10)
            .userName.values("I am FluentMybatis")
        );
        ATM.dataMap.student.countEq(11);
    }

    private StudentEntity newStudent() {
        return new StudentEntity()
            .setEnv("test_env")
            .setIsDeleted(false)
            .setUserName("I am FluentMybatis")
            .setAge(2)
            .setAddress("宇宙深处");
    }

    @Test
    public void formDemo2() {
        ATM.dataMap.student.table().clean();
        // 新增表单
        StudentEntity student = this.newStudent();

        List students = Form.with(student, apply -> apply
            .eq(StudentEntity::getUserName)
            .like(StudentEntity::getAddress)
        ).list();
        want.list(students).sizeEq(0);

        Form form = Form.with(student, apply -> apply
            .eq("userName")
            .eq("age")
        );
        if (form.count() > 0) {
            throw new RuntimeException("出BUG了!");
        }
        student.save();
        want.number(form.count()).eq(1);
        Stream.of(new Object[10]).forEach(o -> student.setId(null).save());

        StdPagedList<StudentEntity> list = form.setPage(0, 5).stdPage();

        want.list(list.getData()).eqDataMap(ATM.dataMap.student.entity(5)
            .userName.values("I am FluentMybatis")
        );
        ATM.dataMap.student.countEq(11);
    }

    @Test
    void formUpdate() {
        ATM.dataMap.student.table().clean();
        // 新增表单
        StudentEntity student = this.newStudent().setId(12L);
        Form.with(student, apply -> apply
            .and(StudentEntity::getUserName)
            .and("address")
            .eq("id")
        ).update();
        db.sqlList().wantFirstSql().eq("" +
            "UPDATE fluent_mybatis.student " +
            "SET `gmt_modified` = now(), " +
            "`address` = ?, " +
            "`user_name` = ? " +
            "WHERE `is_deleted` = ? " +
            "AND `env` = ? " +
            "AND `id` = ?");
        db.sqlList().wantFirstPara().eqList("宇宙深处", "I am FluentMybatis", false, "test_env", 12L);
    }

    @Test
    void insert() {
        ATM.dataMap.student.table().clean();

        new Form(StudentEntity.class)
            .set(StudentEntity::getUserName, "I am FluentMybatis")
            .set("address", "宇宙深处")
            .insert();
        db.sqlList().wantFirstSql().eq("" +
            "INSERT INTO fluent_mybatis.student (`address`, `env`, `tenant`, `user_name`, `gmt_created`, `gmt_modified`, `is_deleted`) " +
            "VALUES (?, ?, ?, ?, now(), now(), 0)");
        db.sqlList().wantFirstPara().eqList("宇宙深处", "test_env", 234567L, "I am FluentMybatis");
    }
}
