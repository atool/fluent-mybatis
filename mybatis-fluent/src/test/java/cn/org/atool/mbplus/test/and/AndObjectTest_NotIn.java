package cn.org.atool.mbplus.test.and;

import cn.org.atool.mbplus.demo.mapper.UserMapper;
import cn.org.atool.mbplus.demo.query.UserEntityQuery;
import cn.org.atool.mbplus.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

public class AndObjectTest_NotIn extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Test
    public void notIn() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.notIn(Arrays.asList(34, 35));
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user WHERE age NOT IN (?,?)");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
    }

    @Test
    public void notIn_condition() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.notIn(true, Arrays.asList(34, 35));
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user WHERE age NOT IN (?,?)");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
    }

    @Test
    public void notIn_supplier() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.notIn(true, () -> Arrays.asList(34, 35));
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user WHERE age NOT IN (?,?)");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
    }

    @Test
    public void notIn_IfNotEmpty() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.notIn_IfNotEmpty(Arrays.asList(34, 35));
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user WHERE age NOT IN (?,?)");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
    }

    @Test
    public void notIn_predicate() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.notIn((ages) -> true, Arrays.asList(34, 35));
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user WHERE age NOT IN (?,?)");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
    }

    @Test
    public void notIn_predicate_supplier() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.notIn((ages) -> true, () -> Arrays.asList(34, 35));
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user WHERE age NOT IN (?,?)");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
    }

    @Test
    public void notIn_array() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.notIn(34, 35);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user WHERE age NOT IN (?,?)");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
    }

    @Test
    public void notIn_array_condition() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.notIn(true, new Integer[]{34, 35});
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user WHERE age NOT IN (?,?)");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
    }

    @Test
    public void notIn_array2_condition() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.notIn(true, 34, 35);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user WHERE age NOT IN (?,?)");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
    }

    @Test
    public void notIn_arr_IfNotEmpty() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.notIn_IfNotEmpty(34, 35);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user WHERE age NOT IN (?,?)");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
    }

    @Test
    public void notIn_arr_IfNotEmpty2() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.notIn_IfNotEmpty(new Integer[0]);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user");
    }
}