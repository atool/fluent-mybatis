package cn.org.atool.fluent.mybatis.test.formservice;

import cn.org.atool.fluent.mybatis.formservice.model.*;
import cn.org.atool.fluent.mybatis.formservice.service.StudentService;
import cn.org.atool.fluent.mybatis.generator.ATM;
import cn.org.atool.fluent.mybatis.model.StdPagedList;
import cn.org.atool.fluent.mybatis.model.TagPagedList;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.modes.EqMode;
import org.test4j.tools.datagen.DataMap;

import java.util.List;

@SuppressWarnings({"rawtypes"})
public class FormServiceTest extends BaseTest {
    @Autowired
    StudentService service;

    @Test
    void listEntity() {
        ATM.dataMap.student.table(2)
            .env.values("test_env")
            .userName.values("ming.li")
            .age.values(23, 34)
            .email.values("xxx@test")
            .address.values("hangzhou binjiang")
            .deskMateId.values(2, 1)
            .cleanAndInsert();
        ATM.dataMap.studentScore.table(3)
            .env.values("test_env")
            .studentId.values(1, 1, 2)
            .subject.values("yuwen", "english")
            .score.values(79, 67, 98)
            .cleanAndInsert();
        List<Student> students = service.listStudentBy(new StudentQuery()
            .setUserName("ming.li")
            .setUserName2("")/* 验证string为blank的场景 */
            .setAddress("hangzhou")
            .setAge(new Integer[]{20, 40})
        );
        want.object(students).eqDataMap(
            ATM.dataMap.student.entity(2)
                .userName.values("ming.li")
                .age.values(34, 23)
                .address.values("hangzhou binjiang")
                .kv("hisEmail", "xxx@test")
        );
        /* 同桌的同桌 = 自己 */
        want.object(students.get(0)).eqReflect(students.get(1).getDeskMate(), EqMode.IGNORE_DEFAULTS);
        db.sqlList().wantFirstSql().end("" +
            "FROM fluent_mybatis.student " +
            "WHERE `is_deleted` = ? AND `env` = ? " +
            "AND (`user_name` = ? " +
            "AND `address` LIKE ? " +
            "AND `age` BETWEEN ? AND ?) " +
            "ORDER BY `user_name` ASC, `age` DESC");
        db.sqlList().wantSql(1).end("" +
            "FROM fluent_mybatis.student " +
            "WHERE `is_deleted` = ? AND `env` = ? AND `desk_mate_id` IN (?, ?)");

        /* 验证成绩列表 */
        want.object(students.get(0).getScores()).eqDataMap(new DataMap(1)
            .kv("score", 98)
            .kv("subject", "english"));
        want.object(students.get(1).getScores()).eqDataMap(new DataMap(2)
            .kv("score", 79, 67)
            .kv("subject", "yuwen", "english"));
        db.sqlList().wantSql(2).end("" +
            "FROM `student_score` " +
            "WHERE `is_deleted` = ? AND `env` = ? " +
            "AND `student_id` IN (?, ?) " +
            "AND `is_deleted` = ? AND `env` = ?");

        want.object(students.get(0).getEnglishScore()).notNull();
        want.object(students.get(1).getEnglishScore()).notNull();
        /* 第4条sql 和 第5条sql一样 */
        db.sqlList().wantSql(3).eq(db.sqlList().sql(4));
        want.exception(() -> db.sqlList().sql(5), IndexOutOfBoundsException.class);
    }

    @Test
    void stdPageEntity() {
        ATM.dataMap.student.table(2)
            .env.values("test_env")
            .userName.values("ming.li")
            .age.values(23, 34)
            .email.values("xxx@test")
            .address.values("hangzhou binjiang")
            .cleanAndInsert();
        StdPagedList<Student> students = service.stdPagedStudent((StdPagedQuery) new StdPagedQuery()
            .setUserName("ming.li")
            .setAddress("hangzhou")
            .setAge(new Integer[]{20, 40}));
        want.object(students.getData()).eqDataMap(
            ATM.dataMap.student.entity(2)
                .userName.values("ming.li")
                .age.values(34, 23)
                .address.values("hangzhou binjiang")
                .kv("hisEmail", "xxx@test")
        );
        want.number(students.getTotal()).isEqualTo(2);
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) " +
            "FROM fluent_mybatis.student " +
            "WHERE `is_deleted` = ? AND `env` = ? " +
            "AND (`user_name` = ? " +
            "AND `address` LIKE ? " +
            "AND `age` BETWEEN ? AND ?)");

        db.sqlList().wantSql(1).end("" +
            "FROM fluent_mybatis.student " +
            "WHERE `is_deleted` = ? AND `env` = ? " +
            "AND (`user_name` = ? " +
            "AND `address` LIKE ? " +
            "AND `age` BETWEEN ? AND ?) " +
            "ORDER BY `user_name` ASC, `age` DESC " +
            "LIMIT ?, ?");
    }

    @Test
    void tagPageEntity() {
        ATM.dataMap.student.table(4)
            .id.values(20L, 30L, 31L, 32L)
            .env.values("test_env")
            .userName.values("ming.li")
            .age.values(23, 34)
            .email.values("xxx@test")
            .address.values("hangzhou binjiang")
            .cleanAndInsert();
        TagPagedList<Student> students = service.tagPagedStudent((TagPagedQuery) new TagPagedQuery()
            .setNextId(23)
            .setPageSize(2)
            .setUserName("ming.li")
            .setAddress("hangzhou")
            .setAge(new Integer[]{20, 40}));
        want.object(students.getData()).eqDataMap(
            ATM.dataMap.student.entity(2)
                .userName.values("ming.li")
                .age.values(34)
                .address.values("hangzhou binjiang")
                .kv("hisEmail", "xxx@test")
        );
        want.number((Long) students.getNext()).eq(32L);
        db.sqlList().wantFirstSql().end("" +
            "FROM fluent_mybatis.student " +
            "WHERE `is_deleted` = ? AND `env` = ? " +
            "AND `id` >= ? " +
            "AND (`user_name` = ? " +
            "AND `address` LIKE ? " +
            "AND `age` BETWEEN ? AND ?) " +
            "ORDER BY `user_name` ASC, `age` DESC " +
            "LIMIT ?, ?");
    }

    @Test
    void findOneByUserName() {
        ATM.dataMap.student.table(2)
            .env.values("test_env")
            .userName.values("ming.li")
            .age.values(23, 34)
            .address.values("hangzhou binjiang")
            .cleanAndInsert();
        Student student = service.findByUserName("ming.li", new int[]{23, 40});
        want.object(student).eqDataMap(
            ATM.dataMap.student.entity(1)
                .userName.values("ming.li")
                .age.values(23)
                .address.values("hangzhou binjiang")
        );
        db.sqlList().wantFirstSql().end("" +
            "FROM fluent_mybatis.student " +
            "WHERE `is_deleted` = ? " +
            "AND `env` = ? " +
            "AND (`user_name` = ? " +
            "AND `age` IN (?, ?)) " +
            "LIMIT ?, ?");
    }


    @Test
    void findOneByUserName2() {
        ATM.dataMap.student.table(2)
            .env.values("test_env")
            .userName.values("ming.li")
            .age.values(23, 34)
            .address.values("hangzhou binjiang")
            .cleanAndInsert();
        Student student = service.findByUserName("ming.li", new StudentQuery().setAge(new Integer[]{20, 40}));
        want.object(student).eqDataMap(
            ATM.dataMap.student.entity(1)
                .userName.values("ming.li")
                .age.values(34)
                .address.values("hangzhou binjiang")
        );
        db.sqlList().wantFirstSql().end("" +
            "FROM fluent_mybatis.student " +
            "WHERE `is_deleted` = ? " +
            "AND `env` = ? " +
            "AND (`user_name` = ? " +
            "AND `age` BETWEEN ? AND ?) " +
            "ORDER BY `user_name` ASC, `age` DESC " +
            "LIMIT ?, ?");
    }

    @Test
    void findOne() {
        ATM.dataMap.student.table(2)
            .env.values("test_env")
            .userName.values("ming.li")
            .age.values(23, 34)
            .address.values("hangzhou binjiang")
            .cleanAndInsert();
        Student student = service.findStudent(new StudentQuery()
            .setUserName("ming.li")
            .setAddress("hangzhou")
            .setAge(new Integer[]{20, null}));
        want.object(student).eqDataMap(
            ATM.dataMap.student.entity(1)
                .userName.values("ming.li")
                .age.values(34)
                .address.values("hangzhou binjiang")
        );
        db.sqlList().wantFirstSql().end("" +
            "FROM fluent_mybatis.student " +
            "WHERE `is_deleted` = ? AND `env` = ? " +
            "AND (`user_name` = ? " +
            "AND `address` LIKE ? " +
            "AND `age` >= ?) " +
            "ORDER BY `user_name` ASC, `age` DESC " +
            "LIMIT ?, ?");
    }

    @Test
    void count() {
        ATM.dataMap.student.table(2)
            .env.values("test_env")
            .userName.values("ming.li")
            .age.values(23, 34)
            .address.values("hangzhou binjiang")
            .status.values("Enabled")
            .cleanAndInsert();
        long count = service.countStudentBy(new StudentQuery()
            .setUserName("ming.li")
            .setAddress("hangzhou")
            .setAge(new Integer[]{20, 40})
            .setStatus(StatusEnum.Enabled)
        );
        want.number(count).isEqualTo(2);
        db.sqlList().wantFirstSql().eq("" +
            "SELECT COUNT(*) " +
            "FROM fluent_mybatis.student " +
            "WHERE `is_deleted` = ? AND `env` = ? " +
            "AND (`user_name` = ? " +
            "AND `address` LIKE ? " +
            "AND `age` BETWEEN ? AND ? " +
            "AND `status` = ?)");
    }

    @Test
    void createStudent() {
        ATM.dataMap.student.cleanTable();
        Student student = service.saveStudent(new Student().setUserName("test").setAge(34));
        want.object(student).eqReflect(new Student().setUserName("test").setAge(34), EqMode.IGNORE_DEFAULTS);
        want.number(student.getId()).isGt(0L);
        ATM.dataMap.student.table(1).eqTable();
    }

    @Test
    void createStudents() {
        ATM.dataMap.student.cleanTable();
        service.saveStudent(list(
            new Student().setUserName("test1").setAge(34),
            new Student().setUserName("test2").setAge(44)
        ));
        ATM.dataMap.student.countEq(2);
    }

    @Test
    void updateStudent() {
        ATM.dataMap.student.table(1)
            .userName.values("---")
            .id.values(2L)
            .env.values("test_env")
            .cleanAndInsert();
        int count = service.updateStudent(new StudentUpdater().setUserName("test").setAge(34).setId(2L));
        want.number(count).eq(1);
        db.sqlList().wantFirstSql().eq("" +
            "UPDATE fluent_mybatis.student " +
            "SET `gmt_modified` = now(), `user_name` = ?, `age` = ? " +
            "WHERE `is_deleted` = ? AND `env` = ? AND (`id` = ?)");
        db.sqlList().wantFirstPara().eqList("test", 34, false, "test_env", 2L);
    }

    @Test
    void updateStudent2() {
        ATM.dataMap.student.table(2)
            .userName.values("---")
            .id.values(2L, 4L)
            .env.values("test_env")
            .cleanAndInsert();
        service.updateStudent(list(
            new StudentUpdater().setUserName("test1").setAge(34).setId(2L),
            new StudentUpdater().setUserName("test2").setAge(34).setId(4L)
        ));
        db.sqlList().wantFirstSql().eq("" +
            "UPDATE fluent_mybatis.student " +
            "SET `gmt_modified` = now(), `user_name` = ?, `age` = ? " +
            "WHERE `is_deleted` = ? AND `env` = ? AND (`id` = ?); " +
            "UPDATE fluent_mybatis.student " +
            "SET `gmt_modified` = now(), `user_name` = ?, `age` = ? " +
            "WHERE `is_deleted` = ? AND `env` = ? AND (`id` = ?)");
        db.sqlList().wantFirstPara().eqList(
            "test1", 34, false, "test_env", 2L,
            "test2", 34, false, "test_env", 4L);
    }

    @Test
    void deleteStudent() {
        ATM.dataMap.student.table(1)
            .userName.values("---")
            .id.values(2L)
            .env.values("test_env")
            .cleanAndInsert();
        int count = service.deleteById(1, 2);
        want.number(count).eq(1);
        db.sqlList().wantFirstSql().eq("" +
            "DELETE FROM fluent_mybatis.student " +
            "WHERE `is_deleted` = ? AND `env` = ? AND (`id` IN (?, ?))");
        db.sqlList().wantFirstPara().eqList(false, "test_env", 1, 2);
    }

    @Test
    void logicDeleteStudent() {
        service.logicDeleteStudent(new StudentQuery().setUserName("test"));
        db.sqlList().wantFirstSql()
            .start("UPDATE fluent_mybatis.student SET")
            .contains("`is_deleted` = ?")
            .end("WHERE `is_deleted` = ? AND `env` = ? AND (`user_name` = ?) ORDER BY `user_name` ASC, `age` DESC");
    }
}
