package cn.org.atool.fluent.mybatis.test.and;

import cn.org.atool.fluent.mybatis.demo.generate.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.demo.generate.query.UserQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class WhereObjectTest_Between extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Test
    public void between() {
        UserQuery query = new UserQuery()
                .and.age.between(23, 40);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().where().eq("age BETWEEN ? AND ?");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{23, 40});
    }

    @Test
    public void between_condition_true() {
        UserQuery query = new UserQuery()
                .and.age.between(true, 23, 40);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().where().eq("age BETWEEN ? AND ?");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{23, 40});
    }

    @Test
    public void between_condition_false() {
        UserQuery query = new UserQuery()
                .and.age.between(false, 23, 40)
                .and.userName.like("user");
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().notContain("BETWEEN");
    }

    @Test
    public void notBetween() {
        UserQuery query = new UserQuery()
                .and.age.notBetween(23, 40);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().where().eq("age NOT BETWEEN ? AND ?");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{23, 40});
    }

    @Test
    public void notBetween_condition_true() {
        UserQuery query = new UserQuery()
                .and.age.notBetween(true, 23, 40);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().where().eq("age NOT BETWEEN ? AND ?");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{23, 40});
    }

    @Test
    public void notBetween_condition_false() {
        UserQuery query = new UserQuery()
                .and.age.notBetween(false, 23, 40);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().notContain("BETWEEN");
    }
}