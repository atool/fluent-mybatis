package cn.org.atool.fluent.mybatis.test1.formapi;

import cn.org.atool.fluent.mybatis.formapi.StdPagedQuery;
import cn.org.atool.fluent.mybatis.formapi.StudentQueryApi;
import cn.org.atool.fluent.mybatis.formapi.StudentQueryApi.TagPagedQuery;
import cn.org.atool.fluent.mybatis.formapi.StudentUpdateApi;
import cn.org.atool.fluent.mybatis.formapi.StudentUpdater;
import cn.org.atool.fluent.mybatis.generator.ATM;
import cn.org.atool.fluent.mybatis.model.StdPagedList;
import cn.org.atool.fluent.mybatis.model.TagPagedList;
import cn.org.atool.fluent.mybatis.test1.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.modes.EqMode;

import java.util.List;

public class FormServiceTest extends BaseTest {
    @Autowired
    StudentUpdateApi updateApi;

    @Autowired
    StudentQueryApi queryApi;

    @Test
    void listEntity() {
        ATM.dataMap.student.table(2)
            .env.values("test_env")
            .userName.values("ming.li")
            .age.values(23, 34)
            .email.values("xxx@test")
            .address.values("hangzhou binjiang")
            .cleanAndInsert();
        List<StudentQueryApi.Student> students = queryApi.listStudentBy(new StudentQueryApi.StudentQuery()
            .setUserName("ming.li")
            .setAddress("hangzhou")
            .setAge(new Integer[]{20, 40}));
        want.object(students).eqDataMap(
            ATM.dataMap.student.entity(2)
                .userName.values("ming.li")
                .age.values(34, 23)
                .address.values("hangzhou binjiang")
                .kv("hisEmail", "xxx@test")
        );
        db.sqlList().wantFirstSql().end("" +
            "FROM fluent_mybatis.student " +
            "WHERE `is_deleted` = ? AND `env` = ? AND `user_name` = ? " +
            "AND `address` LIKE ? " +
            "AND `age` BETWEEN ? AND ? " +
            "ORDER BY `user_name` ASC, `age` DESC");
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
        StdPagedList<StudentQueryApi.Student> students = queryApi.stdPagedStudent((StdPagedQuery) new StdPagedQuery()
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
            "WHERE `is_deleted` = ? AND `env` = ? AND `user_name` = ? " +
            "AND `address` LIKE ? " +
            "AND `age` BETWEEN ? AND ?");

        db.sqlList().wantSql(1).end("" +
            "FROM fluent_mybatis.student " +
            "WHERE `is_deleted` = ? AND `env` = ? AND `user_name` = ? " +
            "AND `address` LIKE ? " +
            "AND `age` BETWEEN ? AND ? " +
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
        TagPagedList<StudentQueryApi.Student> students = queryApi.tagPagedStudent((TagPagedQuery) new TagPagedQuery()
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
            "WHERE `is_deleted` = ? AND `env` = ? AND `user_name` = ? " +
            "AND `address` LIKE ? " +
            "AND `age` BETWEEN ? AND ? " +
            "AND `id` >= ? " +
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
        StudentQueryApi.Student student = queryApi.findByUserName("ming.li", new int[]{20, 40});
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
            "AND `user_name` = ? " +
            "AND `age` BETWEEN ? AND ? " +
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
        StudentQueryApi.Student student = queryApi.findByUserName("ming.li", new StudentQueryApi.StudentQuery().setAge(new Integer[]{20, 40}));
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
            "AND `user_name` = ? " +
            "AND `age` BETWEEN ? AND ? " +
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
        StudentQueryApi.Student student = queryApi.findStudentBy(new StudentQueryApi.StudentQuery()
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
            "WHERE `is_deleted` = ? AND `env` = ? AND `user_name` = ? " +
            "AND `address` LIKE ? " +
            "AND `age` >= ? " +
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
            .cleanAndInsert();
        long count = queryApi.countStudentBy(new StudentQueryApi.StudentQuery()
            .setUserName("ming.li")
            .setAddress("hangzhou")
            .setAge(new Integer[]{20, 40}));
        want.number(count).isEqualTo(2);
        db.sqlList().wantFirstSql().eq("" +
            "SELECT COUNT(*) " +
            "FROM fluent_mybatis.student " +
            "WHERE `is_deleted` = ? AND `env` = ? AND `user_name` = ? " +
            "AND `address` LIKE ? " +
            "AND `age` BETWEEN ? AND ?");
    }

    @Test
    void createStudent() {
        ATM.dataMap.student.table().clean();
        StudentUpdateApi.Student student = updateApi.saveStudent(new StudentUpdateApi.Student().setUserName("test").setAge(34));
        want.object(student).eqReflect(new StudentUpdateApi.Student().setUserName("test").setAge(34), EqMode.IGNORE_DEFAULTS);
        want.number(student.getId()).isGt(0L);
        ATM.dataMap.student.table(1).eqTable();
    }

    @Test
    void updateStudent() {
        ATM.dataMap.student.table(1)
            .userName.values("---")
            .id.values(2L)
            .env.values("test_env")
            .cleanAndInsert();
        int count = updateApi.updateStudent(new StudentUpdater().setUserName("test").setAge(34).setId(2L));
        want.number(count).eq(1);
        db.sqlList().wantFirstSql().eq("" +
            "UPDATE fluent_mybatis.student " +
            "SET `gmt_modified` = now(), `user_name` = ?, `age` = ? " +
            "WHERE `is_deleted` = ? AND `env` = ? AND `id` = ?");
        db.sqlList().wantFirstPara().eqList("test", 34, false, "test_env", 2L);
    }
}