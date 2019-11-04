package cn.org.atool.mybatis.fluent.test.basedao;

import cn.org.atool.mybatis.fluent.demo.dao.intf.UserDao;
import cn.org.atool.mybatis.fluent.demo.dm.entity.UserEntityMap;
import cn.org.atool.mybatis.fluent.demo.dm.table.UserTableMap;
import cn.org.atool.mybatis.fluent.demo.entity.UserEntity;
import cn.org.atool.mybatis.fluent.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author darui.wu
 * @create 2019/10/29 9:34 下午
 */
public class SelectListTest extends BaseTest {
    @Autowired
    private UserDao dao;

    @Test
    public void test_selectList() throws Exception {
        db.table(t_user).clean().insert(new UserTableMap(10).init()
                .user_name.values(DataGenerator.increase("username_%d")));

        List<UserEntity> users = dao.selectList(3L, 6L, 7L);
        want.list(users).eqDataMap(new UserEntityMap(3)
                .id.values(3, 6, 7)
                .userName.values("username_3", "username_6", "username_7")
        );
    }
}
