package cn.org.atool.mbplus.test.and;

import cn.org.atool.mbplus.demo.mapper.UserMapper;
import cn.org.atool.mbplus.demo.query.UserEntityQuery;
import cn.org.atool.mbplus.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AndObjectTest_Le extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Test
    public void le() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.le(34);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user WHERE age <= ?");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34});
    }

    @Test
    public void le_condition() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.le(true, 34);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user WHERE age <= ?");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34});
    }

    @Test
    public void le_supplier() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.le(true, () -> 34);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user WHERE age <= ?");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34});
    }

    @Test
    public void le_IfNotNull() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.le_IfNotNull(34);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user WHERE age <= ?");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34});
    }

    @Test
    public void le_predicate() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.le((age) -> true, 34);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user WHERE age <= ?");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34});
    }

    @Test
    public void le_predicate_supplier() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.le((age) -> true, () -> 34);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user WHERE age <= ?");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34});
    }
}