package cn.org.atool.fluent.mybatis.test.entity;

import cn.org.atool.fluent.mybatis.generator.ATM;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

class IRichEntityTest extends BaseTest {
    @DisplayName("根据Entity非null值条件查询列表")
    @Test
    void testListByNotNull() {
        ATM.dataMap.student.table(3)
            .id.values(3, 4, 6)
            .userName.values("test1")
            .tenant.values(123L)
            .env.values("test_env")
            .cleanAndInsert();

        List<StudentEntity> list = new StudentEntity().setUserName("test1").setTenant(123L)
            .listByNotNull();

        db.sqlList().wantFirstSql().end("" +
            "FROM fluent_mybatis.student " +
            "WHERE `is_deleted` = ? AND `env` = ? AND `user_name` = ? AND `tenant` = ?");
        want.list(list).eqDataMap(ATM.dataMap.student.entity(3)
            .id.values(3, 4, 6)
            .userName.values("test1")
            .tenant.values(123L)
        );
    }

    @DisplayName("根据Entity非null值条件查询符合条件的第一条数据")
    @Test
    void firstByNotNull() {
        new StudentEntity().setUserName("user").firstByNotNull();
        db.sqlList().wantFirstSql().end("" +
            "FROM fluent_mybatis.student " +
            "WHERE `is_deleted` = ? AND `env` = ? AND `user_name` = ? " +
            "LIMIT ?, ?");
        db.sqlList().wantFirstPara().eqList(false, "test_env", "user", 0, 1);
    }

    @DisplayName("将Entity转换为Query操作")
    @Test
    void asQuery() {
        new StudentEntity().setUserName("user").setAge(34)
            .asQuery().to().listEntity();
        db.sqlList().wantFirstSql().end("" +
            "FROM fluent_mybatis.student " +
            "WHERE `is_deleted` = ? AND `env` = ? " +
            "AND `user_name` = ? AND `age` = ?");
        db.sqlList().wantFirstPara().eqList(false, "test_env", "user", 34);
    }
}
