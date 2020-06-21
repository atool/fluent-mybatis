package cn.org.atool.fluent.mybatis.test.and;

import cn.org.atool.fluent.mybatis.demo.generate.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.demo.generate.query.UserQuery;
import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class WhereObjectTest_Eq extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Test
    public void eq() {
        UserQuery query = new UserQuery()
            .and.age.eq(34);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( * ) FROM t_user WHERE age = ?", StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34});
    }

    @Test
    public void eq_null() {
        assertThrows(FluentMybatisException.class,
            () -> new UserQuery()
                .and.age.eq(null)
        );
    }

    @Test
    public void eq_condition_true() {
        UserQuery query = new UserQuery()
            .and.age.eq(true, 34);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( * ) FROM t_user WHERE age = ?", StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34});
    }

    @Test
    public void eq_condition_false() {
        UserQuery query = new UserQuery()
            .and.age.eq(false, 34);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( * ) FROM t_user");
        db.sqlList().wantFirstPara().sizeEq(0);
    }

    @Test
    public void eq_IfNotNull() {
        UserQuery query = new UserQuery()
            .and.userName.eq_IfNotNull("name");
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( * ) FROM t_user WHERE user_name = ?", StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eqReflect(new Object[]{"name"});
    }

    @Test
    public void eq_IfNull() {
        UserQuery query = new UserQuery()
            .and.userName.eq_IfNotNull(null);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( * ) FROM t_user");
        db.sqlList().wantFirstPara().sizeEq(0);
    }
}