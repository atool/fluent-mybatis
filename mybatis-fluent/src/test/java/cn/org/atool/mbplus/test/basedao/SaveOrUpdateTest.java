package cn.org.atool.mbplus.test.basedao;

import cn.org.atool.mbplus.demo.dao.intf.UserDao;
import cn.org.atool.mbplus.demo.dm.table.UserTableMap;
import cn.org.atool.mbplus.demo.entity.UserEntity;
import cn.org.atool.mbplus.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author darui.wu
 * @create 2019/10/29 9:32 下午
 */
public class SaveOrUpdateTest extends BaseTest {
    @Autowired
    private UserDao dao;

    @Test
    public void test_saveOrUpdate() throws Exception {
        db.table(t_user).clean().insert(new UserTableMap(3).init());
        dao.saveOrUpdate(new UserEntity().setId(3L).setUserName("test_111").setAge(30));
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user WHERE id = ?");
        db.sqlList().wantSql(1).eq("UPDATE t_user SET user_name=?, gmt_modified=now(), age=? WHERE id=?");
        db.table(t_user).queryWhere("id=3").eqDataMap(new UserTableMap(1).user_name.values("test_111").age.values(30));
    }

    @Test
    public void test_saveOrUpdate_2() throws Exception {
        db.table(t_user).clean().insert(new UserTableMap(3).init());
        dao.saveOrUpdate(new UserEntity().setId(4L).setUserName("test_111").setAge(30));
        db.sqlList().wantFirstSql().eq("SELECT COUNT( 1 ) FROM t_user WHERE id = ?");
        db.sqlList().wantSql(1).contains("INSERT INTO t_user");
        db.table(t_user).count().eq(4L);
        db.table(t_user).queryWhere("id=4").eqDataMap(new UserTableMap(1).user_name.values("test_111").age.values(30));
    }

}
