package cn.org.atool.mbplus.test.and;

import cn.org.atool.mbplus.demo.mapper.UserMapper;
import cn.org.atool.mbplus.demo.query.UserEntityQuery;
import cn.org.atool.mbplus.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AndObjectTest_Between extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Test
    public void between() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.between(23, 40);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().where().eq("age BETWEEN ? AND ?");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{23, 40});
    }

    @Test
    public void between_condition_true() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.between(true, 23, 40);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().where().eq("age BETWEEN ? AND ?");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{23, 40});
    }

    @Test
    public void between_condition_false() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.between(false, 23, 40)
                .and.userName.like("user");
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().notContain("BETWEEN");
    }

    @Test
    public void notBetween() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.notBetween(23, 40);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().where().eq("age NOT BETWEEN ? AND ?");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{23, 40});
    }

    @Test
    public void notBetween_condition_true() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.notBetween(true, 23, 40);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().where().eq("age NOT BETWEEN ? AND ?");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{23, 40});
    }

    @Test
    public void notBetween_condition_false() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.notBetween(false, 23, 40);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().notContain("BETWEEN");
    }
}