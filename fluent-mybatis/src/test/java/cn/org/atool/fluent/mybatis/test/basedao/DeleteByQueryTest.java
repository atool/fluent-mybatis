package cn.org.atool.fluent.mybatis.test.basedao;

import cn.org.atool.fluent.mybatis.demo.dao.intf.UserDao;
import cn.org.atool.fluent.mybatis.demo.dm.table.UserTableMap;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DeleteByQueryTest extends BaseTest {
    @Autowired
    private UserDao dao;

    @Test
    public void test_deleteByQuery() throws Exception {
        db.table(t_user).clean().insert(new UserTableMap(10).init()
                .user_name.values(DataGenerator.increase("username_%d")));
        dao.deleteByQuery("username_4", "username_5", "username_7");
        db.table(t_user).count().eq(7L);
        db.sqlList().wantFirstSql().eq("DELETE FROM t_user WHERE user_name IN (?,?,?)");
    }
}