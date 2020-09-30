package cn.org.atool.fluent.mybatis.test.and;

import cn.org.atool.fluent.mybatis.generate.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.UserQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class WhereObjectTest_Lt extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Test
    public void lt() {
        UserQuery query = new UserQuery()
            .where.age().lt(34)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM t_user WHERE age < ?");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34});
    }

    @Test
    public void lt_condition() {
        UserQuery query = new UserQuery()
            .where.age().lt(true, 34)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM t_user WHERE age < ?");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34});
    }

    @Test
    public void lt_IfNotNull() {
        UserQuery query = new UserQuery()
            .where.age().lt_IfNotNull(34)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM t_user WHERE age < ?");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34});
    }
}