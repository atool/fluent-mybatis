package cn.org.atool.fluent.mybatis.test.basedao;

import cn.org.atool.fluent.mybatis.customize.StudentExtDao;
import cn.org.atool.fluent.mybatis.generate.ATM;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;

/**
 * @author darui.wu
 * @create 2019/10/29 9:35 下午
 */
public class CountTest extends BaseTest {
    @Autowired
    private StudentExtDao dao;

    @Test
    public void test_count() throws Exception {
        ATM.DataMap.student.initTable(10)
            .userName.values("test1", "test12", "test3", "test12", "tess2")
            .cleanAndInsert();
        int count = dao.count("test12");
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM t_student WHERE user_name = ?", StringMode.SameAsSpace);
        want.number(count).eq(2);
    }
}
