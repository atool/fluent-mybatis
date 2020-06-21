package cn.org.atool.fluent.mybatis.test.and;

import cn.org.atool.fluent.mybatis.demo.generate.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.demo.generate.query.UserQuery;
import cn.org.atool.fluent.mybatis.demo.generate.query.UserQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AndBooleanTest extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Test
    public void isTrue() {
        UserQuery query = new UserQuery()
                .and.isDeleted.isTrue();
        mapper.selectList(query);
        db.sqlList().wantFirstSql().where().eq("is_deleted = ?");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{true});
    }

    @Test
    public void isFalse() {
        UserQuery query = new UserQuery()
                .and.isDeleted.isFalse();
        mapper.selectList(query);
        db.sqlList().wantFirstSql().where().eq("is_deleted = ?");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{false});
    }
}