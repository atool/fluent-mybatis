package cn.org.atool.fluent.mybatis.test.method;

import cn.org.atool.fluent.mybatis.customize.StudentExtDao;
import cn.org.atool.fluent.mybatis.generator.ATM;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;

import java.util.Arrays;

/**
 * @author darui.wu 2019/10/29 9:36 下午
 */
public class DeleteByEntityIdsTest extends BaseTest {
    @Autowired
    private StudentExtDao dao;

    @Test
    public void test_deleteByEntityIds() {
        ATM.dataMap.student.initTable(10)
            .env.values("test_env")
            .cleanAndInsert();
        dao.deleteByEntityIds(Arrays.asList(new StudentEntity().setId(1L), new StudentEntity().setId(5L)));
        db.sqlList().wantFirstSql().eq("" +
            "DELETE FROM fluent_mybatis.student " +
            "WHERE `id` IN (?, ?)");
        ATM.dataMap.student.countEq(8);
    }

    @Test
    public void test_logicDeleteByEntityIds() {
        dao.logicDeleteByEntityIds(Arrays.asList(new StudentEntity().setId(1L), new StudentEntity().setId(5L)));
        db.sqlList().wantFirstSql().eq("" +
            "UPDATE fluent_mybatis.student " +
            "SET `gmt_modified` = now(), `is_deleted` = ? " +
            "WHERE `id` IN (?, ?)");
        db.sqlList().wantFirstPara().eqList(true, 1L, 5L);
    }

    @Test
    public void test_deleteByEntityIds2() {
        ATM.dataMap.student.initTable(10)
            .env.values("test_env")
            .cleanAndInsert();
        dao.deleteByEntityIds(new StudentEntity().setId(1L), new StudentEntity().setId(5L));
        db.sqlList().wantFirstSql().eq("" +
            "DELETE FROM fluent_mybatis.student " +
            "WHERE `id` IN (?, ?)");
        ATM.dataMap.student.countEq(8);
    }

    @Test
    public void test_logicDeleteByEntityIds2() {
        dao.logicDeleteByEntityIds(new StudentEntity().setId(1L), new StudentEntity().setId(5L));
        db.sqlList().wantFirstSql().eq("" +
                "UPDATE fluent_mybatis.student " +
                "SET `gmt_modified` = now(), `is_deleted` = ? " +
                "WHERE `id` IN (?, ?)"
            , StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eqList(true, 1L, 5L);
    }
}
