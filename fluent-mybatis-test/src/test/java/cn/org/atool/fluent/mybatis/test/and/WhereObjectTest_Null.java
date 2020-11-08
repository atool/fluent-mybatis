package cn.org.atool.fluent.mybatis.test.and;

import cn.org.atool.fluent.mybatis.generate.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class WhereObjectTest_Null extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void isNull() {
        StudentQuery query = new StudentQuery()
            .where.age().isNull()
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM student WHERE age IS NULL");
    }

    @Test
    public void isNull_condition_true() {
        StudentQuery query = new StudentQuery()
            .where.age().isNull(true)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM student WHERE age IS NULL");
    }

    @Test
    public void isNull_condition_false() {
        StudentQuery query = new StudentQuery()
            .where.age().isNull(false)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM student");
    }

    @Test
    public void isNotNull() {
        StudentQuery query = new StudentQuery()
            .where.age().notNull()
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM student WHERE age IS NOT NULL");
    }

    @Test
    public void isNotNull_condition_true() {
        StudentQuery query = new StudentQuery()
            .where.age().notNull(true)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM student WHERE age IS NOT NULL");
    }

    @Test
    public void isNotNull_condition_false() {
        StudentQuery query = new StudentQuery()
            .where.age().notNull(false)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM student");
    }
}
