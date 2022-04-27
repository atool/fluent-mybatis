package cn.org.atool.fluent.mybatis.test.method;

import cn.org.atool.fluent.mybatis.generator.ATM;
import cn.org.atool.fluent.mybatis.generator.shared2.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;

public class DeleteTest extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void testDeleteById() {
        ATM.dataMap.student.initTable(2)
            .id.values(23L, 24L)
            .userName.values("user1", "user2")
            .cleanAndInsert();

        StudentQuery query = StudentQuery.emptyQuery()
            .where.id().eq(24L).end();
        mapper.delete(query);
        db.sqlList().wantFirstSql()
            .eq("DELETE FROM fluent_mybatis.student WHERE `id` = ?", StringMode.SameAsSpace);
        ATM.dataMap.student.table(1)
            .id.values(23L)
            .userName.values("user1")
            .eqTable();
    }

    @Test
    public void testLogicDeleteById() {
        mapper.logicDelete(StudentQuery.query()
            .where.id().eq(24L).end());
        db.sqlList().wantFirstSql().eq("" +
                "UPDATE fluent_mybatis.student " +
                "SET `gmt_modified` = now(), `is_deleted` = ? " +
                "WHERE `is_deleted` = ? AND `env` = ? AND `id` = ?"
            , StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eqList(true, false, "test_env", 24L);
    }

    @Test
    public void testDelete_apply() {
        ATM.dataMap.student.initTable(2)
            .id.values(23L, 24L)
            .userName.values("user1", "user2")
            .cleanAndInsert();

        StudentQuery query = StudentQuery.emptyQuery()
            .where.applyFunc("user_name=?", "user2").end();
        mapper.delete(query);
        db.sqlList().wantFirstSql()
            .eq("DELETE FROM fluent_mybatis.student WHERE user_name=?", StringMode.SameAsSpace);
        ATM.dataMap.student.table(1)
            .id.values(23L)
            .userName.values("user1")
            .eqTable();
    }


    @Test
    public void testLogicDelete_apply() {
        StudentQuery query = StudentQuery.emptyQuery()
            .where.applyFunc("user_name=?", "user2").end();
        mapper.logicDelete(query);
        db.sqlList().wantFirstSql().eq("" +
                "UPDATE fluent_mybatis.student " +
                "SET `gmt_modified` = now(), `is_deleted` = ? " +
                "WHERE user_name=?",
            StringMode.SameAsSpace);
    }
}
