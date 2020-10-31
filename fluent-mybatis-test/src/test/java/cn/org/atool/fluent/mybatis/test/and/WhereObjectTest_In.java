package cn.org.atool.fluent.mybatis.test.and;

import cn.org.atool.fluent.mybatis.generate.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import cn.org.atool.fluent.mybatis.If;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;

import java.util.Arrays;

public class WhereObjectTest_In extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void in() {
        StudentQuery query = new StudentQuery()
            .where.age().in(Arrays.asList(34, 35))
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM t_student WHERE age IN (?, ?)", StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
    }

    @Test
    public void in_condition() {
        StudentQuery query = new StudentQuery()
            .where.age().in(Arrays.asList(34, 35), o -> true)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM t_student WHERE age IN (?, ?)", StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
    }

    @Test
    public void in_IfNotEmpty() {
        StudentQuery query = new StudentQuery()
            .where.age().in(Arrays.asList(34, 35), If::notEmpty)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM t_student WHERE age IN (?, ?)", StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
    }

    @Test
    public void in_array() {
        StudentQuery query = new StudentQuery()
            .where.age().in(new int[]{34, 35})
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM t_student WHERE age IN (?, ?)", StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
    }

    @Test
    public void in_array_condition() {
        StudentQuery query = new StudentQuery()
            .where.age().in(new Integer[]{34, 35}, o -> true)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM t_student WHERE age IN (?, ?)", StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
    }

    @Test
    public void in_array2_condition() {
        StudentQuery query = new StudentQuery()
            .where.age().in(new Integer[]{34, 35}, o -> true)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM t_student WHERE age IN (?, ?)", StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
    }

    @Test
    public void in_arr_IfNotEmpty() {
        StudentQuery query = new StudentQuery()
            .where.age().in(new Integer[]{34, 35}, If::notEmpty)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM t_student WHERE age IN (?, ?)", StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
    }

    @Test
    public void in_arr_IfNotEmpty2() {
        StudentQuery query = new StudentQuery()
            .where.age().in(new Integer[0], If::notEmpty)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM t_student");
    }

    @Test
    public void in_select() {
        StudentQuery query = new StudentQuery()
            .where.id().in("select id from t_student where age =?", 24)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT COUNT(*) FROM t_student WHERE id IN (select id from t_student where age =?)");
    }

    @Test
    public void in_select_nested() {
        StudentQuery query = new StudentQuery()
            .where.id().in(q -> q.selectId()
                .where.age().eq(24)
                .and.id().eq(3L)
                .end())
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT COUNT(*) FROM t_student " +
                "WHERE id IN (SELECT id FROM t_student WHERE age = ? AND id = ?)");
    }
}
