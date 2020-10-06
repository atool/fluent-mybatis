package cn.org.atool.fluent.mybatis.test.and;

import cn.org.atool.fluent.mybatis.generate.entity.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.generate.entity.wrapper.UserQuery;
import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class WhereObjectTest_Eq extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Test
    public void eq() {
        UserQuery query = new UserQuery()
            .where.age().eq(34).end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM t_user WHERE age = ?", StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34});
    }

    @Test
    public void eq_null() {
        assertThrows(FluentMybatisException.class,
            () -> new UserQuery()
                .where.age().eq(null)
        );
    }

    @Test
    public void eq_condition_true() {
        UserQuery query = new UserQuery()
            .where.age().eq(34, o -> true)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM t_user WHERE age = ?", StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34});
    }

    @Test
    public void eq_condition_false() {
        UserQuery query = new UserQuery()
            .where.age().eq(34, o -> false)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM t_user");
        db.sqlList().wantFirstPara().sizeEq(0);
    }

    @Test
    public void eq_IfNotNull() {
        UserQuery query = new UserQuery()
            .where.userName().eq("name", Objects::nonNull)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM t_user WHERE user_name = ?", StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eqReflect(new Object[]{"name"});
    }

    @Test
    public void eq_IfNull() {
        UserQuery query = new UserQuery()
            .where.userName().eq(null, Objects::nonNull)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM t_user");
        db.sqlList().wantFirstPara().sizeEq(0);
    }
}