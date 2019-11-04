package cn.org.atool.mbplus.test.basedao;

import cn.org.atool.mbplus.demo.dao.intf.UserDao;
import cn.org.atool.mbplus.demo.dm.table.UserTableMap;
import cn.org.atool.mbplus.demo.mapping.UserMP;
import cn.org.atool.mbplus.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

/**
 * @author darui.wu
 * @create 2019/10/29 9:36 下午
 */
public class DeleteByMapTest extends BaseTest {
    @Autowired
    private UserDao dao;

    @Test
    public void test_deleteByMap() throws Exception {
        db.table(t_user).clean().insert(new UserTableMap(10).init()
                .user_name.values("test1", "test12", "test3", "test12", "tess2")
        );
        dao.deleteByMap(new HashMap<String, Object>() {
            {
                this.put(UserMP.Column.user_name, "test12");
            }
        });
        db.sqlList().wantFirstSql().eq("DELETE FROM t_user WHERE user_name = ?");
        db.table(t_user).count().eq(8L);
    }
}
