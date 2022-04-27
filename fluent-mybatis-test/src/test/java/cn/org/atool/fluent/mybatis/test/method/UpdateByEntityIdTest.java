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
 * @author darui.wu
 * @create 2019/10/29 9:35 下午
 */
public class UpdateByEntityIdTest extends BaseTest {
    @Autowired
    private StudentExtDao dao;

    @Test
    public void test_byEntityId() throws Exception {
        ATM.dataMap.student.initTable(5)
            .cleanAndInsert();

        dao.updateEntityByIds(new StudentEntity().setId(2L).setUserName("test3").setAge(30));
        db.sqlList().wantFirstSql()
            .eq("UPDATE fluent_mybatis.student " +
                "SET `gmt_modified` = now(), `user_name` = ?, `age` = ? " +
                "WHERE `id` = ?", StringMode.SameAsSpace);
        ATM.dataMap.student.query("id=2")
            .eqDataMap(ATM.dataMap.student.table(1)
                .userName.values("test3")
                .age.values(30)
            );
    }

    @Test
    public void test_byEntityId2() throws Exception {
        ATM.dataMap.student.initTable(5)
            .cleanAndInsert();

        dao.updateEntityByIds(
            new StudentEntity().setId(2L).setUserName("test2").setAge(20),
            new StudentEntity().setId(3L).setUserName("test3").setAge(30));
        db.sqlList().wantFirstSql().eq("" +
            "UPDATE fluent_mybatis.student " +
            "SET `gmt_modified` = now(), `user_name` = ?, `age` = ? " +
            "WHERE `id` = ?; " +
            "UPDATE fluent_mybatis.student " +
            "SET `gmt_modified` = now(), `user_name` = ?, `age` = ? " +
            "WHERE `id` = ?", StringMode.SameAsSpace);
        ATM.dataMap.student.query("id in (2, 3)")
            .eqDataMap(ATM.dataMap.student.table(2)
                .userName.values("test2", "test3")
                .age.values(20, 30)
            );
    }

    @Test
    public void test_byEntityId_Collection() throws Exception {
        ATM.dataMap.student.initTable(5)
            .cleanAndInsert();

        dao.updateEntityByIds(Arrays.asList(
            new StudentEntity().setId(2L).setUserName("test2").setAge(20),
            new StudentEntity().setId(3L).setUserName("test3").setAge(30)));
        db.sqlList().wantFirstSql().eq("" +
            "UPDATE fluent_mybatis.student SET `gmt_modified` = now(), `user_name` = ?, `age` = ? " +
            "WHERE `id` = ?; " +
            "UPDATE fluent_mybatis.student SET `gmt_modified` = now(), `user_name` = ?, `age` = ? " +
            "WHERE `id` = ?", StringMode.SameAsSpace);
        ATM.dataMap.student.table(2)
            .userName.values("test2", "test3")
            .age.values(20, 30)
            .eqQuery("id in (2, 3)");
    }

    @Test
    public void test_byEntity3() throws Exception {
        ATM.dataMap.student.initTable(5)
            .env.values("test_env")
            .isDeleted.values(false)
            .cleanAndInsert();

        int count = dao.updateBy(
            new StudentEntity().setUserName("test2").setAge(20),
            new StudentEntity().setId(3L));
        db.sqlList().wantFirstSql().eq("" +
                "UPDATE fluent_mybatis.student SET `gmt_modified` = now(), `user_name` = ?, `age` = ? " +
                "WHERE `is_deleted` = ? AND `env` = ? AND `id` = ?"
            , StringMode.SameAsSpace);
        ATM.dataMap.student.table(1)
            .userName.values("test2")
            .age.values(20)
            .eqQuery("id = 3");
        want.number(count).isEqualTo(1);
    }
}
