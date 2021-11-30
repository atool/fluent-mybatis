package cn.org.atool.fluent.mybatis.base.entity;

import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.test1.BaseTest;
import org.junit.jupiter.api.Test;

class IRichEntityTest extends BaseTest {

    @Test
    void firstByNotNull() {
        new StudentEntity().setUserName("user").firstByNotNull();
        db.sqlList().wantFirstSql().end("" +
            "FROM fluent_mybatis.student " +
            "WHERE `is_deleted` = ? AND `env` = ? AND `user_name` = ? " +
            "LIMIT ?, ?");
        db.sqlList().wantFirstPara().eqList(false, "test_env", "user", 0, 1);
    }

    @Test
    void asQuery() {
        StudentEntity.builder().userName("user").age(34).build()
            .asQuery().to().listEntity();
        db.sqlList().wantFirstSql().end("" +
            "FROM fluent_mybatis.student " +
            "WHERE `is_deleted` = ? AND `env` = ? " +
            "AND `user_name` = ? AND `age` = ?");
        db.sqlList().wantFirstPara().eqList(false, "test_env", "user", 34);
    }
}