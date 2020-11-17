package cn.org.atool.fluent.mybatis.method;

import cn.org.atool.fluent.mybatis.generate.ATM;
import cn.org.atool.fluent.mybatis.generate.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.StudentUpdate;
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
        StudentUpdate update = new StudentUpdate()
            .update.userName().is("user name2").end()
            .where.id().eq(24L)
            .apply("1=1").end();
        mapper.updateBy(update);
        db.sqlList().wantFirstSql()
            .eq("UPDATE student SET gmt_modified = now(), user_name = ? WHERE id = ? AND 1=1", StringMode.SameAsSpace);
        db.table(ATM.table.student).query().eqDataMap(ATM.dataMap.student.table(2)
            .id.values(23L, 24L)
            .userName.values("user1", "user name2")
        );
    }

    @Test
    public void testUpdate_apply() {
        ATM.dataMap.student.initTable(2)
            .id.values(23L, 24L)
            .userName.values("user1", "user2")
            .cleanAndInsert();
        StudentUpdate update = new StudentUpdate()
            .update.userName().is("user name2").end()
            .where.id().eq(24L)
            .and.apply("user_name='user2'")
            .or.apply("user_name=?", "xxx").end();
        mapper.updateBy(update);
        db.sqlList().wantFirstSql()
            .eq("UPDATE student SET gmt_modified = now(), user_name = ? " +
                "WHERE id = ? " +
                "AND user_name='user2' " +
                "OR user_name=?", StringMode.SameAsSpace);
        ATM.dataMap.student.table(2)
            .id.values(23L, 24L)
            .userName.values("user1", "user name2")
            .eqTable();
    }
}
