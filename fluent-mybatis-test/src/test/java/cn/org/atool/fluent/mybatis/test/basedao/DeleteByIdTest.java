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
        ATM.DataMap.student.initTable(10).cleanAndInsert();
        dao.deleteById(4L);
        db.sqlList().wantFirstSql().eq("DELETE FROM student WHERE id = ?");
        db.table(ATM.Table.student).count().eq(9);
    }

    @Test
    public void test_deleteByIds() throws Exception {
        ATM.DataMap.student.initTable(10).cleanAndInsert();
        dao.deleteByIds(Arrays.asList(4L, 6L, 9L));
        db.sqlList().wantFirstSql().eq("DELETE FROM student WHERE id IN (?, ?, ?)", StringMode.SameAsSpace);
        db.table(ATM.Table.student).count().eq(7);
    }
}
