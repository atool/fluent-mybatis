package cn.org.atool.mbplus.test.and;

import cn.org.atool.mbplus.demo.mapper.UserMapper;
import cn.org.atool.mbplus.demo.query.UserEntityQuery;
import cn.org.atool.mbplus.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AndObjectTest_Lt extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Test
    public void lt() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.lt(34);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user WHERE age < ?");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34});
    }

    @Test
    public void lt_condition() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.lt(true, 34);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user WHERE age < ?");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34});
    }

    @Test
    public void lt_supplier() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.lt(true, () -> 34);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user WHERE age < ?");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34});
    }

    @Test
    public void lt_IfNotNull() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.lt_IfNotNull(34);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user WHERE age < ?");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34});
    }

    @Test
    public void lt_predicate() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.lt((age) -> true, 34);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user WHERE age < ?");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34});
    }

    @Test
    public void lt_predicate_supplier() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.lt((age) -> true, () -> 34);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user WHERE age < ?");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34});
    }
}