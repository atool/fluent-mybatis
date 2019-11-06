package cn.org.atool.fluent.mybatis.test.basedao;

import cn.org.atool.fluent.mybatis.demo.dao.intf.UserDao;
import cn.org.atool.fluent.mybatis.demo.dm.table.UserTableMap;
import cn.org.atool.fluent.mybatis.demo.entity.UserEntity;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

/**
 * TODO:类说明
 *
 * @author darui.wu
 * @create 2019/10/31 11:17 上午
 */
public class SaveTest extends BaseTest {
    @Autowired
    private UserDao dao;

    @Test
    public void test_save() throws Exception {
        db.table(t_user).clean();
        dao.save(new UserEntity().setId(4L).setUserName("test name").setAge(43));
        db.sqlList().wantFirstSql().notContain(" id,");
        db.table(t_user).query().eqDataMap(UserTableMap.create(1)
                .user_name.values("test name")
                .age.values(43)
        );
    }

    @Test
    public void test_saveWithPk() throws Exception {
        db.table(t_user).clean();
        dao.saveWithPk(new UserEntity().setId(4L).setUserName("test name").setAge(43));
        db.sqlList().wantFirstSql().contains(" id,");
        db.table(t_user).query().eqDataMap(UserTableMap.create(1)
                .id.values(4)
                .user_name.values("test name")
                .age.values(43)
        );
    }

    @Test
    public void test_batchSaveWithPk() throws Exception {
        db.table(t_user).clean();
        dao.saveWithPk(Arrays.asList(new UserEntity().setId(4L).setUserName("test name1").setAge(43),
                new UserEntity().setId(5L).setUserName("test name2").setAge(43)
        ));
        db.table(t_user).query().eqDataMap(UserTableMap.create(2)
                .id.values(4, 5)
                .user_name.values("test name1", "test name2")
                .age.values(43)
        );
    }
}