package cn.org.atool.fluent.mybatis.test.method;

import cn.org.atool.fluent.mybatis.generator.ATM;
import cn.org.atool.fluent.mybatis.generator.shared2.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentUpdate;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;

public class UpdateByQueryTest extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void testUpdate() {
        ATM.dataMap.student.initTable(2)
            .id.values(23L, 24L)
            .userName.values("user1", "user2")
            .cleanAndInsert();
        StudentUpdate update = StudentUpdate.emptyUpdater()
            .set.userName().is("user name2").end()
            .where.id().eq(24L)
            .applyFunc("1=1").end();
        mapper.updateBy(update);
        db.sqlList().wantFirstSql().eq("" +
            "UPDATE fluent_mybatis.student " +
            "SET `gmt_modified` = now(), `user_name` = ? " +
            "WHERE `id` = ? AND 1=1", StringMode.SameAsSpace);
        ATM.dataMap.student.table(2)
            .id.values(23L, 24L)
            .userName.values("user1", "user name2")
            .eqTable();
    }

    @Test
    public void testUpdate_apply() {
        ATM.dataMap.student.initTable(2)
            .id.values(23L, 24L)
            .userName.values("user1", "user2")
            .cleanAndInsert();
        StudentUpdate update = StudentUpdate.emptyUpdater()
            .set.userName().is("user name2").end()
            .where.id().eq(24L)
            .and.applyFunc("user_name='user2'")
            .or.applyFunc("user_name=?", "xxx").end();
        mapper.updateBy(update);
        db.sqlList().wantFirstSql()
            .eq("UPDATE fluent_mybatis.student SET `gmt_modified` = now(), `user_name` = ? " +
                "WHERE `id` = ? " +
                "AND user_name='user2' " +
                "OR user_name=?", StringMode.SameAsSpace);
        ATM.dataMap.student.table(2)
            .id.values(23L, 24L)
            .userName.values("user1", "user name2")
            .eqTable();
    }
}
