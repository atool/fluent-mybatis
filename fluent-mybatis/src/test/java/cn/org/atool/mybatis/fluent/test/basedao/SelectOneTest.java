package cn.org.atool.mybatis.fluent.test.basedao;

import cn.org.atool.mybatis.fluent.demo.dao.intf.UserDao;
import cn.org.atool.mybatis.fluent.demo.dm.table.UserTableMap;
import cn.org.atool.mybatis.fluent.demo.entity.UserEntity;
import cn.org.atool.mybatis.fluent.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author darui.wu
 * @create 2019/10/29 9:33 下午
 */
public class SelectOneTest extends BaseTest {
    @Autowired
    private UserDao dao;

    @Test
    public void test_selectOne() throws Exception {
        db.table(t_user).clean().insert(new UserTableMap(10).init()
                .user_name.values(DataGenerator.increase("username_%d")));

        UserEntity user = dao.selectOne("username");
        want.object(user).notNull();
        db.sqlList().wantFirstSql().start("SELECT")
                .end("FROM t_user WHERE user_name LIKE ? limit 1");
    }

    @Test
    public void test_selectOne2() throws Exception {
        db.table(t_user).clean().insert(new UserTableMap(10).init()
                .user_name.values(DataGenerator.increase("username_%d")));

        String username = dao.selectOne(5);
        want.string(username).eq("username_5");
        db.sqlList().wantFirstSql().start("SELECT")
                .end("FROM t_user WHERE id = ? limit 1");
    }
}
