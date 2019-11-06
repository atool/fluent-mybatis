package cn.org.atool.fluent.mybatis.test.basedao;

import cn.org.atool.fluent.mybatis.demo.dao.intf.UserDao;
import cn.org.atool.fluent.mybatis.demo.dm.table.UserTableMap;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author darui.wu
 * @create 2019/10/29 9:34 下午
 */
public class SelectObjsTest extends BaseTest {
    @Autowired
    private UserDao dao;

    @Test
    public void test_selectObjs() throws Exception {
        db.table(t_user).clean().insert(new UserTableMap(10).init()
                .user_name.values(DataGenerator.increase("username_%d")));

        List<String> names = dao.selectObjs(2L, 3L, 5L);
        want.list(names).eqReflect(new String[]{"username_2", "username_3", "username_5"});
    }
}
