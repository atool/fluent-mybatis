package cn.org.atool.mbplus.test.and;

import cn.org.atool.mbplus.demo.mapper.UserMapper;
import cn.org.atool.mbplus.demo.query.UserEntityQuery;
import cn.org.atool.mbplus.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

public class AndObjectTest_In extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Test
    public void in() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.in(Arrays.asList(34, 35));
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user WHERE age IN (?,?)");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
    }

    @Test
    public void in_condition() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.in(true, Arrays.asList(34, 35));
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user WHERE age IN (?,?)");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
    }

    @Test
    public void in_supplier() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.in(true, () -> Arrays.asList(34, 35));
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user WHERE age IN (?,?)");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
    }

    @Test
    public void in_IfNotEmpty() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.in_IfNotEmpty(Arrays.asList(34, 35));
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user WHERE age IN (?,?)");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
    }

    @Test
    public void in_predicate() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.in((ages) -> true, Arrays.asList(34, 35));
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user WHERE age IN (?,?)");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
    }

    @Test
    public void in_predicate_supplier() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.in((ages) -> true, () -> Arrays.asList(34, 35));
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user WHERE age IN (?,?)");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
    }

    @Test
    public void in_array() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.in(34, 35);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user WHERE age IN (?,?)");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
    }

    @Test
    public void in_array_condition() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.in(true, new Integer[]{34, 35});
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user WHERE age IN (?,?)");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
    }

    @Test
    public void in_array2_condition() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.in(true, 34, 35);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user WHERE age IN (?,?)");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
    }

    @Test
    public void in_arr_IfNotEmpty() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.in_IfNotEmpty(34, 35);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user WHERE age IN (?,?)");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
    }

    @Test
    public void in_arr_IfNotEmpty2() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.in_IfNotEmpty(new Integer[0]);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user");
    }
}