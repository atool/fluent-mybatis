package cn.org.atool.fluent.mybatis.method;

import cn.org.atool.fluent.mybatis.generate.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generate.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.refs.FieldRef;
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

        StudentQuery query = mapper.query()
            .where.eqByEntity(student)
            .end();

        mapper.listEntity(query);

        db.sqlList().wantFirstSql()
            .end("FROM student WHERE address = ? AND user_name = ?");
        db.sqlList().wantFirstPara()
            .eqList("address", "test");
    }

    @Test
    void eqByEntity_Spec() {
        StudentEntity student = new StudentEntity()
            .setUserName("test")
            .setAddress("address");

        StudentQuery query = mapper.query()
            .where.eqByEntity(student, FieldRef.Student.userName, FieldRef.Student.age)
            .end();

        mapper.listEntity(query);

        db.sqlList().wantFirstSql()
            .end("FROM student WHERE user_name = ? AND age IS NULL");
        db.sqlList().wantFirstPara()
            .eqList("test");
    }


    @Test
    void eqByEntity_Getter() {
        StudentEntity student = new StudentEntity()
            .setUserName("test")
            .setAddress("address");

        StudentQuery query = mapper.query()
            .where.eqByEntity(student, StudentEntity::getUserName, StudentEntity::getAge)
            .end();

        mapper.listEntity(query);

        db.sqlList().wantFirstSql()
            .end("FROM student WHERE user_name = ? AND age IS NULL");
        db.sqlList().wantFirstPara()
            .eqList("test");
    }


    @Test
    void eqByExclude() {
        StudentEntity student = new StudentEntity()
            .setId(1L)
            .setUserName("test")
            .setAddress("address");

        StudentQuery query = mapper.query()
            .where.eqByExclude(student)
            .end();

        mapper.listEntity(query);

        db.sqlList().wantFirstSql()
            .contains("birthday IS NULL AND")
            .contains("AND address = ? AND")
            .contains("AND user_name = ? AND")
            .contains("AND id = ? AND");
        db.sqlList().wantFirstPara()
            .eqList("address", "test", 1L);
    }


    @Test
    void eqByExclude_IdIsNull() {
        StudentEntity student = new StudentEntity()
            .setUserName("test")
            .setAddress("address");

        StudentQuery query = mapper.query()
            .where.eqByExclude(student)
            .end();

        mapper.listEntity(query);

        db.sqlList().wantFirstSql()
            .contains("birthday IS NULL AND")
            .contains("AND address = ? AND")
            .contains("AND user_name = ? AND")
            .notContain("AND id IS NULL AND");
        db.sqlList().wantFirstPara()
            .eqList("address", "test");
    }


    @Test
    void eqByExclude_Getter() {
        StudentEntity student = new StudentEntity()
            .setUserName("test")
            .setAddress("address");

        StudentQuery query = mapper.query()
            .where.eqByExclude(student, StudentEntity::getAddress)
            .end();

        mapper.listEntity(query);

        db.sqlList().wantFirstSql()
            .contains("birthday IS NULL AND")
            .contains("AND user_name = ? AND")
            .notContain("AND address = ? AND")
            .notContain("AND id IS NULL AND");
        db.sqlList().wantFirstPara()
            .eqList("test");
    }
}
