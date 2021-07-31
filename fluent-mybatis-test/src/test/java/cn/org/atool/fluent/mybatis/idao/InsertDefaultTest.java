package cn.org.atool.fluent.mybatis.idao;

import cn.org.atool.fluent.mybatis.generate.ATM;
import cn.org.atool.fluent.mybatis.generate.dao.intf.StudentDao;
import cn.org.atool.fluent.mybatis.generate.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

public class InsertDefaultTest extends BaseTest {
    @Autowired
    private StudentDao dao;

    @DisplayName("dao插入默认值")
    @Test
    void testDefaultInsert() {
        ATM.dataMap.student.table().clean();
        new StudentEntity().setUserName("FluentMybatis").save();
        ATM.dataMap.student.table(1)
            .userName.values("FluentMybatis")
            .env.values("test_env")
            .tenant.values(234567L)
            .isDeleted.values(0)
            .eqTable();
        db.sqlList().wantFirstSql().eq(
            "INSERT INTO fluent_mybatis.student(`gmt_created`, `gmt_modified`, `is_deleted`, `env`, `tenant`, `user_name`) " +
                "VALUES (now(), now(), 0, ?, ?, ?)");
    }

    @DisplayName("dao批量插入默认值")
    @Test
    void testDefaultBatchInsert() {
        ATM.dataMap.student.table().clean();
        dao.save(Arrays.asList(new StudentEntity().setUserName("FluentMybatis")));
        ATM.dataMap.student.table(1)
            .userName.values("FluentMybatis")
            .env.values("test_env")
            .tenant.values(234567L)
            .isDeleted.values(0)
            .eqTable();
    }

    @Test
    void test_defaultQuery() {

    }
}
