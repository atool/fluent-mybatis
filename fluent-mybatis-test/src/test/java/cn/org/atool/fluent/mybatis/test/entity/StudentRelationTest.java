package cn.org.atool.fluent.mybatis.test.entity;

import cn.org.atool.fluent.mybatis.generator.ATM;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.TeacherEntity;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.test4j.hamcrest.matcher.string.StringMode;

import java.util.List;

public class StudentRelationTest extends BaseTest {
    @Test
    void test_findDeskMate() {
        // 准备2条数据, 学号1，2互为同桌
        ATM.dataMap.student.initTable(2)
            .id.values(1, 2)
            .deskMateId.values(2, 1)
            .userName.values("tom", "rose")
            .env.values("test_env")
            .isDeleted.values(false)
            .cleanAndInsert();

        StudentEntity deskMate = new StudentEntity().setId(1L).findDeskMate();
        // 验证执行的SQL语句
        db.sqlList().wantFirstSql().end("FROM fluent_mybatis.student WHERE `is_deleted` = ? AND `env` = ? AND `desk_mate_id` = ?");
        db.sqlList().wantFirstPara().eqList(false, "test_env", 1L);
        // 验证返回的同桌数据
        want.object(deskMate).eqReflect(ATM.dataMap.student.entity(1)
            .id.values(2)
            .deskMateId.values(1)
            .userName.values("rose")
        );
    }

    @Test
    void test_findDeskMate2() {
        ATM.dataMap.student.initTable(4)
            .id.values(1, 2, 3, 4)
            .deskMateId.values(2, 1, 4, 3)
            .userName.values("tom", "rose")
            .env.values("test_env")
            .isDeleted.values(false)
            .cleanAndInsert();

        List<StudentEntity> students = new StudentQuery()
            .where.id().in(new int[]{1, 2, 3, 4}).end()
            .with(StudentEntity::findDeskMate).to().listEntity();

        // 验证执行的SQL语句
        db.sqlList().wantFirstSql().end("" +
            "FROM fluent_mybatis.student " +
            "WHERE `is_deleted` = ? AND `env` = ? AND `id` IN (?, ?, ?, ?)");
        db.sqlList().wantSql(1).end("" +
            "FROM fluent_mybatis.student " +
            "WHERE `is_deleted` = ? AND `env` = ? AND `desk_mate_id` IN (?, ?, ?, ?)");
        want.list(students).sizeEq(4);
        // 同桌的同桌是自己
        students.forEach(s -> want.object(s.getId()).eq(s.findDeskMate().getDeskMateId()));
        want.exception(() -> db.sqlList().wantSql(2), IndexOutOfBoundsException.class);
    }

    @Test
    void test_findTeacherList() {
        new StudentEntity().setId(100L).findTeacherList();
        // 验证SQL语句
        db.sqlList().wantFirstSql().end("" +
            "FROM `teacher` " +
            "WHERE `id` IN " +
            "   (SELECT `teacher_id` " +
            "   FROM `student_teacher_relation` " +
            "   WHERE `student_id` = ?)", StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eqList(100L);
    }

    @Test
    void findStudentScoreList2() {
        /* 准备2个学生数据 */
        ATM.dataMap.student.initTable(2)
            .userName.values("tom", "rose")
            .env.values("test_env")
            .cleanAndInsert();
        /* 给2个学生各准备2门成绩，共4条记录 */
        ATM.dataMap.studentScore.initTable(4)
            .studentId.values(1, 2, 1, 2)
            .subject.values("yuwen", "yuwen", "english")
            .score.values(80, 81, 67, 87)
            .env.values("test_env")
            .cleanAndInsert();
        // 查询学生列表，同时关联查询成绩单
        List<StudentEntity> students = new StudentQuery()
            .where.id().in(new int[]{1, 2}).end()
            .with(StudentEntity::findStudentScoreList) // 同时查询学生成绩列表 1:N 操作
            .to().listEntity();

        // 验证SQL语句
        db.sqlList().wantFirstSql().end("" +
            "FROM fluent_mybatis.student " +
            "WHERE `is_deleted` = ? AND `env` = ? AND `id` IN (?, ?)", StringMode.SameAsSpace);
        db.sqlList().wantSql(1).end("" +
            "FROM `student_score` " +
            "WHERE `is_deleted` = ? AND `env` = ? AND `student_id` IN (?, ?) " +
            "AND `is_deleted` = ? AND `env` = ?");
        want.list(students.get(0).findStudentScoreList())
            .eqMap(ATM.dataMap.studentScore.entity(2)
                .studentId.values(1)
                .score.values(80, 67)
            );
        want.list(students.get(1).findStudentScoreList())
            .eqMap(ATM.dataMap.studentScore.entity(2)
                .studentId.values(2)
                .score.values(81, 87)
            );
    }

    @Test
    void test_findStudentList() {
        new TeacherEntity().setId(200L).findStudentList();
        // 验证SQL语句
        db.sqlList().wantFirstSql().end("" +
            "FROM fluent_mybatis.student " +
            "WHERE `is_deleted` = ? " +
            "AND `env` = ? " +
            "AND `id` IN " +
            "   (SELECT `student_id` " +
            "   FROM `student_teacher_relation` " +
            "   WHERE `teacher_id` = ?)", StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eqList(false, "test_env", 200L);
    }
}
