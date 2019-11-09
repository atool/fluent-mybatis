package cn.org.atool.fluent.mybatis.test.and;

import cn.org.atool.fluent.mybatis.demo.generate.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.demo.generate.query.UserEntityQuery;
import cn.org.atool.fluent.mybatis.exception.NullParameterException;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class AndObjectTest_Eq extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Test
    public void eq() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.eq(34);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user WHERE (age = ?)", StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34});
    }

    @Test
    public void eq_null() {
        assertThrows(NullParameterException.class,
                () -> new UserEntityQuery()
                        .and.age.eq(null)
        );
    }

    @Test
    public void eq_condition_true() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.eq(true, 34);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user WHERE (age = ?)", StringMode.SameAsSpace);
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
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user WHERE (age = ?)", StringMode.SameAsSpace);
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
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user WHERE (user_name = ?)", StringMode.SameAsSpace);
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
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user WHERE (age = ?)", StringMode.SameAsSpace);
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
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user WHERE (age = ?)", StringMode.SameAsSpace);
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

    @Test
    public void eq_Predicate_true_Supplier_null() {
        assertThrows(NullParameterException.class,
                () -> new UserEntityQuery()
                        .and.age.eq((age) -> age == null, () -> null)
        );
    }
}