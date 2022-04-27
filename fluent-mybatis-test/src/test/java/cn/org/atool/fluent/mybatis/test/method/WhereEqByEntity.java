package cn.org.atool.fluent.mybatis.test.method;

import cn.org.atool.fluent.mybatis.generator.shared2.Ref;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generator.shared2.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings({"unchecked"})
public class WhereEqByEntity extends BaseTest {
    @Autowired
    StudentMapper mapper;

    @Test
    void eqByEntity() {
        StudentEntity student = new StudentEntity()
            .setUserName("test")
            .setAddress("address");

        StudentQuery query = mapper.emptyQuery()
            .where.eqByEntity(student)
            .end();

        mapper.listEntity(query);

        db.sqlList().wantFirstSql()
            .end("FROM fluent_mybatis.student WHERE `address` = ? AND `user_name` = ?");
        db.sqlList().wantFirstPara()
            .eqList("address", "test");
    }

    @Test
    void eqByEntity_Spec() {
        StudentEntity student = new StudentEntity()
            .setUserName("test")
            .setAddress("address");

        StudentQuery query = mapper.emptyQuery()
            .where.eqByEntity(student, Ref.Field.Student.userName, Ref.Field.Student.age)
            .end();

        mapper.listEntity(query);

        db.sqlList().wantFirstSql()
            .end("FROM fluent_mybatis.student WHERE `user_name` = ? AND `age` IS NULL");
        db.sqlList().wantFirstPara()
            .eqList("test");
    }


    @Test
    void eqByEntity_Getter() {
        StudentEntity student = new StudentEntity()
            .setUserName("test")
            .setAddress("address");

        StudentQuery query = mapper.emptyQuery()
            .where.eqByEntity(student, StudentEntity::getUserName, StudentEntity::getAge)
            .end();

        mapper.listEntity(query);

        db.sqlList().wantFirstSql()
            .end("FROM fluent_mybatis.student WHERE `user_name` = ? AND `age` IS NULL");
        db.sqlList().wantFirstPara()
            .eqList("test");
    }


    @Test
    void eqByExclude() {
        StudentEntity student = new StudentEntity()
            .setId(1L)
            .setUserName("test")
            .setAddress("address");

        StudentQuery query = mapper.emptyQuery()
            .where.eqByExclude(student)
            .end();

        mapper.listEntity(query);

        db.sqlList().wantFirstSql()
            .contains("`birthday` IS NULL AND")
            .contains("AND `address` = ? AND")
            .contains("AND `user_name` = ? AND")
            .contains("AND `id` = ? AND");
        db.sqlList().wantFirstPara()
            .eqList("address", "test", 1L);
    }


    @Test
    void eqByExclude_IdIsNull() {
        StudentEntity student = new StudentEntity()
            .setUserName("test")
            .setAddress("address");

        StudentQuery query = mapper.emptyQuery()
            .where.eqByExclude(student)
            .end();

        mapper.listEntity(query);

        db.sqlList().wantFirstSql()
            .contains("`birthday` IS NULL AND")
            .contains("AND `address` = ? AND")
            .contains("AND `user_name` = ? AND")
            .notContain("AND `id` IS NULL AND");
        db.sqlList().wantFirstPara()
            .eqList("address", "test");
    }


    @Test
    void eqByExclude_Getter() {
        StudentEntity student = new StudentEntity()
            .setUserName("test")
            .setAddress("address");

        StudentQuery query = mapper.emptyQuery()
            .where.eqByExclude(student, StudentEntity::getAddress)
            .end();

        mapper.listEntity(query);

        db.sqlList().wantFirstSql()
            .contains("`birthday` IS NULL AND")
            .contains("AND `user_name` = ? AND")
            .notContain("AND `address` = ? AND")
            .notContain("AND `id` IS NULL AND");
        db.sqlList().wantFirstPara()
            .eqList("test");
    }
}
