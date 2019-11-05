package cn.org.atool.fluent.mybatis.test.and;

import cn.org.atool.fluent.mybatis.demo.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.demo.query.UserEntityQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AndObjectTest_Null extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Test
    public void isNull() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.isNull();
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user WHERE age IS NULL");
    }

    @Test
    public void isNull_condition_true() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.isNull(true);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user WHERE age IS NULL");
    }

    @Test
    public void isNull_condition_false() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.isNull(false);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user");
    }

    @Test
    public void isNotNull() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.isNotNull();
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user WHERE age IS NOT NULL");
    }

    @Test
    public void isNotNull_condition_true() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.isNotNull(true);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user WHERE age IS NOT NULL");
    }

    @Test
    public void isNotNull_condition_false() {
        UserEntityQuery query = new UserEntityQuery()
                .and.age.isNotNull(false);
        mapper.selectCount(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user");
    }
}