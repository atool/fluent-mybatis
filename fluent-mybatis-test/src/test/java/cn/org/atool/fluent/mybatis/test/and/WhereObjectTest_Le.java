package cn.org.atool.fluent.mybatis.test.and;

import cn.org.atool.fluent.mybatis.generate.entity.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.generate.entity.wrapper.UserQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

public class WhereObjectTest_Le extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Test
    public void le() {
        UserQuery query = new UserQuery()
            .where.age().le(34)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM t_user WHERE age <= ?");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34});
    }

    @Test
    public void le_condition() {
        UserQuery query = new UserQuery()
            .where.age().le(34, o -> true)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM t_user WHERE age <= ?");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34});
    }

    @Test
    public void le_IfNotNull() {
        UserQuery query = new UserQuery()
            .where.age().le(34, Objects::nonNull)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM t_user WHERE age <= ?");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34});
    }
}