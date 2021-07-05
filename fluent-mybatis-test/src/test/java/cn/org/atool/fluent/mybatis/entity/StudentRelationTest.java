package cn.org.atool.fluent.mybatis.entity;

import cn.org.atool.fluent.mybatis.generate.ATM;
import cn.org.atool.fluent.mybatis.generate.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generate.entity.TeacherEntity;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.test4j.hamcrest.matcher.string.StringMode;

public class StudentRelationTest extends BaseTest {
    @Test
    void test_findDeskMate() {
        // 准备2条数据, 学号1，2互为同桌
        ATM.dataMap.student.initTable(2)
            .id.values(1, 2)
            .deskMateId.values(2, 1)
            .userName.values("tom", "rose")
            .cleanAndInsert();

        StudentEntity deskMate = new StudentEntity().setId(1L).findDeskMate();
        // 验证执行的SQL语句
        db.sqlList().wantFirstSql().end("FROM student WHERE `desk_mate_id` = ?");
        db.sqlList().wantFirstPara().eqList(1L);
        // 验证返回的同桌数据
        want.object(deskMate).eqReflect(ATM.dataMap.student.entity(1)
            .id.values(2)
            .deskMateId.values(1)
            .userName.values("rose")
        );
    }

    @Test
    void test_findTeacherList() {
        new StudentEntity().setId(100L).findTeacherList();
        // 验证SQL语句
        db.sqlList().wantFirstSql().end("" +
            "FROM teacher " +
            "WHERE `id` IN " +
            "   (SELECT `teacher_id` " +
            "   FROM student_teacher_relation " +
            "   WHERE `student_id` = ?)", StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eqList(100L);
    }

    @Test
    void test_findStudentList() {
        new TeacherEntity().setId(200L).findStudentList();
        // 验证SQL语句
        db.sqlList().wantFirstSql().end("" +
            "FROM student " +
            "WHERE `is_deleted` = ? " +
            "AND `env` = ? " +
            "AND `id` IN " +
            "   (SELECT `student_id` " +
            "   FROM student_teacher_relation " +
            "   WHERE `teacher_id` = ?)", StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eqList(false, "test_env", 200L);
    }
}
