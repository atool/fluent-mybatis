package cn.org.atool.mybatis.fluent.test.and;

import cn.org.atool.mybatis.fluent.demo.mapper.UserMapper;
import cn.org.atool.mybatis.fluent.demo.query.UserEntityQuery;
import cn.org.atool.mybatis.fluent.exception.NullParameterException;
import cn.org.atool.mybatis.fluent.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AndObjectTest_Eq extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Test
    public void eq() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.eq(34);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user WHERE age = ?");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34});
    }

    @Test(expected = NullParameterException.class)
    public void eq_null() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.eq(null);
        mapper.selectCount(query);
    }

    @Test
    public void eq_condition_true() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.eq(true, 34);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user WHERE age = ?");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34});
    }

    @Test
    public void eq_condition_false() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.eq(false, 34);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user");
        db.sqlList().wantFirstPara().sizeEq(0);
    }

    @Test
    public void eq_condition_true_supplier() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.eq(true, () -> 34);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user WHERE age = ?");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34});
    }

    @Test
    public void eq_condition_false_supplier() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.eq(false, () -> 34);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user");
        db.sqlList().wantFirstPara().sizeEq(0);
    }

    @Test
    public void eq_IfNotNull() {
        UserEntityQuery query = new UserEntityQuery()
                .and.userName.eq_IfNotNull("name");
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user WHERE user_name = ?");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{"name"});
    }

    @Test
    public void eq_IfNull() {
        UserEntityQuery query = new UserEntityQuery()
                .and.userName.eq_IfNotNull(null);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user");
        db.sqlList().wantFirstPara().sizeEq(0);
    }

    @Test
    public void eq_Predicate_true() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.eq((age) -> age > 20, 34);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user WHERE age = ?");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34});
    }

    @Test
    public void eq_Predicate_false() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.eq((age) -> age < 20, 34);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user");
        db.sqlList().wantFirstPara().sizeEq(0);
    }

    @Test
    public void eq_Predicate_true_Supplier() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.eq((age) -> age > 20, () -> 34);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user WHERE age = ?");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34});
    }

    @Test
    public void eq_Predicate_false_Supplier() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.eq((age) -> age > 20, () -> 19);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user");
        db.sqlList().wantFirstPara().sizeEq(0);
    }

    @Test(expected = NullParameterException.class)
    public void eq_Predicate_true_Supplier_null() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.eq((age) -> age == null, () -> null);
        mapper.selectCount(query);
    }
}