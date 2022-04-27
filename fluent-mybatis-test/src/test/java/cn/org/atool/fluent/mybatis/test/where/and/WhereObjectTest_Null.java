package cn.org.atool.fluent.mybatis.test.where.and;

import cn.org.atool.fluent.mybatis.generator.shared2.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class WhereObjectTest_Null extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void isNull() {
        StudentQuery query = StudentQuery.emptyQuery()
            .where.age().isNull()
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("" +
            "SELECT COUNT(*) FROM fluent_mybatis.student WHERE `age` IS NULL");
    }

    @Test
    public void isNull_condition_true() {
        StudentQuery query = StudentQuery.emptyQuery()
            .where.age().isNull(true)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("" +
            "SELECT COUNT(*) FROM fluent_mybatis.student WHERE `age` IS NULL");
    }

    @Test
    public void isNull_condition_false() {
        StudentQuery query = StudentQuery.emptyQuery()
            .where.age().isNull(false)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM fluent_mybatis.student");
    }

    @Test
    public void isNotNull() {
        StudentQuery query = StudentQuery.emptyQuery()
            .where.age().notNull()
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT COUNT(*) FROM fluent_mybatis.student WHERE `age` IS NOT NULL");
    }

    @Test
    public void isNotNull_condition_true() {
        StudentQuery query = StudentQuery.emptyQuery()
            .where.age().notNull(true)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("" +
            "SELECT COUNT(*) FROM fluent_mybatis.student WHERE `age` IS NOT NULL");
    }

    @Test
    public void isNotNull_condition_false() {
        StudentQuery query = StudentQuery.emptyQuery()
            .where.age().notNull(false)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM fluent_mybatis.student");
    }
}
