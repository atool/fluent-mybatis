package cn.org.atool.fluent.mybatis.test.and;

import cn.org.atool.fluent.mybatis.demo.generate.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.demo.generate.query.UserQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class WhereObjectTest_Null extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Test
    public void isNull() {
        UserQuery query = new UserQuery()
                .and.age.isNull();
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( * ) FROM t_user WHERE age IS NULL");
    }

    @Test
    public void isNull_condition_true() {
        UserQuery query = new UserQuery()
                .and.age.isNull(true);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( * ) FROM t_user WHERE age IS NULL");
    }

    @Test
    public void isNull_condition_false() {
        UserQuery query = new UserQuery()
                .and.age.isNull(false);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( * ) FROM t_user");
    }

    @Test
    public void isNotNull() {
        UserQuery query = new UserQuery()
                .and.age.isNotNull();
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( * ) FROM t_user WHERE age IS NOT NULL");
    }

    @Test
    public void isNotNull_condition_true() {
        UserQuery query = new UserQuery()
                .and.age.isNotNull(true);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( * ) FROM t_user WHERE age IS NOT NULL");
    }

    @Test
    public void isNotNull_condition_false() {
        UserQuery query = new UserQuery()
                .and.age.isNotNull(false);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( * ) FROM t_user");
    }
}