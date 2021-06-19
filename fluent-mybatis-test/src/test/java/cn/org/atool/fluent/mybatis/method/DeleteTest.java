package cn.org.atool.fluent.mybatis.method;

import cn.org.atool.fluent.mybatis.generate.ATM;
import cn.org.atool.fluent.mybatis.generate.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.StudentQuery;
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

        StudentQuery query = new StudentQuery()
            .where.id().eq(24L).end();
        mapper.delete(query);
        db.sqlList().wantFirstSql()
            .eq("DELETE FROM student WHERE id = ?", StringMode.SameAsSpace);
        db.table(ATM.table.student).query().eqDataMap(ATM.dataMap.student.table(1)
            .id.values(23L)
            .userName.values("user1")
        );
    }

    @Test
    public void testLogicDeleteById() {
        mapper.logicDelete(new StudentQuery()
            .where.id().eq(24L).end());
        db.sqlList().wantFirstSql()
            .eq("UPDATE student SET `is_deleted` = true WHERE id = ?", StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eq(new Object[]{24L});
    }

    @Test
    public void testDelete_apply() {
        ATM.dataMap.student.initTable(2)
            .id.values(23L, 24L)
            .userName.values("user1", "user2")
            .cleanAndInsert();

        StudentQuery query = new StudentQuery()
            .where.apply("user_name=?", "user2").end();
        mapper.delete(query);
        db.sqlList().wantFirstSql()
            .eq("DELETE FROM student WHERE user_name=?", StringMode.SameAsSpace);
        db.table(ATM.table.student).query().eqDataMap(ATM.dataMap.student.table(1)
            .id.values(23L)
            .userName.values("user1")
        );
    }


    @Test
    public void testLogicDelete_apply() {
        StudentQuery query = new StudentQuery()
            .where.apply("user_name=?", "user2").end();
        mapper.logicDelete(query);
        db.sqlList().wantFirstSql()
            .eq("UPDATE student SET `is_deleted` = true WHERE user_name=?", StringMode.SameAsSpace);
    }
}
