package cn.org.atool.fluent.mybatis.test.basedao;

import cn.org.atool.fluent.mybatis.demo.dao.intf.UserDao;
import cn.org.atool.fluent.mybatis.demo.dm.table.UserTableMap;
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
    private UserDao dao;

    @Test
    public void test_count() throws Exception {
        db.table(t_user).clean().insert(new UserTableMap(10).init()
                .user_name.values("test1", "test12", "test3", "test12", "tess2")
        );
        int count = dao.count("test12");
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user WHERE (user_name = ?)", StringMode.SameAsSpace);
        want.number(count).eq(2);
    }
}
