package cn.org.atool.fluent.mybatis.test.and;

import cn.org.atool.fluent.mybatis.demo.generate.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.demo.generate.query.UserQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;

public class WhereObjectTest_Ne extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Test
    public void ne() {
        UserQuery query = new UserQuery()
                .and.age.ne(34);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( * ) FROM t_user WHERE age <> ?", StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34});
    }

    @Test
    public void ne_condition() {
        UserQuery query = new UserQuery()
                .and.age.ne(true, 34);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( * ) FROM t_user WHERE age <> ?", StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34});
    }

    @Test
    public void ne_IfNotNull() {
        UserQuery query = new UserQuery()
                .and.age.ne_IfNotNull(34);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( * ) FROM t_user WHERE age <> ?");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34});
    }
}