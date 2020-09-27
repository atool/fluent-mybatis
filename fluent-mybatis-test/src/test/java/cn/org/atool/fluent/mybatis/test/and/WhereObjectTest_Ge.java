package cn.org.atool.fluent.mybatis.test.and;

import cn.org.atool.fluent.mybatis.generate.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.UserQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class WhereObjectTest_Ge extends BaseTest {

    @Autowired
    private UserMapper mapper;

    @Test
    public void ge() {
        UserQuery query = new UserQuery()
            .where.age().ge(34).end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( * ) FROM t_user WHERE age >= ?");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34});
    }

    @Test
    public void ge_condition() {
        UserQuery query = new UserQuery()
            .where.age().ge(true, 34).end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( * ) FROM t_user WHERE age >= ?");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34});
    }

    @Test
    public void ge_IfNotNull() {
        UserQuery query = new UserQuery()
            .where.age().ge_IfNotNull(34).end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( * ) FROM t_user WHERE age >= ?");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34});
    }
}