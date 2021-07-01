package cn.org.atool.fluent.mybatis.test.basedao;

import cn.org.atool.fluent.mybatis.customize.StudentExtDao;
import cn.org.atool.fluent.mybatis.generate.ATM;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;
import org.test4j.tools.datagen.DataGenerator;

/**
 * @author darui.wu
 * @create 2019/10/29 9:33 下午
 */
public class SelectOneTest extends BaseTest {
    @Autowired
    private StudentExtDao dao;

    @Test
    public void test_selectOne() throws Exception {
        ATM.dataMap.student.initTable(10)
            .userName.values(DataGenerator.increase("username_%d"))
            .env.values("test_env")
            .cleanAndInsert();

        want.exception(() -> dao.selectOne("username"),
            MyBatisSystemException.class)
            .contains("Expected one result (or null) to be returned by selectOne(), but found");
        db.sqlList().wantFirstSql().start("SELECT")
            .end("FROM student " +
                "WHERE `is_deleted` = ? " +
                "AND `env` = ? " +
                "AND `user_name` LIKE ?", StringMode.SameAsSpace);
    }

    @Test
    public void test_selectOne2() throws Exception {
        ATM.dataMap.student.initTable(10)
            .userName.values(DataGenerator.increase("username_%d"))
            .env.values("test_env")
            .cleanAndInsert();

        String username = dao.selectOne(5);
        want.string(username).eq("username_5");
        db.sqlList().wantFirstSql().start("SELECT")
            .end("FROM student " +
                "WHERE `is_deleted` = ? " +
                "AND `env` = ? " +
                "AND `id` = ?", StringMode.SameAsSpace);
    }
}
