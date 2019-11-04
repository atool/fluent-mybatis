package cn.org.atool.mybatis.fluent.test.and;

import cn.org.atool.mybatis.fluent.demo.mapper.UserMapper;
import cn.org.atool.mybatis.fluent.demo.query.UserEntityQuery;
import cn.org.atool.mybatis.fluent.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AndObjectTest_Ge extends BaseTest {

    @Autowired
    private UserMapper mapper;

    @Test
    public void ge() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.ge(34);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user WHERE age >= ?");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34});
    }

    @Test
    public void ge_condition() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.ge(true, 34);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user WHERE age >= ?");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34});
    }

    @Test
    public void ge_supplier() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.ge(true, () -> 34);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user WHERE age >= ?");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34});
    }

    @Test
    public void ge_IfNotNull() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.ge_IfNotNull(34);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user WHERE age >= ?");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34});
    }

    @Test
    public void ge_predicate() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.ge((age) -> true, 34);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user WHERE age >= ?");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34});
    }

    @Test
    public void ge_predicate_supplier() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.ge((age) -> true, () -> 34);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user WHERE age >= ?");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34});
    }
}