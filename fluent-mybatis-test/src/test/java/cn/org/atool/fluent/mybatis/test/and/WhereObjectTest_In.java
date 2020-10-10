package cn.org.atool.fluent.mybatis.test.and;

import cn.org.atool.fluent.mybatis.generate.entity.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.generate.entity.wrapper.UserQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import cn.org.atool.fluent.mybatis.If;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;

import java.util.Arrays;

public class WhereObjectTest_In extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Test
    public void in() {
        UserQuery query = new UserQuery()
            .where.age().in(Arrays.asList(34, 35))
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM t_user WHERE age IN (?, ?)", StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
    }

    @Test
    public void in_condition() {
        UserQuery query = new UserQuery()
            .where.age().in(Arrays.asList(34, 35), o -> true)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM t_user WHERE age IN (?, ?)", StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
    }

    @Test
    public void in_IfNotEmpty() {
        UserQuery query = new UserQuery()
            .where.age().in(Arrays.asList(34, 35), If::notEmpty)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM t_user WHERE age IN (?, ?)", StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
    }

    @Test
    public void in_array() {
        UserQuery query = new UserQuery()
            .where.age().in(new int[]{34, 35})
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM t_user WHERE age IN (?, ?)", StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
    }

    @Test
    public void in_array_condition() {
        UserQuery query = new UserQuery()
            .where.age().in(new Integer[]{34, 35}, o -> true)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM t_user WHERE age IN (?, ?)", StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
    }

    @Test
    public void in_array2_condition() {
        UserQuery query = new UserQuery()
            .where.age().in(new Integer[]{34, 35}, o -> true)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM t_user WHERE age IN (?, ?)", StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
    }

    @Test
    public void in_arr_IfNotEmpty() {
        UserQuery query = new UserQuery()
            .where.age().in(new Integer[]{34, 35}, If::notEmpty)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM t_user WHERE age IN (?, ?)", StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
    }

    @Test
    public void in_arr_IfNotEmpty2() {
        UserQuery query = new UserQuery()
            .where.age().in(new Integer[0], If::notEmpty)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM t_user");
    }

    @Test
    public void in_select() {
        UserQuery query = new UserQuery()
            .where.id().in("select id from t_user where age =?", 24)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT COUNT(*) FROM t_user WHERE id IN (select id from t_user where age =?)");
    }

    @Test
    public void in_select_nested() {
        UserQuery query = new UserQuery()
            .where.id().in(q -> q.selectId()
                .where.age().eq(24)
                .and.id().eq(3L)
                .end())
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT COUNT(*) FROM t_user " +
                "WHERE id IN (SELECT id FROM t_user WHERE age = ? AND id = ?)");
    }
}
