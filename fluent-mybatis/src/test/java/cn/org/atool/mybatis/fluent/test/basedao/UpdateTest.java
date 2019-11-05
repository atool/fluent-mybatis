package cn.org.atool.mybatis.fluent.test.basedao;

import cn.org.atool.mybatis.fluent.demo.dao.intf.UserDao;
import cn.org.atool.mybatis.fluent.demo.dm.table.UserTableMap;
import cn.org.atool.mybatis.fluent.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * TODO:类说明
 *
 * @author darui.wu
 * @create 2019/10/31 11:16 上午
 */
public class UpdateTest extends BaseTest {
    @Autowired
    private UserDao dao;

    @Test
    public void test_update() throws Exception {
        db.table(t_user).clean().insert(new UserTableMap(5).init()
                .user_name.values(DataGenerator.increase("username_%d")));
        dao.updateUserNameById("new_user_name", 4L);
        db.sqlList().wantFirstSql().eq("UPDATE t_user SET user_name=?, gmt_modified=now() WHERE id = ?");
        db.table(t_user).queryWhere("id=4").eqDataMap(UserTableMap.create(1)
                .user_name.values("new_user_name")
        );
    }
}