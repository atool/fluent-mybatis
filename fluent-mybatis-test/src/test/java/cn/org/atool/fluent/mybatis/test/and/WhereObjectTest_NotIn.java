package cn.org.atool.fluent.mybatis.test.and;

import cn.org.atool.fluent.mybatis.generate.entity.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.generate.entity.wrapper.UserQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import cn.org.atool.fluent.mybatis.utility.Predicates;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

public class WhereObjectTest_NotIn extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Test
    public void notIn() {
        UserQuery query = new UserQuery()
            .where.age().notIn(Arrays.asList(34, 35))
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM t_user WHERE age NOT IN (?, ?)");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
    }

    @Test
    public void notIn_condition() {
        UserQuery query = new UserQuery()
            .where.age().notIn(Arrays.asList(34, 35), o -> true)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM t_user WHERE age NOT IN (?, ?)");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
    }

    @Test
    public void notIn_IfNotEmpty() {
        UserQuery query = new UserQuery()
            .where.age().notIn(Arrays.asList(34, 35), Predicates::notEmpty)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM t_user WHERE age NOT IN (?, ?)");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
    }

    @Test
    public void notIn_array() {
        UserQuery query = new UserQuery()
            .where.age().notIn(new Integer[]{34, 35})
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM t_user WHERE age NOT IN (?, ?)");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
    }

    @Test
    public void notIn_array_condition() {
        UserQuery query = new UserQuery()
            .where.age().notIn(new Integer[]{34, 35}, o -> true)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM t_user WHERE age NOT IN (?, ?)");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
    }

    @Test
    public void notIn_array2_condition() {
        UserQuery query = new UserQuery()
            .where.age().notIn(new Integer[]{34, 35}, o -> true)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM t_user WHERE age NOT IN (?, ?)");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
    }

    @Test
    public void notIn_arr_IfNotEmpty() {
        UserQuery query = new UserQuery()
            .where.age().notIn(new Integer[]{34, 35}, Predicates::notEmpty)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM t_user WHERE age NOT IN (?, ?)");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
    }

    @Test
    public void notIn_arr_IfNotEmpty2() {
        UserQuery query = new UserQuery()
            .where.age().notIn(new Integer[0], Predicates::notEmpty)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM t_user");
    }
}