package cn.org.atool.fluent.mybatis.test.method;

import cn.org.atool.fluent.form.Form;
import cn.org.atool.fluent.mybatis.generator.ATM;
import cn.org.atool.fluent.mybatis.generator.shared2.Ref;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;

class QueryExecutorTest_Delete extends BaseTest {

    @Test
    void delete() {
        ATM.dataMap.student.table(2)
            .userName.values("test1", "test2")
            .env.values("test_env")
            .isDeleted.values(0)
            .cleanAndInsert();
        Ref.Query.student.emptyQuery()
            .where.defaults()
            .and.userName().eq("test1").end()
            .to().delete();
        db.sqlList().wantFirstSql().eq("" +
            "DELETE FROM fluent_mybatis.student " +
            "WHERE `is_deleted` = ? AND `env` = ? AND `user_name` = ?");
        ATM.dataMap.student.table(1)
            .userName.values("test2")
            .eqTable();
    }

    @Test
    void delete_entity() {
        ATM.dataMap.student.table(2)
            .userName.values("test1", "test2")
            .env.values("test_env")
            .isDeleted.values(0)
            .cleanAndInsert();
        Form.with(new StudentEntity().setUserName("test1"), wrap -> wrap
            .eq("userName")
        ).delete();

        db.sqlList().wantFirstSql().eq("" +
            "DELETE FROM fluent_mybatis.student " +
            "WHERE `is_deleted` = ? AND `env` = ? AND `user_name` = ?");
        ATM.dataMap.student.table(1)
            .userName.values("test2")
            .eqTable();
    }
}
