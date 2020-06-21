package cn.org.atool.fluent.mybatis.test.and;

import cn.org.atool.fluent.mybatis.demo.generate.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.demo.generate.query.UserQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

public class WhereObjectTest_NotIn extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Test
    public void notIn() {
        UserQuery query = new UserQuery()
                .and.age.notIn(Arrays.asList(34, 35));
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( * ) FROM t_user WHERE age NOT IN (?, ?)");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
    }

    @Test
    public void notIn_condition() {
        UserQuery query = new UserQuery()
                .and.age.notIn(true, Arrays.asList(34, 35));
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( * ) FROM t_user WHERE age NOT IN (?, ?)");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
    }

    @Test
    public void notIn_IfNotEmpty() {
        UserQuery query = new UserQuery()
                .and.age.notIn_IfNotEmpty(Arrays.asList(34, 35));
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( * ) FROM t_user WHERE age NOT IN (?, ?)");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
    }

    @Test
    public void notIn_array() {
        UserQuery query = new UserQuery()
                .and.age.notIn(34, 35);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( * ) FROM t_user WHERE age NOT IN (?, ?)");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
    }

    @Test
    public void notIn_array_condition() {
        UserQuery query = new UserQuery()
                .and.age.notIn(true, new Integer[]{34, 35});
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( * ) FROM t_user WHERE age NOT IN (?, ?)");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
    }

    @Test
    public void notIn_array2_condition() {
        UserQuery query = new UserQuery()
                .and.age.notIn(true, 34, 35);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( * ) FROM t_user WHERE age NOT IN (?, ?)");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
    }

    @Test
    public void notIn_arr_IfNotEmpty() {
        UserQuery query = new UserQuery()
                .and.age.notIn_IfNotEmpty(34, 35);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( * ) FROM t_user WHERE age NOT IN (?, ?)");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
    }

    @Test
    public void notIn_arr_IfNotEmpty2() {
        UserQuery query = new UserQuery()
                .and.age.notIn_IfNotEmpty(new Integer[0]);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( * ) FROM t_user");
    }
}