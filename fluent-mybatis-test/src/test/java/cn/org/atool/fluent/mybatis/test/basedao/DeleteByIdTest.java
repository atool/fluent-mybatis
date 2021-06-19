package cn.org.atool.fluent.mybatis.test.basedao;

import cn.org.atool.fluent.mybatis.customize.StudentExtDao;
import cn.org.atool.fluent.mybatis.generate.ATM;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;

import java.util.Arrays;

public class DeleteByIdTest extends BaseTest {
    @Autowired
    private StudentExtDao dao;

    @Test
    public void test_deleteById() throws Exception {
        ATM.dataMap.student.initTable(10).cleanAndInsert();
        dao.deleteById(4L);
        db.sqlList().wantFirstSql().eq("DELETE FROM student WHERE `id` = ?");
        db.table(ATM.table.student).count().eq(9);
    }

    @Test
    public void test_logicDeleteById() throws Exception {
        dao.logicDeleteById(4L);
        db.sqlList().wantFirstSql().eq("" +
            "UPDATE student SET `is_deleted` = true WHERE `id` = ?");
    }

    @Test
    public void test_deleteById2() throws Exception {
        ATM.dataMap.student.initTable(10).cleanAndInsert();
        dao.deleteById(4L, 5L, 6L);
        db.sqlList().wantFirstSql().eq("DELETE FROM student WHERE `id` IN (?, ?, ?)");
        db.table(ATM.table.student).count().eq(7);
    }

    @Test
    public void test_deleteByIds() throws Exception {
        ATM.dataMap.student.initTable(10).cleanAndInsert();
        dao.deleteByIds(Arrays.asList(4L, 6L, 9L));
        db.sqlList().wantFirstSql().eq("DELETE FROM student WHERE `id` IN (?, ?, ?)", StringMode.SameAsSpace);
        db.table(ATM.table.student).count().eq(7);
    }

    @Test
    public void test_logicDeleteByIds() throws Exception {
        dao.logicDeleteByIds(Arrays.asList(4L, 6L, 9L));
        db.sqlList().wantFirstSql().eq("" +
            "UPDATE student SET `is_deleted` = true WHERE `id` IN (?, ?, ?)", StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eq(new Object[]{4L, 6L, 9L});
    }
}
