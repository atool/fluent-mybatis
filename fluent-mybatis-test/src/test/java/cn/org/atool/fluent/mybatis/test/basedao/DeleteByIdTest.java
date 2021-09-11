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
    public void test_deleteById() {
        ATM.dataMap.student.initTable(10)
            .env.values("test_env")
            .cleanAndInsert();
        dao.deleteById(4L);
        db.sqlList().wantFirstSql().eq("DELETE FROM fluent_mybatis.student " +
            "WHERE `is_deleted` = ? AND `env` = ? AND `id` = ?");
        db.table(ATM.table.student).count().eq(9);
    }

    @Test
    public void test_logicDeleteById() {
        dao.logicDeleteById(4L);
        db.sqlList().wantFirstSql().eq("" +
                "UPDATE fluent_mybatis.student " +
                "SET `gmt_modified` = now(), `is_deleted` = ? " +
                "WHERE `is_deleted` = ? AND `env` = ? AND `id` = ?"
            , StringMode.SameAsSpace);
    }

    @Test
    public void test_deleteById2() {
        ATM.dataMap.student.initTable(10)
            .env.values("test_env")
            .cleanAndInsert();
        dao.deleteById(4L, 5L, 6L);
        db.sqlList().wantFirstSql().eq("DELETE FROM fluent_mybatis.student " +
            "WHERE `is_deleted` = ? AND `env` = ? AND `id` IN (?, ?, ?)");
        db.table(ATM.table.student).count().eq(7);
    }

    @Test
    public void test_deleteByIds() {
        ATM.dataMap.student.initTable(10)
            .env.values("test_env")
            .cleanAndInsert();
        dao.deleteByIds(Arrays.asList(4L, 6L, 9L));
        db.sqlList().wantFirstSql()
            .eq("DELETE FROM fluent_mybatis.student " +
                    "WHERE `is_deleted` = ? AND `env` = ? AND `id` IN (?, ?, ?)"
                , StringMode.SameAsSpace);
        db.table(ATM.table.student).count().eq(7);
    }

    @Test
    public void test_logicDeleteByIds() {
        dao.logicDeleteByIds(Arrays.asList(4L, 6L, 9L));
        db.sqlList().wantFirstSql().eq("" +
                "UPDATE fluent_mybatis.student " +
                "SET `gmt_modified` = now(), `is_deleted` = ? " +
                "WHERE `is_deleted` = ? AND `env` = ? AND `id` IN (?, ?, ?)"
            , StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eqList(true, false, "test_env", 4L, 6L, 9L);
    }
}
