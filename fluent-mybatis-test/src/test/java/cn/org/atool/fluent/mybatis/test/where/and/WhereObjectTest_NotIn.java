package cn.org.atool.fluent.mybatis.test.where.and;

import cn.org.atool.fluent.mybatis.If;
import cn.org.atool.fluent.mybatis.generator.shared2.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

public class WhereObjectTest_NotIn extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void notIn() {
        StudentQuery query = StudentQuery.emptyQuery()
            .where.age().notIn(Arrays.asList(34, 35))
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT COUNT(*) FROM fluent_mybatis.student WHERE `age` NOT IN (?, ?)");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
    }

    @Test
    public void notIn_condition() {
        StudentQuery query = StudentQuery.emptyQuery()
            .where.age().notIn(Arrays.asList(34, 35), o -> true)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("" +
            "SELECT COUNT(*) FROM fluent_mybatis.student WHERE `age` NOT IN (?, ?)");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
    }

    @Test
    public void notIn_IfNotEmpty() {
        StudentQuery query = StudentQuery.emptyQuery()
            .where.age().notIn(Arrays.asList(34, 35), If::notEmpty)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("" +
            "SELECT COUNT(*) FROM fluent_mybatis.student WHERE `age` NOT IN (?, ?)");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
    }

    @Test
    public void notIn_array() {
        StudentQuery query = StudentQuery.emptyQuery()
            .where.age().notIn(new Integer[]{34, 35})
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("" +
            "SELECT COUNT(*) FROM fluent_mybatis.student WHERE `age` NOT IN (?, ?)");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
    }

    @Test
    public void notIn_array_condition() {
        StudentQuery query = StudentQuery.emptyQuery()
            .where.age().notIn(new Integer[]{34, 35}, o -> true)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("" +
            "SELECT COUNT(*) FROM fluent_mybatis.student WHERE `age` NOT IN (?, ?)");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
    }

    @Test
    public void notIn_array2_condition() {
        StudentQuery query = StudentQuery.emptyQuery()
            .where.age().notIn(new Integer[]{34, 35}, o -> true)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("" +
            "SELECT COUNT(*) FROM fluent_mybatis.student WHERE `age` NOT IN (?, ?)");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
    }

    @Test
    public void notIn_arr_IfNotEmpty() {
        StudentQuery query = StudentQuery.emptyQuery()
            .where.age().notIn(new Integer[]{34, 35}, If::notEmpty)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("" +
            "SELECT COUNT(*) FROM fluent_mybatis.student WHERE `age` NOT IN (?, ?)");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
    }

    @Test
    public void notIn_arr_IfNotEmpty2() {
        StudentQuery query = StudentQuery.emptyQuery()
            .where.age().notIn(new Integer[0], If::notEmpty)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM fluent_mybatis.student");
    }
}
