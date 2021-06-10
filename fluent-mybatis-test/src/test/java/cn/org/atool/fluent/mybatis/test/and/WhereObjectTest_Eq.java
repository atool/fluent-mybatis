package cn.org.atool.fluent.mybatis.test.and;

import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;
import cn.org.atool.fluent.mybatis.generate.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class WhereObjectTest_Eq extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void eq() {
        StudentQuery query = new StudentQuery()
            .where.age().eq(34).end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("" +
            "SELECT COUNT(*) FROM student WHERE age = ?", StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34});
    }

    @Test
    public void eq_null() {
        assertThrows(FluentMybatisException.class,
            () -> new StudentQuery()
                .where.age().eq(null)
        );
    }

    @Test
    public void eq_condition_true() {
        StudentQuery query = new StudentQuery()
            .where.age().eq(34, o -> true)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM student WHERE age = ?", StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34});
    }

    @Test
    public void eq_condition_false() {
        StudentQuery query = new StudentQuery()
            .where.age().eq(34, o -> false)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM student");
        db.sqlList().wantFirstPara().sizeEq(0);
    }

    @Test
    public void eq_IfNotNull() {
        StudentQuery query = new StudentQuery()
            .where.userName().eq("name", Objects::nonNull)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM student WHERE user_name = ?", StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eqReflect(new Object[]{"name"});
    }

    @Test
    public void eq_IfNull() {
        StudentQuery query = new StudentQuery()
            .where.userName().eq(null, Objects::nonNull)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM student");
        db.sqlList().wantFirstPara().sizeEq(0);
    }
}