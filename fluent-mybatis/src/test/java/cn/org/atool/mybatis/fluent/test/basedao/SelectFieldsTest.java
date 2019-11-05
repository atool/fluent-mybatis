package cn.org.atool.mybatis.fluent.test.basedao;

import cn.org.atool.mybatis.fluent.demo.dao.intf.UserDao;
import cn.org.atool.mybatis.fluent.demo.dm.table.UserTableMap;
import cn.org.atool.mybatis.fluent.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author darui.wu
 * @create 2019/10/29 9:34 下午
 */
public class SelectFieldsTest extends BaseTest {
    @Autowired
    private UserDao dao;

    @Test
    public void test_selectFields() throws Exception {
        db.table(t_user).clean().insert(new UserTableMap(10).init()
                .user_name.values(DataGenerator.increase("username_%d")));

        List<String> names = dao.selectFields(3L, 5L, 8L);
        want.list(names).eqReflect(new String[]{"username_3", "username_5", "username_8"});
    }
}
