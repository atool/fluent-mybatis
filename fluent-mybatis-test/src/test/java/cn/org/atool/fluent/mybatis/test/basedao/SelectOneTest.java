package cn.org.atool.fluent.mybatis.test.basedao;

import cn.org.atool.fluent.mybatis.customize.StudentExtDao;
import cn.org.atool.fluent.mybatis.generate.ATM;
import cn.org.atool.fluent.mybatis.generate.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;

/**
 * @author darui.wu
 * @create 2019/10/29 9:33 下午
 */
public class SelectOneTest extends BaseTest {
    @Autowired
    private StudentExtDao dao;

    @Test
    public void test_selectOne() throws Exception {
        ATM.DataMap.student.initTable(10)
            .userName.values(DataGenerator.increase("username_%d"))
            .cleanAndInsert();

        StudentEntity student = dao.selectOne("username");
        want.object(student).notNull();
        db.sqlList().wantFirstSql().start("SELECT")
            .end("FROM t_student WHERE user_name LIKE ? LIMIT ?, ?", StringMode.SameAsSpace);
    }

    @Test
    public void test_selectOne2() throws Exception {
        ATM.DataMap.student.initTable(10)
            .userName.values(DataGenerator.increase("username_%d"))
            .cleanAndInsert();

        String username = dao.selectOne(5);
        want.string(username).eq("username_5");
        db.sqlList().wantFirstSql().start("SELECT")
            .end("FROM t_student WHERE id = ? LIMIT ?, ?", StringMode.SameAsSpace);
    }
}
