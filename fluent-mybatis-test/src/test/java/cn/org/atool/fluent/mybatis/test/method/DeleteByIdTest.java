package cn.org.atool.fluent.mybatis.test.method;

import cn.org.atool.fluent.mybatis.customize.StudentExtDao;
import cn.org.atool.fluent.mybatis.generator.ATM;
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
            "WHERE `id` = ?");
        ATM.dataMap.student.countEq(9);
    }

    @Test
    public void test_logicDeleteById() {
        dao.logicDeleteById(4L);
        db.sqlList().wantFirstSql().eq("" +
                "UPDATE fluent_mybatis.student " +
                "SET `gmt_modified` = now(), `is_deleted` = ? " +
                "WHERE `id` = ?"
            , StringMode.SameAsSpace);
    }

    @Test
    public void test_deleteById2() {
        ATM.dataMap.student.initTable(10)
            .env.values("test_env")
            .cleanAndInsert();
        dao.deleteById(4L, 5L, 6L);
        db.sqlList().wantFirstSql().eq("DELETE FROM fluent_mybatis.student " +
            "WHERE `id` IN (?, ?, ?)");
        ATM.dataMap.student.countEq(7);
    }

    @Test
    public void test_deleteByIds() {
        ATM.dataMap.student.initTable(10)
            .env.values("test_env")
            .cleanAndInsert();
        dao.deleteByIds(Arrays.asList(4L, 6L, 9L));
        db.sqlList().wantFirstSql()
            .eq("DELETE FROM fluent_mybatis.student " +
                    "WHERE `id` IN (?, ?, ?)"
                , StringMode.SameAsSpace);
        ATM.dataMap.student.countEq(7);
    }

    @Test
    public void test_logicDeleteByIds() {
        dao.logicDeleteByIds(Arrays.asList(4L, 6L, 9L));
        db.sqlList().wantFirstSql().eq("" +
                "UPDATE fluent_mybatis.student " +
                "SET `gmt_modified` = now(), `is_deleted` = ? " +
                "WHERE `id` IN (?, ?, ?)"
            , StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eqList(true, 4L, 6L, 9L);
    }
}
