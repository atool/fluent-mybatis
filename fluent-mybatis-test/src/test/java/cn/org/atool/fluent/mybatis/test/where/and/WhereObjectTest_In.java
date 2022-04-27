package cn.org.atool.fluent.mybatis.test.where.and;

import cn.org.atool.fluent.mybatis.If;
import cn.org.atool.fluent.mybatis.generator.shared2.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;

import java.util.Arrays;

public class WhereObjectTest_In extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void in() {
        StudentQuery query = StudentQuery.emptyQuery()
            .where.age().in(Arrays.asList(34, 35))
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT COUNT(*) FROM fluent_mybatis.student WHERE `age` IN (?, ?)", StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
    }

    @Test
    public void in_str() {
        StudentQuery query = StudentQuery.emptyQuery()
            .where.address().in(new String[]{"a1", "a2"})
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT COUNT(*) FROM fluent_mybatis.student WHERE `address` IN (?, ?)"
                , StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eqReflect(new String[]{"a1", "a2"});
    }

    @Test
    public void in_condition() {
        StudentQuery query = StudentQuery.emptyQuery()
            .where.age().in(Arrays.asList(34, 35), o -> true)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM fluent_mybatis.student WHERE `age` IN (?, ?)", StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
    }

    @Test
    public void in_IfNotEmpty() {
        StudentQuery query = StudentQuery.emptyQuery()
            .where.age().in(Arrays.asList(34, 35), If::notEmpty)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM fluent_mybatis.student WHERE `age` IN (?, ?)", StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
    }

    @Test
    public void in_array() {
        StudentQuery query = StudentQuery.emptyQuery()
            .where.age().in(new int[]{34, 35})
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM fluent_mybatis.student WHERE `age` IN (?, ?)", StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
    }

    @Test
    public void in_array_condition() {
        StudentQuery query = StudentQuery.emptyQuery()
            .where.age().in(new Integer[]{34, 35}, o -> true)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM fluent_mybatis.student WHERE `age` IN (?, ?)", StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
    }

    @Test
    public void in_array2_condition() {
        StudentQuery query = StudentQuery.emptyQuery()
            .where.age().in(new Integer[]{34, 35}, o -> true)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("" +
            "SELECT COUNT(*) " +
            "FROM fluent_mybatis.student WHERE `age` IN (?, ?)", StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
    }

    @Test
    public void in_arr_IfNotEmpty() {
        StudentQuery query = StudentQuery.emptyQuery()
            .where.age().in(new Integer[]{34, 35}, If::notEmpty)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM fluent_mybatis.student WHERE `age` IN (?, ?)", StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
    }

    @Test
    public void in_arr_IfNotEmpty2() {
        StudentQuery query = StudentQuery.emptyQuery()
            .where.age().in(new Integer[0], If::notEmpty)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM fluent_mybatis.student");
    }

    @Test
    public void in_select() {
        StudentQuery query = StudentQuery.emptyQuery()
            .where.id().in("select id FROM fluent_mybatis.student where age =?", 24)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("" +
            "SELECT COUNT(*) " +
            "FROM fluent_mybatis.student " +
            "WHERE `id` IN (select id FROM fluent_mybatis.student where age =?)");
    }

    @Test
    public void in_select_nested() {
        StudentQuery query = StudentQuery.emptyQuery()
            .where.id().in(q -> q.selectId()
                .where.age().eq(24)
                .and.id().eq(3L)
                .end())
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT COUNT(*) FROM fluent_mybatis.student " +
                "WHERE `id` IN (SELECT `id` FROM fluent_mybatis.student WHERE `age` = ? AND `id` = ?)");
    }
}
