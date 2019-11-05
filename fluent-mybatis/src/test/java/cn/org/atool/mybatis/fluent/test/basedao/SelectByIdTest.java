package cn.org.atool.mybatis.fluent.test.basedao;

import cn.org.atool.mybatis.fluent.demo.dao.intf.UserDao;
import cn.org.atool.mybatis.fluent.demo.dm.entity.UserEntityMap;
import cn.org.atool.mybatis.fluent.demo.dm.table.UserTableMap;
import cn.org.atool.mybatis.fluent.demo.entity.UserEntity;
import cn.org.atool.mybatis.fluent.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

/**
 * @author darui.wu
 * @create 2019/10/29 9:33 下午
 */
public class SelectByIdTest extends BaseTest {
    @Autowired
    private UserDao dao;

    @Test
    public void test_selectById() throws Exception {
        db.table(t_user).clean().insert(new UserTableMap(3).init()
                .user_name.values(DataGenerator.increase("username_%d")));
        UserEntity user = dao.selectById(3L);
        db.sqlList().wantFirstSql()
                .eq("SELECT id,user_name,address_id,gmt_created,gmt_modified,is_deleted,age,version FROM t_user WHERE id=?");
        want.object(user).eqHashMap(new UserEntityMap().userName.values("username_3"));
    }

    @Test
    public void test_selectByIds() throws Exception {
        db.table(t_user).clean().insert(new UserTableMap(10).init()
                .user_name.values(DataGenerator.increase("username_%d")));
        List<UserEntity> users = dao.selectByIds(Arrays.asList(3L, 5L));
        db.sqlList().wantFirstSql()
                .eq("SELECT id,user_name,address_id,gmt_created,gmt_modified,is_deleted,age,version " +
                        "FROM t_user WHERE id IN ( ? , ? )");
        want.object(users).eqDataMap(new UserEntityMap(2)
                .userName.values("username_3", "username_5")
        );
    }
}
